package net.umaass_providers.app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.SuggestModel;
import net.umaass_providers.app.data.remote.models.Suggestion;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySuggestion extends BaseActivity {

    private EditText name, phonenumber, country, edt_city,edt_specialty,ed_introducer_code;
    private RoundCornerButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sug);
        readView();
        functionView();

    }

    @Override
    public void readView() {
        super.readView();

        name = findViewById(R.id.edtFirstName);
        phonenumber = findViewById(R.id.edt_phonenumber);
        country = findViewById(R.id.edt_country);
        edt_city = findViewById(R.id.edt_city);
        edt_specialty = findViewById(R.id.edt_specialty);
        btnDone = findViewById(R.id.btnDone);
        ed_introducer_code = findViewById(R.id.ed_introducer_code);

    }

    @Override
    public void functionView() {
        super.functionView();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             submit();

            }
        });
    }

    private void submit(){
        boolean b = check(name, phonenumber,edt_specialty);
        String introducerCode = ed_introducer_code.getText().toString();
        if (b) {
            return;
        }
        SuggestModel model = new SuggestModel(name.getText().toString(), phonenumber.getText().toString(),country.getText().toString(),
                edt_city.getText().toString(), edt_specialty.getText().toString());
        showLoading();
        Repository.getInstance().suggestDoctor(model, new CallBack<DefualtResponse>() {
            @Override
            public void onSuccess(DefualtResponse defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                Toast.makeText(ActivitySuggestion.this, defualtResponseApi.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
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

    ProgressDialog progressDialog;

    public void showLoading() {
        progressDialog = new ProgressDialog(ActivityUtils.getTopActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pls_wait));
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
