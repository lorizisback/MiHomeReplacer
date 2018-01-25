package org.loriz.mihomereplacer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.top_panel.*
import org.loriz.mihomereplacer.utils.Utils

/**
 * Created by loriz on 1/24/18.
 */

class MainActivity : AppCompatActivity() {

    var miHomeVersion : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)

        setupView()

        super.onCreate(savedInstanceState)
    }


    private fun setupView() {

        //////////////////// setup top panel //////////////////////

        if (Utils.isAppInstalled(this, resources.getString(R.string.mi_home_package_name))) {
            //mi home installed! proceed to differentiate modded and stock version

            top_panel_image_container.visibility = View.VISIBLE

            miHomeVersion = Utils.getMiHomeVersion(this)
            if (miHomeVersion?.count { it.equals(".") } == 3 && miHomeVersion?.split(".")?.last() == "00") {
                top_panel_mihome_mod_icon.visibility = View.VISIBLE
                top_panel_main_text.text = resources.getString(R.string.mihome_mod_detected)
            } else {
                top_panel_mihome_mod_icon.visibility = View.VISIBLE
                top_panel_main_text.text = resources.getString(R.string.mihome_mod_detected)
            }

            top_panel_version_text_upper.text = resources.getString(R.string.mi_home_version_prefix) + miHomeVersion

        } else {

            //mi home not installed! prompt to install "P" version

            top_panel_image_container.visibility = View.INVISIBLE
            top_panel_mihome_mod_icon.visibility = View.INVISIBLE

            top_panel_main_text.text = resources.getString(R.string.mi_home_not_detected)
            top_panel_main_text.setOnClickListener {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                startActivity(browserIntent)

            }
            top_panel_version_text_upper.visibility = View.GONE

        }

        ////////////////////////////////////////////////////////////


        //////////////////// setup main panel //////////////////////





    }


}