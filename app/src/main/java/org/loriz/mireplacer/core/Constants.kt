package org.loriz.mireplacer.core

import android.os.Environment
import org.loriz.mireplacer.core.models.MiItem

/**
 * Created by loriz on 1/24/18.
 */


class Constants {


    companion object {

        var MI_ITEMS = hashMapOf<Int, MiItem>()
        var baseURL = "http://xcape.esy.es/xiaomi/smarthome/IT/"
        var remoteFileExtension = ".IT"
        var packageFileExtension = ".mpk"
        var useBestFileExtension = ".marker"
        var pluginDownloadFolder = Environment.getExternalStorageDirectory().path + "/plugin/download"
        var baseFolder = Environment.getExternalStorageDirectory().path + "/plugin"
        var miReplacerFolderName = ".mireplacer"
        var miReplacerDownloadFolderName = "download"

        var sharedPrefsLayoutMode = "ALT_LAYOUT"

        fun getMiReplacerDownloadFolder() : String {
            return baseFolder + "/" + miReplacerFolderName + "/" + miReplacerDownloadFolderName
        }

    }

    enum class ReplacerLayouts {FULL, COMPACT}



}