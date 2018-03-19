package org.loriz.mireplacer.utils

import android.app.ActivityManager
import android.content.Context
import android.app.Activity
import android.content.pm.PackageManager
import org.loriz.mireplacer.R
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import org.loriz.mireplacer.SplashScreenActivity
import org.loriz.mireplacer.core.Constants
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.ZipException
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
                Log.d("MiReplacer", "File problems!")
                return false }
            catch (e: ZipException) {
                Log.d("MiReplacer", "Zip is corrupt!")
                return false
            } finally {
                try {
                    if (zipfile != null) {
                        zipfile.close()
                    }
                } catch (e: IOException) {
                }

            }
        }

        fun downloadFile(url : String, path: String) : Boolean {
            var input: DataInputStream? = null
            var output: OutputStream? = null
            var connection: HttpURLConnection? = null
            var outPath = path.replace("download", ".mireplacer/download")
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
                output = DataOutputStream(FileOutputStream(outPath + ".temp"))

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
                var newPlugin = File(outPath + ".temp")

                Log.d("MiReplacer", "MD5 = "+MD5.calculateMD5(newPlugin))

                if (oldPlugin.exists()) {

                    if (path.contains("plugin/")) {
                        if (!Utils.isValidZip(newPlugin)) {
                            newPlugin.delete()
                            return false
                        }
                        deleteOldInstalledApk(path)
                    }
                    oldPlugin.delete()

                }

                newPlugin.renameTo(oldPlugin)


            } catch (e: Exception) {
                return false
            }
            return true
        }



        fun deleteOldInstalledApk(path: String) : Boolean {

            var newPath = path.replace("download", "install/mpk")

            newPath = newPath.substring(0, newPath.lastIndexOf("/")+1)

            var file = File(newPath)

            if (file != null && (file.exists() or file.isDirectory)) {

                if (file.isDirectory) {
                    file.listFiles().forEach {
                        it.delete()
                    }
                }

                file.delete()
                return true
            }
            return false
        }


        fun writeStringToFile(path: String, text: String) : Boolean {

            val stream = FileOutputStream(File(path))
            try {
                stream.write(text.toByteArray())
            } catch (e: Exception) {
                return false
            }
            finally {
                stream.close()
            }
            return true
        }


        fun deleteOldInstalledApk(item: Int) : Boolean {

            return deleteOldInstalledApk(Constants.pluginDownloadFolder + "/" + getFolderByInstalledItemId(item) + "/" + item + Constants.packageFileExtension)

        }



        fun deleteInstalledMPK(item : Int) : Boolean {
            val result = (File(Constants.pluginDownloadFolder + "/" + (getFolderByInstalledItemId(item) ?: 0) + "/" + item + Constants.packageFileExtension).delete()
                    or
                    File(Constants.getMiReplacerDownloadFolder() + "/" + (getFolderByInstalledItemId(item) ?: 0) + "/" + item + Constants.packageFileExtension + Constants.useBestFileExtension).delete())
            return result
        }


        fun getFolderByInstalledItemId(item: Int) : Int? {
            return Constants.MI_ITEMS.entries.find { it.value.installedVersion == item }?.value?.folderNumber
        }


        fun cleanUp(item: Int) : Boolean{
            return deleteInstalledMPK(item) or deleteOldInstalledApk(item)
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

        fun prepareMiReplacerFolder() {
            val downloadFolder = File(Constants.pluginDownloadFolder)

            val pluginsFolder = Constants.baseFolder
            val miReplacerFolder = Constants.miReplacerFolderName
            val miReplacerDownloadFolder = Constants.miReplacerDownloadFolderName

            val baseFolder = "$pluginsFolder/$miReplacerFolder/$miReplacerDownloadFolder"
            val baseFolderFile = File(baseFolder)
            if (!baseFolderFile.exists()) {
                //if mireplacer folder does not exists (maybe first time using MiReplacer), create the folder structure
                baseFolderFile.mkdirs()
                replicateFolderStructure(downloadFolder, baseFolderFile)
            } else {
                //mireplacer folder exists, update the folder structure
                replicateFolderStructure(downloadFolder, baseFolderFile)
            }

        }

        private fun replicateFolderStructure(fromFolder: File, toFolder: File) : Boolean {
            return if (fromFolder.exists() && toFolder.exists()) {

                fromFolder.listFiles().forEach {
                    if (it.isDirectory && !File(toFolder.absolutePath +"/"+ it.name).isDirectory()) {
                        File(toFolder.absolutePath +"/"+ it.name).mkdirs()
                    }
                }

                true

            } else false
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
            CHINESE, ITALIAN, OTHER, USEBEST
        }

    }

}