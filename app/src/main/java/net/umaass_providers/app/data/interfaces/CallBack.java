package net.umaass_providers.app.data.interfaces;


import net.umaass_providers.app.data.remote.utils.RequestException;

public abstract class CallBack<T> implements CallBacks<T> {
    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onFail(RequestException e) {

    }

}
