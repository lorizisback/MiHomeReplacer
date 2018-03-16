package org.loriz.mireplacer.update

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.loriz.mireplacer.R
import org.loriz.mireplacer.core.Constants
import org.loriz.mireplacer.core.listener.OnPluginManagementListener
import org.loriz.mireplacer.utils.Utils
import java.io.File
import java.io.FileInputStream


class UploadPluginWithFTPTask(val context : Context, val pluginNumber: Int, val onPluginManagementListener: OnPluginManagementListener?) : AsyncTask<Void, Void, Boolean>() {

    lateinit var con: FTPClient
    lateinit var mProgressDialog : ProgressDialog


    override fun onPreExecute() {

        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage(context.getString(R.string.upload_plugin_dialog_message))
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show()

        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): Boolean {

        var pluginFolder = Utils.getFolderByInstalledItemId(pluginNumber) ?: return false


        try {
            con = FTPClient()
            con.connect("ftp.xcape.esy.es")

            if (con.login("u159600289.replacer", "replacer1$#")) {
                con.enterLocalPassiveMode() // important!
                con.setFileType(FTP.BINARY_FILE_TYPE)

                val data = Constants.pluginDownloadFolder + "/" + pluginFolder + "/" + pluginNumber + Constants.packageFileExtension

                val `in` = FileInputStream(File(data))
                val result = con.storeFile("/${pluginFolder}_${pluginNumber}", `in`)
                `in`.close()
                if (result) Log.v("MiReplacer", "Upload of plugin ${pluginNumber} successful!")
                con.logout()
                con.disconnect()
                return true
            } else {
                Log.d("MiReplacer", "Upload failed: wrong credentials?")
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }


    }

    override fun onPostExecute(result: Boolean) {

        mProgressDialog.dismiss()


        if (result) {

            onPluginManagementListener?.OnUploadSuccess()

        } else {

            onPluginManagementListener?.OnUploadError()

        }


        super.onPostExecute(result)
    }

}