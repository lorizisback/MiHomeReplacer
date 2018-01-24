package org.loriz.mihomereplacer

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.loriz.mihomereplacer.utils.Utils

/**
 * Created by loriz on 1/24/18.
 */

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(name: String?, context: Context?, attrs: AttributeSet?): View {

        button.setOnClickListener {

            Log.d("MIHOME", Utils.isAppRunning(this, "com.xiaomi.smarthome").toString())

        }

        return super.onCreateView(name, context, attrs)
    }

}