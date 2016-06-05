package com.manish.outlooksearch.model;

import java.util.LinkedHashMap;

/**
 * Created by manishdewan on 04/06/16.
 */
public class ResponseQuery {
    public LinkedHashMap<String, PageValue> getPages() {
        return pages;
    }

    public void setPages(LinkedHashMap<String, PageValue> pages) {
        this.pages = pages;
    }

    private LinkedHashMap<String, PageValue> pages;
}
