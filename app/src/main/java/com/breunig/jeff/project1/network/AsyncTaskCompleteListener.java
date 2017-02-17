package com.breunig.jeff.project1.network;

/**
 * Created by jkbreunig on 2/17/17.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
