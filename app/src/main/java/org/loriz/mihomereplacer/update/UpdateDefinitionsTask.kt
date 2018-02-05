package org.loriz.mihomereplacer.update

import android.content.Context
import android.os.AsyncTask
import android.text.Html
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.core.models.MiItemEntry
import org.loriz.mihomereplacer.core.models.MiItem
import org.loriz.mihomereplacer.utils.Utils
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


/**
 * Created by loriz on 1/24/18.
 */

open class UpdateDefinitionsTask(val context: Context) : AsyncTask<Void, Void, Elements?>() {

    val url : String = "http://xcape.esy.es/xiaomi/smarthome/PLUGIN.JSON"

    val mObjectMapper = ObjectMapper().registerModule(KotlinModule())
    var siteMap = hashMapOf<Int, MiItemEntry>()

    override fun doInBackground(vararg params: Void?): Elements? {

        //download plugin.json
        if (Utils.downloadFile(context, url, context.filesDir.path + "/plugins.json") != true) return null

        if (parseJSON() != true) return null

        // scrape descriptor
        try {
            val doc  = Jsoup.connect("http://www.xcapesoft.cloud/").get()
            val bottomMask = Jsoup.connect(doc.select("frame[name=bottommask]").first().absUrl("src")).get()
            val electro = bottomMask.select("div#Electro")
            return electro.select("div#cbp-pgcontainer > ul > li")
        } catch (e: Exception) {
            return null
        }

    }

    private fun parseJSON(): Boolean {

        try {
            val inputStream = context.openFileInput("plugins.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val composition = String(buffer, Charset.forName("UTF-8"))
            val typeRef = object : TypeReference<HashMap<Int, MiItemEntry>>() {}
            siteMap = mObjectMapper.readValue<HashMap<Int, MiItemEntry>>(composition, typeRef)

        } catch (ex: Exception) {
            return false
        }

        return true
    }


    override fun onPostExecute(result: Elements?) {


        result?.forEach {

            val miItem = MiItem()
            val baseDiv = it.select("div.cbp-pgcontent")

            miItem.itemName = Html.fromHtml(baseDiv.select("h4 > center").toString()).toString().replace("_"," ")
            miItem.imgLink = baseDiv.select("div.cbp-pgitem-flip > img").first().absUrl("src").toString()

            //bottom container parsing
            val bottomContainer = baseDiv.select("ul.cbp-pgoptions > li")


            val foldNum = bottomContainer[0].select("span").toString()
            miItem.folderNumber = Html.fromHtml(foldNum).split("\n").last().toInt()

            val itemNum = bottomContainer[1].select("span").toString()
            miItem.itemNumber = Html.fromHtml(itemNum).split("\n").last().toInt()

            miItem.latestItaMD5 = siteMap[miItem.folderNumber as Int]?.md5
            miItem.previousItaMD5 = siteMap[miItem.folderNumber as Int]?.md5Old
            miItem.latestVersion = siteMap[miItem.folderNumber as Int]?.lastPlugin?.toInt()
            miItem.previousVersion = siteMap[miItem.folderNumber as Int]?.oldPlugin?.toInt()

            miItem.downloadLink = bottomContainer[2].select("span > div.cbp-pgopttooltip").select("span").select("a").first().attr("href")

            Constants.MI_ITEMS.put(miItem.folderNumber as Int, miItem)
        }


        super.onPostExecute(result)
    }

}