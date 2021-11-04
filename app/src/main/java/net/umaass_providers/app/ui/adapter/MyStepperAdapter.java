package net.umaass_providers.app.ui.adapter;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import net.umaass_providers.app.R;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.ui.FragmentStepHours;
import net.umaass_providers.app.ui.FragmentStepMap;
import net.umaass_providers.app.ui.FragmentStepService;
import net.umaass_providers.app.ui.FragmentStepStaff;
import net.umaass_providers.app.ui.FragmentStepWelcome;
import net.umaass_providers.app.utils.Utils;

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    private String[] title = {Utils.getString(R.string.welcome),
                              Utils.getString(R.string.location),
                              Utils.getString(R.string.hours),
                              Utils.getString(R.string.service)
                              //Utils.getString(R.string.staff)
     };

    private NewIndustry newIndustry = new NewIndustry();

    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    public NewIndustry getNewIndustry() {
        return newIndustry;
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                FragmentStepWelcome fragmentStepWelcome = new FragmentStepWelcome();
                fragmentStepWelcome.setNewIndustry(newIndustry);
                return fragmentStepWelcome;
            case 1:
                FragmentStepMap fragmentStepMap = new FragmentStepMap();
                fragmentStepMap.setNewIndustry(newIndustry);
                fragmentStepMap.setInStep(true);
                return fragmentStepMap;
            case 2:
                FragmentStepHours fragmentStepHours = new FragmentStepHours();
                fragmentStepHours.setNewIndustry(newIndustry);
                fragmentStepHours.setInStep(true);
                return fragmentStepHours;

            case 3:
                FragmentStepService fragmentStepService = new FragmentStepService();
                fragmentStepService.setInStep(true);
                return fragmentStepService;
            case 4:
                FragmentStepStaff fragmentStepStaff = new FragmentStepStaff();
                fragmentStepStaff.setInStep(true);
                return fragmentStepStaff;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(title[position]) //can be a CharSequence instead
                .create();
    }
}