package org.loriz.mihomereplacer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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


    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_splashscreen)



        ImageUtils.setup(this)

        ImageUtils.configBuilder = ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(3)
                //.imageDownloader(UserAgentImageDownloader(this))
                .imageDownloader(BaseImageDownloader(this))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(UsingFreqLimitedMemoryCache(2000000))
                .diskCache(UnlimitedDiskCache(StorageUtils.getCacheDirectory(this)))
                .diskCacheFileNameGenerator(HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

        ImageUtils.displayOptionsBuilder
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .showImageOnFail(R.drawable.placeholder_newsfeed)
        //.showImageForEmptyUri(R.drawable.mi_home_stock)
//                .showImageOnLoading(R.drawable.placeholder_newsfeed)
        //.displayer(new FadeInBitmapDisplayer(200))
        //.showImageOnLoading(R.drawable.image_ph)


        ImageUtils.build()



        Handler().postDelayed({

            object : UpdateAllTask() {

                override fun onPostExecute(result: Elements?) {

                    splashscreen_text.text = baseContext.resources.getString(R.string.updating)

                    super.onPostExecute(result)

                    if (Constants.MI_ITEMS.isEmpty()) {
                        finish()
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