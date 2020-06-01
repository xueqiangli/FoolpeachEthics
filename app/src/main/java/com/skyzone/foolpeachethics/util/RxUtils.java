package com.skyzone.foolpeachethics.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Skyzone on 2/17/2017.
 */

public class RxUtils {

    public static <T> Observable.Transformer<T, T> normalSchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
