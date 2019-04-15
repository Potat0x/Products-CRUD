package com.example.demo.domain;

public class Image {
    private final String url;

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        return url != null ? url.equals(image.url) : image.url == null;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
