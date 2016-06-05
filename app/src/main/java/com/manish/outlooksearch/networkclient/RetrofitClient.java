package com.manish.outlooksearch.networkclient;

import com.manish.outlooksearch.AppBaseApplication;
import com.manish.outlooksearch.utils.Constants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

/**
 * Created by manishdewan on 04/06/16.
 */

/**
 * Class created to create Retrofit builder for each api call
 */
public class RetrofitClient {

    private static RetrofitClient retrofitClient;

    private RetrofitClient() {
        //private constructor
    }

    /**
     * singleton instance for client
     */
    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    /**
     * Retrofit Builder Initialized
     */
    private RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(Constants.BASE_URL)
            .setLog(new AndroidLog("API_CALL"))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(getOkClient());

    /**
     * Retrofit request created
     */
    public <S> S createRequest(Class<S> apiService) {
        RestAdapter adapter = builder.build();
        return adapter.create(apiService);
    }


    /**
     * returns Okhttp client
     */
    public static OkClient getOkClient() {
        OkHttpClient okHttpClient = AppBaseApplication.getClient();
        OkClient okClient = new OkClient(okHttpClient);
        return okClient;
    }
}
