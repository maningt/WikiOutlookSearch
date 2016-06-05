package com.manish.outlooksearch.networkclient;

import android.content.Context;


import com.manish.outlooksearch.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by manishdewan on 04/06/16.
 */
/**
 * Wrapper above Retrofit Callback to handle Retrofit Errors
 */
public abstract class ResponseListener<T> implements Callback<T> {
    private Context context;

    public ResponseListener(Context context) {
        this.context = context;
    }

    @Override
    public void success(T t, Response response) {
        //passing success scenario to UI
        onSuccess(t);
    }


    @Override
    public void failure(RetrofitError error) {
        //Handling Different error cases of retrofit error
        if (error.isNetworkError()) {
            if (error.getCause() instanceof SocketTimeoutException) {
                _onError(context.getString(R.string.timeout_connection_error));
                return;
            }
            if (error.getCause() instanceof UnknownHostException) {
                _onError(context.getString(R.string.no_internet_connection));
                return;
            }
        } else {
            _onError(context.getString(R.string.something_wrong_error));
            return;
        }
    }

    void _onError(String error) {
        //passing error scenario to UI
        onError(error);
    }

    public abstract void onSuccess(T result);

    public abstract void onError(String error);


}
