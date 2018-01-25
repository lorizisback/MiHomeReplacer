package org.loriz.mihomereplacer.update

import android.os.AsyncTask
import android.text.Html
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.core.models.MiItem

/**
 * Created by loriz on 1/24/18.
 */

open class UpdateAllTask : AsyncTask<Void, Void, Elements?>() {


    override fun doInBackground(vararg params: Void?): Elements? {

        try {
            val doc  = Jsoup.connect("http://www.xcapesoft.cloud/").get()
            val bottomMask = Jsoup.connect(doc.select("frame[name=bottommask]").first().absUrl("src")).get()
            val electro = bottomMask.select("div#Electro")
            return electro.select("div#cbp-pgcontainer > ul > li")
        } catch (e: Exception) {
            return null
        }

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

            miItem.downloadLink = bottomContainer[2].select("span > div.cbp-pgopttooltip").select("span").select("a").first().attr("href")

            Constants.MI_ITEMS.put(miItem.folderNumber as Int, miItem)
        }


        super.onPostExecute(result)
    }


}