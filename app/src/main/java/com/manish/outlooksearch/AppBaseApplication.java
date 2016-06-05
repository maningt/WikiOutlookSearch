package com.manish.outlooksearch;

import android.app.Application;
import android.os.SystemClock;

import com.manish.outlooksearch.utils.Constants;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by manishdewan on 04/06/16.
 */
public class AppBaseApplication extends Application {
    private static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //Delay added for showing splash first time
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(Constants.DELAY_SPLASH));
        setOkHttpClient();
    }

    /**
     * Okhttp Client created when application is initialized
     */
    private void setOkHttpClient() {
        okHttpClient = new OkHttpClient();
        Cache cache = new Cache(getApplicationContext().getCacheDir(), Constants.CACHE_SIZE);
        okHttpClient.setCache(cache);
        okHttpClient.setConnectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(Constants.TIME_OUT, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(Constants.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * returns the Okhttp client
     */
    public static OkHttpClient getClient() {
        return okHttpClient;
    }
}
