package org.loriz.mireplacer.update

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.loriz.mireplacer.R
import org.loriz.mireplacer.core.Constants
import org.loriz.mireplacer.core.listener.OnPluginManagementListener
import org.loriz.mireplacer.core.models.MiItem
import org.loriz.mireplacer.utils.Utils
import java.io.File

/**
 * Created by loriz on 2/5/18.
 */

open class UpdatePluginTask(val context : Context, val item : MiItem, val onPluginManagementListener: OnPluginManagementListener?, val overriddenVersion: Int? = null) : AsyncTask<Void, Void, Boolean>() {

    var mProgressDialog : ProgressDialog? = null

    override fun onPreExecute() {

        Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))

        mProgressDialog = ProgressDialog(context)
        mProgressDialog?.setMessage(context.getString(R.string.update_plugin_dialog_message))
        mProgressDialog?.setCancelable(false);
        mProgressDialog?.setIndeterminate(true);
        mProgressDialog?.show()

        super.onPreExecute()
    }


    override fun doInBackground(vararg params: Void?): Boolean? {

        var result = false
        var counter = 0
        while (result == false && counter<5) {
            result = Utils.downloadFile(Utils.composePluginUrl(overriddenVersion ?: item.installedVersion), Constants.pluginDownloadFolder + "/" + item.folderNumber + "/" + item.installedVersion + Constants.packageFileExtension)
            counter++
        }

        return result

    }

    override fun onPostExecute(result: Boolean?) {

        val path = Constants.getMiReplacerDownloadFolder() + "/" + item.folderNumber + "/" + item.installedVersion + Constants.packageFileExtension

        if (result != null && result) {

            if (overriddenVersion != null) {
                if (!Utils.writeStringToFile(path + Constants.useBestFileExtension, path + Constants.useBestFileExtension)) {
                    File(path + Constants.useBestFileExtension).createNewFile()
                }
                //Utils.writeStringToFile(path + Constants.useBestFileExtension, path + Constants.useBestFileExtension)
            } else {
                if (File(path + Constants.useBestFileExtension).exists())  {
                    File(path + Constants.useBestFileExtension).delete()
                }
            }

            onPluginManagementListener?.OnDownloadSuccess()

        } else {
            onPluginManagementListener?.OnDownloadError()

        }

        mProgressDialog?.dismiss()

        super.onPostExecute(result)
    }

}