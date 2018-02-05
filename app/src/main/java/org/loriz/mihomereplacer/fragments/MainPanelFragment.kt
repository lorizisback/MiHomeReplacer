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
import org.loriz.mihomereplacer.utils.MD5
import org.loriz.mihomereplacer.utils.Utils
import java.io.File
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import org.loriz.mihomereplacer.core.adapters.HomeListAdapter
import org.loriz.mihomereplacer.core.listener.OnPluginDownloadListener
import org.loriz.mihomereplacer.core.models.MiItem


/**
 * Created by loriz on 1/25/18.
 */

class MainPanelFragment : Fragment() {

    val path = Constants.pluginDownloadFolder
    val extension = Constants.packageFileExtension

    var mAdapter : HomeListAdapter? = null
    var listInstalledPlugins: ArrayList<Pair<Int, MiItem>> = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater!!.inflate(R.layout.fragment_main_panel, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*swiperefresh.setOnRefreshListener {
            refreshList()
        }*/

        refreshList()


        mAdapter = HomeListAdapter(context, listInstalledPlugins, object : OnPluginDownloadListener {
            override fun OnDownloadSuccess() {

                refreshList()
                mAdapter?.notifyDataSetChanged()
                Toast.makeText(context, "Plugin aggiornato!", Toast.LENGTH_SHORT).show()
            }

            override fun OnDownloadError() {
                Toast.makeText(context, "Aggiornamento fallito!", Toast.LENGTH_SHORT).show()

            }
        })
        val mLayoutManager = LinearLayoutManager(context)
        recyclerview.setLayoutManager(mLayoutManager)
        recyclerview.setItemAnimator(DefaultItemAnimator())
        recyclerview.setAdapter(mAdapter)




    }



    private fun refreshList() {

        listInstalledPlugins.clear()
        Utils.getLatestInstalledMiItems(File(path), extension)?.forEach {
            val key = it.key

            var item = Constants.MI_ITEMS[key]

            if (item != null) {
                item.installedVersion = it.value.toInt()
                item.md5 = MD5.calculateMD5(File(path + "/" + key + "/" + it.value + extension))
                item.language = if (item.md5 != null && Utils.isPluginItalian(item.md5 as String)) {
                    Utils.Companion.Flag.ITALIAN
                } else if (item.installedVersion as Int >= item.previousVersion as Int) {
                    Utils.Companion.Flag.CHINESE
                } else {
                    Utils.Companion.Flag.OTHER
                }

                listInstalledPlugins.add(Pair(key, item))
            }


        }


    }

}