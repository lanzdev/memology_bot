package com.lanzdev.utils;

public class Counter {

    private int current;

    public Counter( ) {
        current = 1;
    }

    public int increase( ) {
        return current++;
    }

    public int getCurrent( ) {
        return current;
    }
}
