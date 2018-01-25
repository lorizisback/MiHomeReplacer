package org.loriz.mihomereplacer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*
import org.jsoup.select.Elements
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.update.UpdateAllTask
import org.loriz.mihomereplacer.utils.Utils


/**
 * Created by loriz on 1/24/18.
 */


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_splashscreen)

        Handler().postDelayed({

            object : UpdateAllTask() {

                override fun onPostExecute(result: Elements?) {

                    splashscreen_text.text = baseContext.resources.getString(R.string.updating)

                    super.onPostExecute(result)

                    if (Constants.MI_ITEMS.isEmpty()) {
                        Utils.showFatalErrorDialog(this@SplashScreenActivity)
                    } else {
                        this@SplashScreenActivity.startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    }

                }

            }.execute()

        }, 1500L)

        super.onCreate(savedInstanceState)
    }



}