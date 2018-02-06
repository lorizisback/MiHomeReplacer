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
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipFile


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



        fun isPluginItalian(md5: String) : Boolean {

            Constants.MI_ITEMS.values.forEach {
                if (md5.equals(it.latestItaMD5, true) || md5.equals(it.previousItaMD5, true)) return true
            }
            return false
        }


        fun composePluginUrl(itemId: Int?) : String {

            return Constants.baseURL+(itemId ?: "")+Constants.remoteFileExtension

        }

        fun isValidZip(file: File): Boolean {
            var zipfile: ZipFile? = null
            try {
                zipfile = ZipFile(file)
                return true
            } catch (e: IOException) {
                return false
            } finally {
                try {
                    if (zipfile != null) {
                        zipfile.close()
                        zipfile = null
                    }
                } catch (e: IOException) {
                }

            }
        }

        fun downloadFile(context: Context, url : String, path: String, md5: String? = null) : Boolean {
            var input: DataInputStream? = null
            var output: OutputStream? = null
            var connection: HttpURLConnection? = null
            try {
                val url = URL(url)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return false
                }

                // download the file
                input = DataInputStream(url.openStream())
                output = DataOutputStream(FileOutputStream(path+".temp"))

                val contentLength = connection.getContentLength()


                val data = ByteArray(contentLength)

                /*var count: Int = -1
                while ({count = input!!.read(data); count}() != -1) {
                    output.write(data, 0, count)
                }*/

                input.readFully(data)
                input.close()

                output.write(data)
                output.flush()
                output.close()

                var oldPlugin = File(path)
                var newPlugin = File(path+".temp")

                if (oldPlugin.exists()) {

                    if (path.contains("plugin/")) {
                        if (!Utils.isValidZip(newPlugin)) {
                            return false
                        }
                        deleteOldInstalledeApk(path)
                    }
                    oldPlugin.delete()
                }

                newPlugin.renameTo(oldPlugin)
            } catch (e: Exception) {
                return false
            } finally {
                try {
                    if (output != null)
                        output.close()
                    if (input != null)
                        input.close()
                } catch (ignored: IOException) {
                    return false
                }

                if (connection != null)
                    connection.disconnect()
            }

            return true

        }



        fun deleteOldInstalledeApk(path: String) {

            var file = File(path.replace("download", "install/mpk").replace(".mpk", ".apk")).parentFile

            if (file.exists()) {

                if (file.isDirectory) {
                    file.listFiles().forEach {
                        it.delete()
                    }
                }

                file.delete()
            }

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