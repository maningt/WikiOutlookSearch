package com.manish.outlooksearch.model;

/**
 * Created by manishdewan on 04/06/16.
 */
public class ThumbNail {
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private String source;
    private int width;
    private int height;
}
