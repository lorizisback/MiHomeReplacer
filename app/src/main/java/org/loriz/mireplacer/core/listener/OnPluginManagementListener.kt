package org.loriz.mireplacer.core.listener

/**
 * Created by loriz on 2/5/18.
 */

interface OnPluginManagementListener {

    fun OnDownloadSuccess()
    fun OnDownloadError()
    fun OnDeleteSuccess()
    fun OnDeleteError()
    fun OnUploadSuccess()
    fun OnUploadError()

}