package net.umaass_providers.app.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.IndustryResult;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.UpdateIndustry;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

public class ActivityEditIndustry extends BaseActivity {

    EditText edtTitle;
    EditText edtPhone;
    EditText edtAddress;
    EditText edtBio;
    RoundCornerButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_industry);
        readView();
        functionView();
        initViewModel();
    }

    @Override
    public void readView() {
        super.readView();
        edtTitle = findViewById(R.id.edtTitle);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtBio = findViewById(R.id.edtBio);
        btnDone = findViewById(R.id.btnDone);
    }

    @Override
    public void functionView() {
        super.functionView();

        edtPhone.setVisibility(View.GONE);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        getData();
    }

    ShowIndustry industry;

    private void getData() {
        showLoading();
        Repository.getInstance().showIndustry(Preference.getActiveIndustryId(), new CallBack<Api<ShowIndustry>>() {
            @Override
            public void onSuccess(Api<ShowIndustry> showIndustryApi) {
                super.onSuccess(showIndustryApi);
                hideLoading();
                industry = showIndustryApi.getData();
                edtAddress.setText(industry.getAddress());
                edtBio.setText(industry.getDescription());
                edtPhone.setText(industry.getPhone());
                edtTitle.setText(industry.getTitle());
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }


    private void update() {
        if (industry == null) {
            return;
        }
        boolean b = check(edtTitle, edtAddress);
        if (b) {
            return;
        }
        UpdateIndustry updateIndustry = new UpdateIndustry();
        updateIndustry.industry_id = industry.getId() + "";
        updateIndustry.title = edtTitle.getText().toString();
       // updateIndustry.phone = edtPhone.getText().toString();
        updateIndustry.address = edtAddress.getText().toString();
        updateIndustry.description = edtBio.getText().toString();

        showLoading();
        Repository.getInstance().updateIndustry(updateIndustry, new CallBack<Api<IndustryResult>>() {
            @Override
            public void onSuccess(Api<IndustryResult> industryResultApi) {
                super.onSuccess(industryResultApi);
                hideLoading();
                G.changeIndustry = true;
                G.toast(Utils.getString(R.string.saved));
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

    ProgressDialog progressDialog;

    private void showLoading() {
        progressDialog = new ProgressDialog(ActivityUtils.getTopActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pls_wait));
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private boolean check(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText() != null) {
                if (editText.getText().length() == 0) {
                    editText.setError(Utils.getString(R.string.not_empty));
                    return true;
                }
            }
        }
        return false;
    }

}
