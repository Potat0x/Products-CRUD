package com.example.demo.domain;

import com.example.demo.domain.exceptions.InvalidDtoException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class ImageDto {
    private final String url;

    @JsonCreator
    public ImageDto(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    static void assertValid(ImageDto image) {
        if (image == null) {
            throw new InvalidDtoException("image is required");
        }

        if (Strings.isNullOrEmpty(image.getUrl())) {
            throw new InvalidDtoException("image.url cannot be empty");
        }

        try {
            new URL(image.getUrl());
        } catch (MalformedURLException e) {
            throw new InvalidDtoException("invalid image.url");
        }
    }

    @Override
    public String toString() {
        return "ImageDto{" +
                "url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDto imageDto = (ImageDto) o;
        return Objects.equals(url, imageDto.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
