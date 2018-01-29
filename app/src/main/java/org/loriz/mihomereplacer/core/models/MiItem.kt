package org.loriz.mihomereplacer.core.models

/**
 * Created by loriz on 1/24/18.
 */

class MiItem {


    var itemName : String? = null
    var imgLink : String? = null
    var folderNumber : Int? = null
    var itemNumber : Int? = null
    var downloadLink : String? = null
    var latestmd5: String? = null
    var latestVersion: String? = null


    fun set(itemName : String?, imgLink: String?, folderNumber: Int?, itemNumber: Int?, downloadLink: String?) {
        this.itemName = itemName
        this.imgLink = imgLink
        this.folderNumber = folderNumber
        this.itemNumber = itemNumber
        this.downloadLink = downloadLink
    }



}