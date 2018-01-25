package org.loriz.mihomereplacer.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_panel.*
import org.loriz.mihomereplacer.R


/**
 * Created by loriz on 1/25/18.
 */

class MainPanelFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater!!.inflate(R.layout.fragment_main_panel, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swiperefresh.setOnRefreshListener {
            refreshList()
        }



    }



    private fun refreshList() {

    }

}