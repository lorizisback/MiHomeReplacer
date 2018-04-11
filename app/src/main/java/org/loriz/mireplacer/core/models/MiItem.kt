package org.loriz.mireplacer.core.models

import org.loriz.mireplacer.utils.Utils

/**
 * Created by loriz on 1/24/18.
 */

class MiItem {


    var itemName : String? = null
    var imgLink : String? = null
    var folderNumber : Int? = null
    var itemNumber : Int? = null
    var downloadLink : String? = null
    var latestItaMD5: String? = null
    var latestVersion: Int? = null
    var previousItaMD5: String? = null
    var previousVersion: Int? = null
    var installedVersion : Int? = null
    var language : Utils.Companion.Flag? = null
    var md5 : String? = null


    fun set(itemName : String?, imgLink: String?, folderNumber: Int?, itemNumber: Int?, downloadLink: String?) {
        this.itemName = itemName
        this.imgLink = imgLink
        this.folderNumber = folderNumber
        this.itemNumber = itemNumber
        this.downloadLink = downloadLink
    }



}