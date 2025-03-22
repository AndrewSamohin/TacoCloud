package com.example.taco_cloud.web;

public interface Converter<S, T> {
    T convert(S source);
}
