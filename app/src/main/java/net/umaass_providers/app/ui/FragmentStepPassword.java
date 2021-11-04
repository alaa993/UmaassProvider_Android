package net.umaass_providers.app.ui;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.IndustryResult;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.utils.Utils;

public class FragmentStepPassword extends BaseFragment implements BlockingStep {

    private Toolbar toolbar;
    private EditText edtAdminPass;
    private EditText edtCoworkerPass;
    private EditText edtAssistantPass;
    private boolean inStep = false;

    private NewIndustry newIndustry;

    public void setNewIndustry(NewIndustry newIndustry) {
        this.newIndustry = newIndustry;
    }

    public void setInStep(boolean inStep) {
        this.inStep = inStep;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_password;
    }

    @Override
    public void readView() {
        super.readView();
        toolbar = baseView.findViewById(R.id.toolbar);
        edtAdminPass = baseView.findViewById(R.id.edtAdminPass);
        edtCoworkerPass = baseView.findViewById(R.id.edtCoworkerPass);
        edtAssistantPass = baseView.findViewById(R.id.edtAssistantPass);
    }

    @Override
    public void functionView() {
        super.functionView();
        toolbar.setVisibility(inStep ? View.GONE : View.VISIBLE);
    }


    StepperLayout.OnNextClickedCallback callback;

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        this.callback = callback;
        createIndustry();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        VerificationError verificationError = null;
        boolean b = Utils.isEmptyEditText(edtAdminPass, edtCoworkerPass, edtAssistantPass);
        if (b) {
            verificationError = new VerificationError(Utils.getString(R.string.not_empty));
        } else {
            if (newIndustry != null) {
                newIndustry.admin_password = edtAdminPass.getText().toString();
                newIndustry.coworker_password = edtCoworkerPass.getText().toString();
                newIndustry.assistant_password = edtAssistantPass.getText().toString();
            }
        }
        return verificationError;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void createIndustry() {
        showLoading();
        Repository.getInstance().createIndustry(newIndustry, new CallBack<Api<IndustryResult>>() {
            @Override
            public void onSuccess(Api<IndustryResult> industryResultApi) {
                super.onSuccess(industryResultApi);
                hideLoading();
                if (industryResultApi.getData() != null) {
                    Preference.setActiveIndustryId(industryResultApi.getData().getId() + "");
                    Preference.setValidLogin(true);
                    if (callback != null) {
                        callback.goToNextStep();
                    }
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.try_again));
            }
        });
    }
}
