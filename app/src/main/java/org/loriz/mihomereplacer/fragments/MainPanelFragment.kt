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
import org.loriz.mihomereplacer.core.models.MiItem
import org.loriz.mihomereplacer.utils.Utils
import java.io.File


/**
 * Created by loriz on 1/25/18.
 */

class MainPanelFragment : Fragment() {

    var installedPlugins : HashMap<Int, MiItem?> = hashMapOf()

    var path = Environment.getExternalStorageDirectory().path + "/plugin/installed"


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater!!.inflate(R.layout.fragment_main_panel, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swiperefresh.setOnRefreshListener {
            refreshList()
        }

        Utils.getList(File(path)).forEach {
            val number = it.toInt()
            installedPlugins.put(number, Constants.MI_ITEMS[number])
        }


        installedPlugins.entries.toString()


    }



    private fun refreshList() {

    }

}