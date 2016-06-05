package com.manish.outlooksearch.model;

/**
 * Created by manishdewan on 04/06/16.
 */
public class SearchSuggestionResponse {
    public ResponseQuery getQuery() {
        return query;
    }

    public void setQuery(ResponseQuery query) {
        this.query = query;
    }

    private ResponseQuery query;
}
