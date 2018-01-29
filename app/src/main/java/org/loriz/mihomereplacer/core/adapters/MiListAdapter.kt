package org.loriz.mihomereplacer.core.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.loriz.mihomereplacer.R
import org.loriz.mihomereplacer.utils.ImageUtils
import android.view.LayoutInflater
import org.loriz.mihomereplacer.core.models.InstalledMiItem
import org.loriz.mihomereplacer.utils.Utils


/**
 * Created by loriz on 1/29/18.
 */

class MiListAdapter(val context: Context, val installedPlugins: ArrayList<Pair<Int, InstalledMiItem>>) : RecyclerView.Adapter<MiListAdapter.MiViewHolder>() {

    override fun getItemCount(): Int {
        return installedPlugins.size
    }

    override fun onBindViewHolder(holder: MiViewHolder?, position: Int) {

        var item = installedPlugins[position]

        if (holder != null) {
            holder.itemName.text = item.second.miItem?.itemName
            holder.itemVersion.text = context.resources.getString(R.string.miitem_installed_version_label) + item.second.installedVersion
            holder.latestVersion.text = context.resources.getString(R.string.miitem_latest_version_label) + item.second.miItem?.latestVersion

            if (item.second.language != null) {
                when (item.second.language) {
                    Utils.Companion.PluginLanguage.CHINESE -> holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.chinese_flag))
                    Utils.Companion.PluginLanguage.ITALIAN -> holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.italian_flag))
                }
            } else {
                holder.flag.visibility = View.INVISIBLE
            }

            ImageUtils.display(context, item.second.miItem!!.imgLink, holder.image)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MiViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.mihome_item, parent, false)

        return MiViewHolder(itemView)
    }


    inner class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var itemName : TextView
        var image : ImageView
        var flag : ImageView
        var itemVersion : TextView
        var latestVersion : TextView

        init {
            image = view.findViewById(R.id.mi_item_image)
            itemName = view.findViewById(R.id.mi_item_item_name)
            flag = view.findViewById(R.id.mi_item_flag)
            itemVersion = view.findViewById(R.id.mi_item_plugin_version)
            latestVersion = view.findViewById(R.id.mi_item_plugin_latest_version)
        }


    }

}
