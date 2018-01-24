package org.loriz.mihomereplacer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.jsoup.select.Elements
import org.loriz.mihomereplacer.core.models.MiItem
import org.loriz.mihomereplacer.update.UpdateAllTask


/**
 * Created by loriz on 1/24/18.
 */


class SplashScreenActivity : AppCompatActivity() {

    var miItemsMap : HashMap<Int, MiItem> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_splashscreen)

        Handler().postDelayed({

        try {

            object : UpdateAllTask() {

                override fun onPostExecute(result: Elements?) {
                    super.onPostExecute(result)

                    this@SplashScreenActivity.startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                }

            }.execute()

        }catch (e : Exception) {

            Toast.makeText(this@SplashScreenActivity, "Errore di rete! Si prega di riprovare piu' tardi.", Toast.LENGTH_LONG).show()
            finish()
        }


        }, 1500L)

        super.onCreate(savedInstanceState)
    }



}