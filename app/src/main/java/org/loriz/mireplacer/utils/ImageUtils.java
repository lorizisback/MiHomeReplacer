package org.loriz.mireplacer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public final class ImageUtils {

    public static DisplayImageOptions displayOptions;
    public static ImageLoaderConfiguration config;

    public static ImageLoaderConfiguration.Builder configBuilder;
    public static DisplayImageOptions.Builder displayOptionsBuilder;
    private static ImageLoader mInstance;

    private ImageUtils(){}

    public static void setup(Context context)
    {
        mInstance = ImageLoader.getInstance();
        displayOptions= null;
        config = null;

        File cacheDir = StorageUtils.getCacheDirectory(context);
        configBuilder = new ImageLoaderConfiguration.Builder(context)
            .threadPoolSize(3)
            .threadPriority(Thread.NORM_PRIORITY - 2) // default
            .tasksProcessingOrder(QueueProcessingType.FIFO) // default
            .denyCacheImageMultipleSizesInMemory()

            //.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
            //.memoryCacheSize(2 * 1024 * 1024)
            //.memoryCacheSizePercentage(13) // default

            .memoryCache(new UsingFreqLimitedMemoryCache(2000000))
            //.memoryCacheSize(1500000) // 1.5 Mb

            .denyCacheImageMultipleSizesInMemory()

            .diskCache(new UnlimitedDiskCache(cacheDir)) // default
            //.diskCacheSize(50 * 1024 * 1024)
            //.diskCacheFileCount(100)

            .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()); // default
            //.writeDebugLogs()


        displayOptionsBuilder = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheInMemory(false)
            .cacheOnDisk(true)
            //.imageScaleType(ImageScaleType.EXACTLY)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888); // default
    }

    public static void build() {
        config = configBuilder.build();
        displayOptions = displayOptionsBuilder.build();

        mInstance.init(config);
    }

    public static void loadImage(String url) {
        mInstance.loadImageSync(url, displayOptions);
    }

    public static void display(Context context, String url, ImageView view) {
        mInstance.displayImage(url, view, displayOptions);
    }

    public static void display(Context context, String url, ImageView view, DisplayImageOptions options  ) {
        mInstance.displayImage(url, view, options);
    }

    public static void display(Context context, String url, ImageView view, ImageLoadingListener listener) {
        mInstance.displayImage(url, view, displayOptions, listener);
    }

    public static void display(Context context, String url, ImageView view, ImageLoadingListener listener, DisplayImageOptions options ) {
        mInstance.displayImage(url, view, options, listener);
    }

    public static void displayThumb(Context context, String url, ImageView view) {
        mInstance.displayImage(url, view, displayOptions);
    }

    public static void displayThumb(Context context, String url, ImageView view, ImageLoadingListener listener) {
        mInstance.displayImage(url, view, displayOptions, listener);
    }

    public static ImageLoader getInstance() {
        return mInstance;
    }
}
