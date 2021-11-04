package net.umaass_providers.app.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.viewModel.impl.SplashView;
import net.umaass_providers.app.viewModel.persenter.SplashPresenter;


public class ViewModelSplash extends AndroidViewModel implements SplashPresenter {

    private Repository repository;
    private SplashView splashView;

    public ViewModelSplash(
            @NonNull
                    Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    @Override
    public void setSplashView(SplashView presenter) {
        this.splashView = presenter;
    }

    @Override
    public void getToken(String user, String pass, String deviceCode) {
        if (splashView != null) {
            splashView.openLoading();
        }
    }
}


