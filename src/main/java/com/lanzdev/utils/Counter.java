package com.lanzdev.utils;

public class Counter {

    private int current;

    public Counter(int beginning) {
        current = beginning;
    }

    public int increase( ) {
        return current++;
    }

    public int getCurrent( ) {
        return current;
    }
}
