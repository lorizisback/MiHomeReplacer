package org.loriz.mireplacer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.bottom_panel.*
import org.loriz.mireplacer.core.Constants
import org.loriz.mireplacer.fragments.ReplacerFragment
import org.loriz.mireplacer.fragments.WarningFragment
import org.loriz.mireplacer.utils.Utils
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


/**
 * Created by loriz on 1/24/18.
 */

class MainActivity : AppCompatActivity() {

    var miHomeVersion : String? = null
    var isPVersion: Boolean = false
    var isInstalled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.app_name) +" "+ getString(R.string.mi_home_version_prefix) + getString(R.string.mi_home_version) + " Beta"


        with (getPreferences(Context.MODE_PRIVATE)) {
            if (!this.contains(Constants.sharedPrefsLayoutMode)) {
                this.edit().putString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString()).apply()
            }
        }

        Fabric.with(this, Crashlytics())

        Utils.prepareMiReplacerFolder()

        Utils.killProcess(this, resources.getString(R.string.mi_home_package_name))

        setupView()

        super.onCreate(savedInstanceState)
    }





    private fun setupView() {

        //////////////////// setup top panel //////////////////////

        isInstalled = Utils.isAppInstalled(this, resources.getString(R.string.mi_home_package_name))

        if (isInstalled) {
            //mi home installed! proceed to differentiate modded and stock version

            bottom_panel_icon_container.visibility = View.VISIBLE

            miHomeVersion = Utils.getMiHomeVersion(this)
            isPVersion = (miHomeVersion?.endsWith(".00", true) ?: false)
            if (isPVersion) {
                bottom_panel_mihome_mod_icon.visibility = View.VISIBLE
                bottom_panel_mihome_stock_icon.visibility = View.GONE
                bottom_panel_main_text.text = resources.getString(R.string.mihome_mod_detected)
            } else {
                bottom_panel_mihome_mod_icon.visibility = View.GONE
                bottom_panel_mihome_stock_icon.visibility = View.VISIBLE
                bottom_panel_main_text.text = resources.getString(R.string.mihome_stock_detected)
            }

            bottom_panel_version_text_upper.text = resources.getString(R.string.mi_home_version_prefix) + miHomeVersion

        } else {

            //mi home not installed! prompt to install "P" version

            bottom_panel_mihome_mod_icon.visibility = View.GONE
            bottom_panel_no_mihome_icon.visibility = View.VISIBLE

            bottom_panel_main_text.text = resources.getString(R.string.mi_home_not_detected)

            bottom_panel_version_text_upper.visibility = View.GONE

        }

        ////////////////////////////////////////////////////////////


    }

    override fun onResume() {

        var fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        var frag : Fragment
        if (isPVersion) {
            frag = ReplacerFragment()
        } else {
            frag = WarningFragment()
            var bundle = Bundle()

            if (isInstalled) {
                bundle.putString(WarningFragment.STRING_KEY, resources.getString(R.string.mihome_support_mod_only))
                frag.setArguments(bundle)
            } else {
                bundle.putString(WarningFragment.STRING_KEY, resources.getString(R.string.mihome_install_mod))
                frag.setArguments(bundle)
            }

        }

        fragmentTransaction.add(R.id.mainPanel, frag)
        fragmentTransaction.commit()

        super.onResume()
    }


    override fun onStop() {
        finish()
        super.onStop()
    }

}