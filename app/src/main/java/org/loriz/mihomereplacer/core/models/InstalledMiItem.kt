package org.loriz.mihomereplacer.core.models

import org.loriz.mihomereplacer.utils.Utils

/**
 * Created by loriz on 1/24/18.
 */

class InstalledMiItem {

    var miItem : MiItem? = null
    var installedVersion : String? = null
    var language : Utils.Companion.PluginLanguage? = null
    var md5 : String? = null

}