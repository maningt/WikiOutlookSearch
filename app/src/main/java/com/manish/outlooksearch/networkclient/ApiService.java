package com.manish.outlooksearch.networkclient;

import com.manish.outlooksearch.model.SearchSuggestionResponse;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by manishdewan on 04/06/16.
 */
/**
 * Interface which has signature for each retrofit network request
 */
public interface ApiService {

    /**
     * signature for getting search suggestions api
     */
    @GET("/")
    void getSearchSuggestions(@Query("action") String action,
                              @Query("prop") String prop,
                              @Query("format") String format,
                              @Query("piprop") String piprop,
                              @Query("pithumbsize") String pithumbsize,
                              @Query("pilimit") String pilimit,
                              @Query("generator") String generator,
                              @Query("gpssearch") String searchTerm,
                              @Query("gpsoffset") String gpsoffset,
                              Callback<SearchSuggestionResponse> searchSuggestionResponseCallback);
}
