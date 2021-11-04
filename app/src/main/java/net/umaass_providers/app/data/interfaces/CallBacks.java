package net.umaass_providers.app.data.interfaces;


import net.umaass_providers.app.data.remote.utils.RequestException;

public interface CallBacks<T> {
    void onSuccess(T t);

    void onFail(RequestException e);
}