package org.loriz.mihomereplacer.update

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import org.loriz.mihomereplacer.R
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.core.listener.OnPluginDownloadListener
import org.loriz.mihomereplacer.core.models.MiItem
import org.loriz.mihomereplacer.utils.Utils

/**
 * Created by loriz on 2/5/18.
 */

open class UpdatePluginTask(val context : Context, val item : MiItem, val onPluginDownloadListener: OnPluginDownloadListener?) : AsyncTask<Void, Void, Boolean>() {

    var mProgressDialog : ProgressDialog? = null

    override fun onPreExecute() {

        Utils.killProcess(context, context.resources.getString(R.string.mi_home_package_name))

        mProgressDialog = ProgressDialog(context)
        mProgressDialog?.setMessage("Aggiornamento in corso...")
        mProgressDialog?.setCancelable(false);
        mProgressDialog?.setIndeterminate(true);
        mProgressDialog?.show()

        super.onPreExecute()
    }


    override fun doInBackground(vararg params: Void?): Boolean? {

        return Utils.downloadFile(context, Utils.composePluginUrl(item.installedVersion), Constants.pluginDownloadFolder + "/" + item.folderNumber + "/" + item.installedVersion + Constants.packageFileExtension)

    }

    override fun onPostExecute(result: Boolean?) {

        if (result != null && result) {

            onPluginDownloadListener?.OnDownloadSuccess()

        } else {
            onPluginDownloadListener?.OnDownloadError()

        }

        mProgressDialog?.dismiss()


        super.onPostExecute(result)
    }

}