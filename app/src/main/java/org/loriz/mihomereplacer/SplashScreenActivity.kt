package org.loriz.mihomereplacer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import com.nostra13.universalimageloader.utils.StorageUtils
import kotlinx.android.synthetic.main.activity_splashscreen.*
import org.jsoup.select.Elements
import org.loriz.mihomereplacer.core.Constants
import org.loriz.mihomereplacer.update.UpdateAllTask
import org.loriz.mihomereplacer.utils.ImageUtils
import org.loriz.mihomereplacer.utils.Utils


/**
 * Created by loriz on 1/24/18.
 */


class SplashScreenActivity : AppCompatActivity() {

    private val WRITE_EXT: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_splashscreen)

        setupImageHandling()

        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),  WRITE_EXT)
        } else {
            updateAndStart()
        }


        super.onCreate(savedInstanceState)

    }



    private fun updateAndStart() {

        Handler().postDelayed({

            object : UpdateAllTask(this) {

                override fun onPostExecute(result: Elements?) {

                    splashscreen_text.text = baseContext.resources.getString(R.string.updating)

                    super.onPostExecute(result)

                    if (Constants.MI_ITEMS.isEmpty()) {
                        this@SplashScreenActivity.runOnUiThread { Utils.showFatalErrorDialog(this@SplashScreenActivity, resources.getString(R.string.error_network_text)) }
                    } else {
                        this@SplashScreenActivity.startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                    }

                }

            }.execute()

        }, 2000)

    }



    private fun setupImageHandling() {
        ImageUtils.setup(this)

        ImageUtils.configBuilder = ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(3)
                .imageDownloader(BaseImageDownloader(this))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(UsingFreqLimitedMemoryCache(2000000))
                .diskCache(UnlimitedDiskCache(StorageUtils.getCacheDirectory(this)))
                .diskCacheFileNameGenerator(HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

        ImageUtils.displayOptionsBuilder
                .cacheInMemory(true)
                .cacheOnDisk(true)

        ImageUtils.build()

    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    updateAndStart()
                } else {
                    this@SplashScreenActivity.runOnUiThread { Utils.showFatalErrorDialog(this@SplashScreenActivity, resources.getString(R.string.error_permission_text)) }

                }
                return
            }

            else -> { }
        }
    }



}