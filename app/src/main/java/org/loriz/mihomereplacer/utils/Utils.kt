package org.loriz.mihomereplacer.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.app.Activity
import android.content.pm.PackageManager
import org.loriz.mihomereplacer.R
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import org.loriz.mihomereplacer.MainActivity


/**
 * Created by loriz on 1/24/18.
 */

class Utils {

    companion object {

        fun isAppRunning(context: Context, packageName: String): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos = activityManager.runningAppProcesses
            if (procInfos != null) {
                for (processInfo in procInfos) {
                    if (processInfo.processName == packageName) {
                        return true
                    }
                }
            }
            return false
        }

        fun killProcess(context: Context, packageName: String) {
                val am = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
                am.killBackgroundProcesses(packageName)
        }


        fun isAppInstalled(context: Context, packageName: String): Boolean {
            val pm = context.getPackageManager()
            try {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
            }

            return false
        }


        fun getMiHomeVersion(context: Context) : String? {

            try {
                // Executes the command.
                val packageInfo = context.getPackageManager().getPackageInfo(context.resources.getString(R.string.mi_home_package_name), 0)
                return packageInfo.versionName

            } catch (e: Exception) {
                return null
            }

        }


        fun showFatalErrorDialog(context: Context) {
            val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                AlertDialog.Builder(context)
            }
            builder.setTitle(context.resources.getString(R.string.error_network_title))
                    .setMessage(context.resources.getString(R.string.error_network_text))
                    .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                        (context as MainActivity).finish()
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }



    }

}