package org.loriz.mihomereplacer.core

import android.os.Environment
import org.loriz.mihomereplacer.core.models.MiItem

/**
 * Created by loriz on 1/24/18.
 */


class Constants {


    companion object {

        var MI_ITEMS = hashMapOf<Int, MiItem>()
        var baseURL = "http://xcape.esy.es/xiaomi/smarthome/IT/"
        var remoteFileExtension = ".IT"
        var packageFileExtension = ".mpk"
        var pluginDownloadFolder = Environment.getExternalStorageDirectory().path + "/plugin/download"

    }

}