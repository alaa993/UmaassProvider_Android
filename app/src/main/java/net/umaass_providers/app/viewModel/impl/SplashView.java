package net.umaass_providers.app.viewModel.impl;


public interface SplashView extends BaseView {
    void onNewVersion(String url);

    void onErrorNetwork(String message);

    void onNextStep();
}
