package org.loriz.mihomereplacer.utils

import android.app.ActivityManager
import android.content.Context
import android.app.Activity
import android.content.pm.PackageManager
import org.loriz.mihomereplacer.R
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import org.loriz.mihomereplacer.SplashScreenActivity
import org.loriz.mihomereplacer.core.Constants
import java.io.File


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

        /*fun isPluginModded(manifest: String) : Boolean? {

            manifest.replace("s/.*versionName='\([^']*\).*//*\1/p", "")

        }*/


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



        fun isPluginItalian(md5: String) : Boolean {

            Constants.MI_ITEMS.values.forEach {
                if (md5.equals(it.latestItaMD5, true) || md5.equals(it.previousItaMD5, true)) return true
            }
            return false
        }



        fun showFatalErrorDialog(context: Context, text: String) {
            val builder: AlertDialog.Builder =  AlertDialog.Builder(context)
            builder.setTitle(context.resources.getString(R.string.error_title))
                    .setMessage(text)
                    .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                        (context as SplashScreenActivity).finish()
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }


        fun getLatestInstalledMiItems(parentDir: File, extension: String): HashMap<Int, Int>? {
            val inFiles = HashMap<Int, Int>()
            val files = parentDir.listFiles()
            if (files != null && files.isNotEmpty() ) {
                for (file in files) {
                    if (file.isDirectory) {
                        val map = getLatestInstalledMiItems(file, extension)
                        if (map != null && map.isNotEmpty()) {
                            inFiles.putAll(map)
                        }
                    } else {

                        if (file.name.endsWith(extension)) {
                            var folderName = file.parent.split("/").last().toInt()

                            if (inFiles.containsKey(folderName)) {

                                if (inFiles[folderName]!!.toInt() <= file.name.removeSuffix(extension).toInt()) {
                                    inFiles.remove(folderName)
                                    inFiles.put(folderName, file.name.removeSuffix(extension).toInt())
                                }

                            } else {
                                inFiles.put(folderName, file.name.removeSuffix(extension).toInt())
                            }
                        }
                    }
                }
            }


            return inFiles
        }


        enum class Flag {
            CHINESE, ITALIAN, OTHER
        }

    }

}