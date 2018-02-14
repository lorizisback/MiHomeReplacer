package org.loriz.mireplacer.core.adapters

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.loriz.mireplacer.R
import org.loriz.mireplacer.utils.ImageUtils
import android.view.LayoutInflater
import org.loriz.mireplacer.core.listener.OnPluginManagementListener
import org.loriz.mireplacer.core.models.MiItem
import org.loriz.mireplacer.update.UpdatePluginTask
import org.loriz.mireplacer.utils.Utils


/**
 * Created by loriz on 1/29/18.
 */

class HomeListAdapter(val context: Context, val installedPlugins: ArrayList<Pair<Int, MiItem>>, val onPluginManagementListener: OnPluginManagementListener? = null) : RecyclerView.Adapter<HomeListAdapter.MiViewHolder>() {

    override fun getItemCount(): Int {
        return installedPlugins.size
    }

    override fun onBindViewHolder(holder: MiViewHolder?, position: Int) {


        var item = installedPlugins[position]

        if (holder != null) {

            holder.item = item.second

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                holder.container.clipToOutline = true
            }
            holder.itemName.text = item.second.itemName
            holder.latestVersion.text = context.resources.getString(R.string.miitem_latest_version_label) + item.second.latestVersion+ " IT"
            holder.itemVersion.text = context.resources.getString(R.string.miitem_installed_version_label) + item.second.installedVersion

            if (item.second.language != null) {
                when (item.second.language) {
                    Utils.Companion.Flag.CHINESE -> holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.chinese_flag))
                    Utils.Companion.Flag.ITALIAN -> {
                        holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.italian_flag))
                        holder.itemVersion.text = holder.itemVersion.text.toString() + " IT"
                    }
                    Utils.Companion.Flag.OTHER -> {
                        holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.old_flag))
                    }
                }
            } else {
                holder.flag.visibility = View.INVISIBLE
            }

            ImageUtils.display(context, item.second.imgLink, holder.image)

            holder.container.setOnClickListener {

                if (item.second.language != Utils.Companion.Flag.ITALIAN && (item.second.installedVersion as Int <= item.second.latestVersion as Int)) {

                    val builder: AlertDialog.Builder =  AlertDialog.Builder(context)
                    builder.setMessage("Vuoi scaricare il plugin ${item.second.installedVersion} italiano?"  )
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                object : UpdatePluginTask(context, item.second, onPluginManagementListener){}.execute()
                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()

                } else if (item.second.language == Utils.Companion.Flag.ITALIAN) {
                    val builder: AlertDialog.Builder =  AlertDialog.Builder(context)
                    builder.setMessage("Vuoi cancellare il plugin?")
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                if (Utils.cleanUp(item.second.installedVersion as Int)) {

                                    onPluginManagementListener?.OnDeleteSuccess()
                                } else {
                                    onPluginManagementListener?.OnDeleteError()
                                }
                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MiViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.mihome_item, parent, false)

        return MiViewHolder(itemView)
    }



    inner class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var item: MiItem? = null

        var container: View
        var itemName : TextView
        var image : ImageView
        var flag : ImageView
        var itemVersion : TextView
        var latestVersion : TextView

        init {
            container = view.findViewById(R.id.mi_item_container)
            image = view.findViewById(R.id.mi_item_image)
            itemName = view.findViewById(R.id.mi_item_item_name)
            flag = view.findViewById(R.id.mi_item_flag)
            itemVersion = view.findViewById(R.id.mi_item_plugin_version)
            latestVersion = view.findViewById(R.id.mi_item_plugin_latest_version)
        }


    }

}
