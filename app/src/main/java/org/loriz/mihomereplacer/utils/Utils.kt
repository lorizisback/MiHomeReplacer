package org.loriz.mihomereplacer.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE



/**
 * Created by loriz on 1/24/18.
 */

class Utils {

    companion object {

        fun isAppRunning(context: Context, packageName: String): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos = activityManager.getRunningTasks(Integer.MAX_VALUE)
            if (procInfos != null) {
                procInfos.forEach {

                    if (it.baseActivity.packageName == packageName) return true

                }
            }
            return false
        }





    }

}