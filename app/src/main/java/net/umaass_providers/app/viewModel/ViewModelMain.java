package net.umaass_providers.app.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.viewModel.impl.MainView;


public class ViewModelMain extends AndroidViewModel {
    private Repository repository;
    private MainView   presenter;
    public ViewModelMain(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }
}
