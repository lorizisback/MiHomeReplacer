package org.loriz.mireplacer.fragments

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_main_panel.*
import org.loriz.mireplacer.R
import org.loriz.mireplacer.core.Constants
import org.loriz.mireplacer.utils.MD5
import org.loriz.mireplacer.utils.Utils
import java.io.File
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import org.loriz.mireplacer.core.adapters.HomeListAdapter
import org.loriz.mireplacer.core.listener.OnPluginManagementListener
import org.loriz.mireplacer.core.models.MiItem


/**
 * Created by loriz on 1/25/18.
 */

class ReplacerFragment : Fragment() {

    lateinit var prefs : SharedPreferences
    var altLayoutEnabled : Boolean = false

    val path = Constants.pluginDownloadFolder
    val miReplacerDownloadFolderPath = Constants.getMiReplacerDownloadFolder()
    val extension = Constants.packageFileExtension
    var mAdapter : HomeListAdapter? = null
    var listInstalledPlugins: ArrayList<Pair<Int, MiItem>> = arrayListOf()

    val pluginManagerListener = object : OnPluginManagementListener {
        override fun OnUploadSuccess() {}

        override fun OnUploadError() {}

        override fun OnDownloadSuccess() {
            refreshList()
            mAdapter?.notifyDataSetChanged()
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Toast.makeText(context, "Plugin aggiornato!", Toast.LENGTH_SHORT).show()
        }

        override fun OnDownloadError() {
            Toast.makeText(context, "Aggiornamento fallito!", Toast.LENGTH_SHORT).show()

        }

        override fun OnDeleteError() {
            Toast.makeText(context, "Cancellazione fallita!", Toast.LENGTH_SHORT).show()

        }

        override fun OnDeleteSuccess() {
            refreshList()
            mAdapter?.notifyDataSetChanged()
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))
            Toast.makeText(context, "Cancellazione completata!", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.replacer_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.replacer_menu_change_layout -> {cycleLayout(); true}
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        prefs = activity.getPreferences(Context.MODE_PRIVATE)
        altLayoutEnabled = when (prefs.getString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString())) {
            Constants.ReplacerLayouts.FULL.toString() -> false
            else -> true
        }
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_main_panel, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshList()
        setupContainer()
    }



    fun setupContainer() {
        recyclerview.adapter = null
        recyclerview.layoutManager = null

        mAdapter = HomeListAdapter(context, listInstalledPlugins, pluginManagerListener)

        val mLayoutManager = if (altLayoutEnabled) {
            GridLayoutManager(context, 2)
        } else {
            LinearLayoutManager(context)
        }
        recyclerview.setLayoutManager(mLayoutManager)
        recyclerview.setItemAnimator(DefaultItemAnimator())
        recyclerview.setAdapter(mAdapter)
    }



    fun cycleLayout() {

        if (prefs.contains(Constants.sharedPrefsLayoutMode)) {
            when (prefs.getString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString())) {
                Constants.ReplacerLayouts.FULL.toString() -> {
                    prefs.edit().putString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.COMPACT.toString()).apply()
                    altLayoutEnabled = true
                }
                else -> {
                    prefs.edit().putString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString()).apply()
                    altLayoutEnabled = false
                }
            }
        } else {
            prefs.edit().putString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString()).apply()
        }

        setupContainer()
    }




    private fun refreshList() {

        listInstalledPlugins.clear()
        Utils.getLatestInstalledMiItems(File(path), extension)?.forEach {
            val key = it.key
            val item = Constants.MI_ITEMS[key]

            if (item != null ) {
                item.installedVersion = it.value.toInt()
                item.md5 = MD5.calculateMD5(File("$path/$key/${it.value}$extension")).toUpperCase()
                if (item.md5 == null) {
                    item.md5 = "0000000000000000000000000000"
                }

                item.language = if (item.md5 == item.latestItaMD5 && item.installedVersion == item.latestVersion) {
                    Utils.Companion.Flag.ITALIAN
                } else if (File("$miReplacerDownloadFolderPath/$key/${it.value}$extension${Constants.useBestFileExtension}").isFile) {
                    Utils.Companion.Flag.USEDBEST
                } else if (item.installedVersion as Int >= item.latestVersion as Int) {
                Utils.Companion.Flag.CHINESE
                } else if (item.installedVersion as Int != item.latestVersion as Int) {
                    Utils.Companion.Flag.USEBEST
                } else {
                    Utils.Companion.Flag.OTHER
                }
                listInstalledPlugins.add(Pair(key, item))
            }


            else {

                Utils.Companion.Flag.UNKNOWN
                val key = it.key
                val newItem = MiItem()
                    with(newItem) {
                        itemName = "Plugin Sconosciuto"
                        imgLink = "http://xcape.esy.es/xiaomi/smarthome/img/000.png"
                        folderNumber = 0
                        itemNumber = 0
                        downloadLink = ""
                        latestItaMD5 = "00000000000000"
                        latestVersion = 0
                        previousItaMD5 = "00000000"
                        previousVersion = 0
                        language = Utils.Companion.Flag.UNKNOWN
                        md5 = "000000000000"
                    }

                newItem.installedVersion = it.value.toInt()
                newItem.folderNumber = it.key.toInt()
                    listInstalledPlugins.add(Pair(key, newItem))

                }



            }


        }


    }


