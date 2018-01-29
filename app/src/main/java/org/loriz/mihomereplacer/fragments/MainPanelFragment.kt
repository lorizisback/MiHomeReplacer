package org.loriz.mihomereplacer.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_panel.*
import org.loriz.mihomereplacer.R
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.core.models.InstalledMiItem
import org.loriz.mihomereplacer.utils.MD5
import org.loriz.mihomereplacer.utils.Utils
import java.io.File
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import org.loriz.mihomereplacer.core.adapters.MiListAdapter


/**
 * Created by loriz on 1/25/18.
 */

class MainPanelFragment : Fragment() {

    var installedPlugins : LinkedHashMap<Int, ArrayList<InstalledMiItem>> = linkedMapOf()

    var listInstalledPlugins : ArrayList<Pair<Int, InstalledMiItem>> = arrayListOf()

    val path = Environment.getExternalStorageDirectory().path + "/plugin/installed"
    val extension = ".apk"

    var mAdapter : MiListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater!!.inflate(R.layout.fragment_main_panel, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*swiperefresh.setOnRefreshListener {
            refreshList()
        }*/

        Utils.getInstalledMiItems(File(path))?.forEach {
            val key = it.key

            it.value.forEach {

                var item = InstalledMiItem()
                item.miItem = Constants.MI_ITEMS[key]
                item.installedVersion = it
                item.md5 = MD5.calculateMD5(File(path + "/" + key + "/" + it + extension))
                if (item.md5 == item.miItem?.latestmd5) {
                    item.language = Utils.Companion.PluginLanguage.ITALIAN
                } else {
                    item.language = Utils.Companion.PluginLanguage.CHINESE
                }

                if (installedPlugins[key] != null) {
                    installedPlugins[key]?.add(item)
                } else {
                    installedPlugins.put(key, arrayListOf(item))
                }

                listInstalledPlugins.add(Pair(key, item))
            }

        }


        mAdapter = MiListAdapter(context, listInstalledPlugins)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(mLayoutManager)
        recyclerview.setItemAnimator(DefaultItemAnimator())
        recyclerview.setAdapter(mAdapter)



    }



    private fun refreshList() {

        mAdapter?.notifyDataSetChanged()

    }

}