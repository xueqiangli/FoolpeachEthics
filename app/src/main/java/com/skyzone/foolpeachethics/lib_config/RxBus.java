package com.skyzone.foolpeachethics.lib_config;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * to instead of EventBus
 * <p>
 * Created by Skyzone on 12/21/2016.
 */

public enum RxBus {

    Instance;

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return bus;
    }

    public <T> Observable<T> toObserverable(Class<T> t) {
        return bus.ofType(t);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
