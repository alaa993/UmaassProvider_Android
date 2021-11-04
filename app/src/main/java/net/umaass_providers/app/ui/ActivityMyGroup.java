package net.umaass_providers.app.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hbb20.CountryCodePicker;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.ModelIntroducer;
import net.umaass_providers.app.ui.adapter.AdapterMyGroup;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.utils.EndlessRecyclerOnScrollListener;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyGroup extends BaseActivity {

    RecyclerView rView;
    TextView txt_sum, text_count;
    ImageView addDoctor;
    private AdapterMyGroup adapterMyGroup;
    List<ModelIntroducer> modelIntroducers = new ArrayList<>();
    long sum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interoducer);
        readView();
        functionView();
        getData();
    }

    @Override
    public void readView() {
        super.readView();

        rView = findViewById(R.id.recyclerView);
        text_count = findViewById(R.id.text_count);
        txt_sum = findViewById(R.id.txt_sum);
        addDoctor = findViewById(R.id.addDouctor);

    }

    @Override
    public void functionView() {
        super.functionView();

        adapterMyGroup = new AdapterMyGroup(this);
        LinearLayoutManager doctorManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rView.setLayoutManager(doctorManager);
        rView.setAdapter(adapterMyGroup);
        adapterMyGroup.setListener(item -> {

            if (item.getStaff().size()>0){
                dialogDeleteMember(item.getStaff().get(0).getUser_id());
            }
        });

        rView.addOnScrollListener(new EndlessRecyclerOnScrollListener(doctorManager) {
            @Override
            public void onLoadMore(int currentPage) {

            }
        });

        addDoctor.setOnClickListener(v -> {
          dialogAddDoctor();
        });
    }

    private void getData() {
        showLoading();
        Repository.getInstance().getListIntroducer(new CallBack<Api<List<ModelIntroducer>>>() {
            @Override
            public void onSuccess(Api<List<ModelIntroducer>> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                adapterMyGroup.clearAndPut(listApi.getData());
                for (ModelIntroducer modelIntroducer : listApi.getData()) {
                    sum = sum + modelIntroducer.getIncome();
                }
                int count = listApi.getData().size();
                text_count.setText(getResources().getString(R.string.count) +": "+ count);
                txt_sum.setText(getResources().getString(R.string.sum_price) +": "+ sum);

            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });


    }

    private void dialogAddDoctor() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_add_doctor, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
         alertDialogBuilder.setView(promptsView);
        final EditText phoneNumberInput =  promptsView.findViewById(R.id.et_phonenumber);
        CountryCodePicker countryCodePicker = promptsView.findViewById(R.id.countryCodePicker);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.add), (dialog, id) -> {
                    String phoneNumber = "+"+countryCodePicker.getSelectedCountryCode()+phoneNumberInput.getText().toString();
                    add(phoneNumber);
                }).setNegativeButton(getString(R.string.cancel), (dialog, id) ->

                dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void add(String phoneNumber) {

        for (ModelIntroducer item : modelIntroducers) {
            if (item.getStaff().get(0).getUser().getPhone().equals(phoneNumber)) {
                Toast.makeText(this, getString(R.string.doctor_subset), Toast.LENGTH_SHORT).show();
            return;
            }
        }
        showLoading();

        Repository.getInstance().addDoctorToGroup(phoneNumber, new CallBack<Api>() {
            @Override
            public void onSuccess(Api api) {
                super.onSuccess(api);
                hideLoading();
                if (api.getStatus()==406){
                    Toast.makeText(ActivityMyGroup.this, getString(R.string.another_introducer), Toast.LENGTH_SHORT).show();
                }else if (api.getStatus() == 422 ){
                    Toast.makeText(ActivityMyGroup.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ActivityMyGroup.this, getString(R.string.request_successfully), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
              if ( e.getCode() == 406){
                  Toast.makeText(ActivityMyGroup.this, getString(R.string.another_introducer), Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(ActivityMyGroup.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
              }

            }
        });

    }

    private void dialogDeleteMember(int idDactor) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.remove_person_subcategory));
        builder.setPositiveButton(getString(R.string.delete), (dialog, which) ->
                delete(String.valueOf(idDactor)));
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete(String userId) {
        showLoading();
        Repository.getInstance().deleteDoctorGroup(userId, new CallBack<Api>() {
            @Override
            public void onSuccess(Api api) {
                super.onSuccess(api);
                hideLoading();

               if (api.getStatus() == 422 ){
                    Toast.makeText(ActivityMyGroup.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    return;
                }
                 getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                getData();

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
}
