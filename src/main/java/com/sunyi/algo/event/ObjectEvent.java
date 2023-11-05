package com.sunyi.algo.event;

/**
 * @author SunYi
 */
public class ObjectEvent<T> {
    T val;

    public void clear() {
        val = null;
    }
}
