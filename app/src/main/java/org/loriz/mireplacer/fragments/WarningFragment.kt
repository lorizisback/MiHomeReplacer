package org.loriz.mireplacer.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.loriz.mireplacer.R

/**
 * Created by loriz on 2/14/18.
 */

class WarningFragment : Fragment() {

    companion object {
        val STRING_KEY = "text"
    }

    lateinit var msg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        msg = arguments.getString(STRING_KEY)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_empty, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        view?.findViewById<TextView>(R.id.empty_fragment_text)?.setText(msg)
        view?.findViewById<Button>(R.id.empty_fragment_telegram_link_button)?.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(activity.resources.getString(R.string.telegram_link)))
            startActivity(browserIntent)

        }

        super.onViewCreated(view, savedInstanceState)
    }

}