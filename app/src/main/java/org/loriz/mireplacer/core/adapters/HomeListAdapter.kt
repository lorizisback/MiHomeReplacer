package org.loriz.mireplacer.core.adapters

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.loriz.mireplacer.R
import org.loriz.mireplacer.utils.ImageUtils
import android.view.LayoutInflater
import android.widget.Toast
import org.loriz.mireplacer.MainActivity
import org.loriz.mireplacer.core.Constants
import org.loriz.mireplacer.core.listener.OnPluginManagementListener
import org.loriz.mireplacer.core.models.MiItem
import org.loriz.mireplacer.update.UpdatePluginTask
import org.loriz.mireplacer.update.UploadPluginWithFTPTask
import org.loriz.mireplacer.utils.Utils




/**
 * Created by loriz on 1/29/18.
 */

class HomeListAdapter(val context: Context, val installedPlugins: ArrayList<Pair<Int, MiItem>>, val onPluginManagementListener: OnPluginManagementListener? = null) : RecyclerView.Adapter<HomeListAdapter.MiViewHolder>() {

    var layoutType = (context as MainActivity).getPreferences(Context.MODE_PRIVATE).getString(Constants.sharedPrefsLayoutMode, Constants.ReplacerLayouts.FULL.toString())


    override fun getItemCount(): Int {
        return installedPlugins.size
    }

    override fun onBindViewHolder(holder: MiViewHolder?, position: Int) {


        var item = installedPlugins[position]

        if (holder != null) {

            when (layoutType) {
                Constants.ReplacerLayouts.FULL.toString() -> setupFullViewHolder(holder, item)
                Constants.ReplacerLayouts.COMPACT.toString() -> setupCompactViewHolder(holder, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MiViewHolder {
        val layout = when (layoutType) {
            Constants.ReplacerLayouts.FULL.toString() -> R.layout.mihome_item
            else -> R.layout.mihome_compact_item
        }

        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(layout, parent, false)

        return MiViewHolder(itemView)
    }

    ///////////////////////////////////////// SETUP OF VIEWHOLDERS ////////////////////////////////////////////////

    private fun setupFullViewHolder(holder: MiViewHolder, item: Pair<Int, MiItem>) {

        holder.item = item.second

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.container.clipToOutline = true
        }

        holder.itemName.text = item.second.itemName

        holder.latestVersion.visibility = View.VISIBLE
        holder.itemVersion.visibility = View.VISIBLE

        holder.latestVersion.text = context.resources.getString(R.string.miitem_latest_version_label) + item.second.latestVersion+ " IT"
        holder.itemVersion.text = context.resources.getString(R.string.miitem_installed_version_label) + item.second.installedVersion

        if (item.second.language != null) {
            when (item.second.language) {

                Utils.Companion.Flag.USEBEST -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.use_best_flag))
                    if (item.second.installedVersion as Int == item.second.latestVersion as Int) {
                        // usebest plugin, but the correct one is now available
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_update_available_label)
                    } else {
                        // usebest plugin, still no updates
                        holder.itemVersion.text = holder.itemVersion.text.toString() + " \"IT\""
                        holder.latestVersion.visibility = View.INVISIBLE
                    }
                }

                Utils.Companion.Flag.CHINESE -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.chinese_flag))
                    if (item.second.installedVersion as Int > item.second.latestVersion as Int) {
                        // chinese plugin, newer than latest translated one (use best available)
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_usebest_available_label)

                    } else {
                        // chinese plugin, older than latest translated one
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_translation_available_label)
                    }
                }

                Utils.Companion.Flag.ITALIAN -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.italian_flag))
                    holder.itemVersion.text = holder.itemVersion.text.toString() + " IT"
                    holder.latestVersion.visibility = View.INVISIBLE

                }

                Utils.Companion.Flag.OTHER -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.old_flag))
                }

            }
        } else {
            holder.flag.visibility = View.INVISIBLE
        }

        ImageUtils.display(context, item.second.imgLink, holder.image)


        setupMiItemClickListener(holder.container, item)

    }

    private fun setupCompactViewHolder(holder: MiViewHolder, item: Pair<Int, MiItem>) {


        holder.item = item.second

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.container.clipToOutline = true
        }

        holder.latestVersion.visibility = View.VISIBLE
        holder.itemVersion.visibility = View.VISIBLE

        holder.itemName.text = item.second.itemName
        holder.latestVersion.text = context.resources.getString(R.string.miitem_latest_version_label) + item.second.latestVersion+ " IT"
        holder.itemVersion.text = context.resources.getString(R.string.miitem_installed_version_label) + item.second.installedVersion

        if (item.second.language != null) {
            when (item.second.language) {

                Utils.Companion.Flag.USEBEST -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.use_best_compact_flag))
                    if (item.second.installedVersion as Int == item.second.latestVersion as Int) {
                        // usebest plugin, but the correct one is now available
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_update_available_label)
                    } else {
                        // usebest plugin, still no updates
                        holder.itemVersion.text = holder.itemVersion.text.toString() + " \"IT\""
                        holder.latestVersion.visibility = View.INVISIBLE

                    }
                }

                Utils.Companion.Flag.CHINESE -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.chinese_compact_flag))
                    if (item.second.installedVersion as Int > item.second.latestVersion as Int) {
                        // chinese plugin, newer than latest translated one (use best available)
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_usebest_available_label)

                    } else {
                        // chinese plugin, older than latest translated one
                        holder.latestVersion.text = context.resources.getString(R.string.miitem_translation_available_label)
                    }
                }

                Utils.Companion.Flag.ITALIAN -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.italian_compact_flag))
                    holder.itemVersion.text = holder.itemVersion.text.toString() + " IT"
                    holder.latestVersion.visibility = View.INVISIBLE

                }

                Utils.Companion.Flag.OTHER -> {
                    holder.flag.setImageDrawable(context.resources.getDrawable(R.drawable.old_compact_flag))
                }

            }
        } else {
            holder.flag.visibility = View.INVISIBLE
        }

        ImageUtils.display(context, item.second.imgLink, holder.image)

        setupMiItemClickListener(holder.container, item)

    }



    private fun setupMiItemClickListener(container : View, item: Pair<Int, MiItem>) {

        container.setOnClickListener {

            if (item.second.language == Utils.Companion.Flag.CHINESE || item.second.language == Utils.Companion.Flag.OTHER) {
                //if installed plugin is not either chinese or unknown (too old)...

                if (item.second.installedVersion as Int <= item.second.latestVersion as Int) {
                    //...and installed version is less than latest translated version
                    // download corresponding translated plugin
                    AlertDialog.Builder(context).setMessage("Vuoi scaricare il plugin ${item.second.installedVersion} italiano?")
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                UpdatePluginTask(context, item.second, onPluginManagementListener).execute()
                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                } else {
                    //...and installed version is greater than latest translated plugin
                    //prompt to download latest plugin and rename it as installed plugin
                    AlertDialog.Builder(context).setMessage("Il plugin ${item.second.installedVersion} non e' stato ancora tradotto!\nVuoi scaricare e forzare l'utilizzo dell'ultimo tradotto disponibile (${item.second.latestVersion} IT)?")
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->

                                val xcapeDialog = AppCompatDialog(context)
                                xcapeDialog.setContentView(R.layout.mihome_dialog)
                                xcapeDialog.setCanceledOnTouchOutside(false)
                                xcapeDialog.setCancelable(false)


                                val window = xcapeDialog.getWindow()
                                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                                val dialogContainer : View? =   xcapeDialog.findViewById(R.id.mireplacer_dialog_container)
                                val positive : TextView? = xcapeDialog.findViewById(R.id.mireplacer_dialog_positive_button)
                                val negative : TextView? = xcapeDialog.findViewById(R.id.mireplacer_dialog_negative_button)
                                val title : TextView? = xcapeDialog.findViewById(R.id.mireplacer_dialog_title)
                                val body : TextView? = xcapeDialog.findViewById(R.id.mireplacer_dialog_body)

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    dialogContainer?.clipToOutline = true
                                }

                                title?.setText("xCape wants you!")
                                body?.setText("Prima di procedere, aiuta il processo di traduzione inviandoci il plugin ${item.second.installedVersion} cinese!")
                                positive?.setText("CI STO!")
                                negative?.setText("NO")

                                positive?.setOnClickListener {
                                    xcapeDialog.dismiss()

                                    UploadPluginWithFTPTask(context, item.second.installedVersion as Int, object : OnPluginManagementListener {
                                        override fun OnDownloadSuccess() {}
                                        override fun OnDownloadError() {}
                                        override fun OnDeleteSuccess() {}
                                        override fun OnDeleteError() {}

                                        override fun OnUploadSuccess() {
                                            AlertDialog.Builder(context).setMessage("Grazie per il tuo aiuto! \nOra puoi procedere alla sostituzione del plugin cinese col migliore disponibile")
                                                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                                                        UpdatePluginTask(context, item.second, onPluginManagementListener, item.second.latestVersion).execute()
                                                    })
                                                    .setCancelable(false)
                                                    .show()
                                        }

                                        override fun OnUploadError() {
                                            Toast.makeText(context, "Upload fallito!", Toast.LENGTH_SHORT).show()
                                        }

                                    }).execute()
                                }


                                negative?.setOnClickListener {
                                    xcapeDialog.dismiss()
                                    UpdatePluginTask(context, item.second, onPluginManagementListener, item.second.latestVersion).execute()

                                }

                                xcapeDialog.show()

                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                }

            } else if (item.second.language == Utils.Companion.Flag.ITALIAN) {
                AlertDialog.Builder(context).setMessage("Vuoi cancellare il plugin?")
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
            } else if (item.second.language == Utils.Companion.Flag.USEBEST) {

                if (item.second.installedVersion as Int > item.second.latestVersion as Int) {
                    //...and installed version is greater than latest translated version
                    // delete installed plugin
                    AlertDialog.Builder(context).setMessage("Vuoi cancellare il plugin?")
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
                } else {
                    //...and installed version is less or equal than latest translated plugin
                    // prompt to download correct (not use best) plugin

                    AlertDialog.Builder(context).setMessage("E' ora disponibile il plugin ${item.second.installedVersion} in italiano. \nVuoi scaricarlo?")
                            .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
                                UpdatePluginTask(context, item.second, onPluginManagementListener).execute()
                            })
                            .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener { dialog, which ->

                            })
                            .show()
                }

            } else {
                Toast.makeText(context, context.resources.getString(R.string.miitem_no_action_available), Toast.LENGTH_SHORT).show()
            }

        }
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
            container = view.findViewById(R.id.mireplacer_dialog_container)
            image = view.findViewById(R.id.mi_item_image)
            itemName = view.findViewById(R.id.mi_item_item_name)
            flag = view.findViewById(R.id.mi_item_flag)
            itemVersion = view.findViewById(R.id.mi_item_plugin_version)
            latestVersion = view.findViewById(R.id.mi_item_plugin_latest_version)
        }


    }

}
