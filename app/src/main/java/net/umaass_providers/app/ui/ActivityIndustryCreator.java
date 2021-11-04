package net.umaass_providers.app.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.umaass_providers.app.R;
import net.umaass_providers.app.ui.adapter.MyStepperAdapter;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.utils.Utils;

public class ActivityIndustryCreator extends BaseActivity {

    StepperLayout stepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        readView();
        functionView();
    }

    @Override
    public void readView() {
        super.readView();
        stepperLayout = findViewById(R.id.stepperLayout);
    }

    @Override
    public void functionView() {
        super.functionView();
        MyStepperAdapter adapter = new MyStepperAdapter(getSupportFragmentManager(), this);
        stepperLayout.setAdapter(adapter);
        stepperLayout.setOffscreenPageLimit(adapter.getCount());
        stepperLayout.setCompleteButtonColor(Utils.getColor(R.color.colorAccent));
        stepperLayout.setListener(new StepperLayout.StepperListener() {
            @Override
            public void onCompleted(View completeButton) {
                Intent intent = new Intent(ActivityIndustryCreator.this, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(VerificationError verificationError) {

            }

            @Override
            public void onStepSelected(int newStepPosition) {

            }

            @Override
            public void onReturn() {

            }
        });
    }


}
