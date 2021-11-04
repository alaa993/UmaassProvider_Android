package net.umaass_providers.app.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.ServiceResult;
import net.umaass_providers.app.data.remote.models.ServicesItem;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.ui.adapter.AdapterService;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.List;

public class FragmentStepService extends BaseFragment implements BlockingStep {

    private AdapterService adapterService;
    private RecyclerView recyclerView;
    private EditText edtName;
    private EditText edtPrice;
    private EditText edtTime;

    private EditText edtTypeTime;
    private EditText edtTypeprice;

    private EditText edtDesc;
    private TextView btnCancel;
    private RoundCornerButton btnAddNew;

    private ServicesItem editItem;

    private Toolbar toolbar;
    private boolean inStep = false;

    public static FragmentStepService newInstance() {
        Bundle args = new Bundle();
        FragmentStepService fragment = new FragmentStepService();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInStep(boolean inStep) {
        this.inStep = inStep;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_service;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
        toolbar = baseView.findViewById(R.id.toolbar);
        edtName = baseView.findViewById(R.id.edtName);
        edtPrice = baseView.findViewById(R.id.edtPrice);
        edtTime = baseView.findViewById(R.id.edtTime);
        edtTypeTime = baseView.findViewById(R.id.TypeTime);
        edtTypeprice = baseView.findViewById(R.id.TypePrice);
        edtDesc = baseView.findViewById(R.id.edtDesc);
        btnAddNew = baseView.findViewById(R.id.btnAddNew);
        btnCancel = baseView.findViewById(R.id.btnCancel);
    }

    @Override
    public void functionView() {
        super.functionView();
        toolbar.setVisibility(inStep ? View.GONE : View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        adapterService = new AdapterService();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterService);
        adapterService.setDeleteListener(new ItemClickListener<ServicesItem>() {
            @Override
            public void onClick(ServicesItem item) {
                dialog(item);
            }
        });
        adapterService.setEditListener(new ItemClickListener<ServicesItem>() {
            @Override
            public void onClick(ServicesItem item) {
                editItem = item;
                btnAddNew.setText(Utils.getString(R.string.update_service));
                btnCancel.setVisibility(View.VISIBLE);
                edtName.setText(item.getTitle());
                edtPrice.setText(String.valueOf(item.getPrice()));
                edtTime.setText(String.valueOf(item.getDuration()));
                edtDesc.setText(item.getNotesForTheCustomer());
                edtTypeTime.setText(String.valueOf(item.getTypeTime()));
                edtTypeprice.setText(String.valueOf(item.getTypePrice()));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem = null;
                btnCancel.setVisibility(View.GONE);
                edtName.setText("");
                edtPrice.setText("");
                edtTime.setText("");
                edtDesc.setText("");
                btnAddNew.setText(Utils.getString(R.string.add_service));
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editItem == null) {
                    addNew();
                } else {
                    editItem();
                }
            }
        });
        if (!inStep) {
            getData();
        }
    }

    private ShowIndustry industry;

    private void getData() {
        showLoading();
        Repository.getInstance().showIndustry(Preference.getActiveIndustryId(), new CallBack<Api<ShowIndustry>>() {
            @Override
            public void onSuccess(Api<ShowIndustry> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                if (listApi != null) {
                    if (listApi.getData() != null) {
                        industry = listApi.getData();
                        adapterService.clearAndPut(industry.getServices());
                        Log.i("ttttllllllll", industry.getServices().toString());
                        YoYo.with(Techniques.SlideInRight)
                            .duration(500)
                            .playOn(recyclerView);
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

    private void addNew() {
        if (industry == null) {
            return;
        }
        boolean b = Utils.isEmptyEditText(edtName, edtPrice, edtTime);
        if (b) {
            return;
        }
        String title = edtName.getText().toString();
        String price = edtPrice.getText().toString();
        String time = edtTime.getText().toString();
        String desc = edtDesc.getText().toString();
        String Typeprice = edtTypeprice.getText().toString();
        String TypeTime = edtTypeTime.getText().toString();
        showLoading();
        Repository.getInstance().createService(industry.getId() + "", title, time, price, desc,TypeTime,Typeprice,
                                               new CallBack<Api<List<ServiceResult>>>() {
                                                   @Override
                                                   public void onSuccess(Api<List<ServiceResult>> defualtResponseApi) {
                                                       super.onSuccess(defualtResponseApi);
                                                       hideLoading();
                                                       getData();
                                                       edtName.setText("");
                                                       edtPrice.setText("");
                                                       edtTime.setText("");
                                                       edtDesc.setText("");
                                                       edtTypeTime.setText("");
                                                       edtTypeprice.setText("");
                                                   }

                                                   @Override
                                                   public void onFail(RequestException e) {
                                                       super.onFail(e);
                                                       hideLoading();
                                                       G.toast(Utils.getString(R.string.try_again));
                                                   }
                                               });
    }

    private void editItem() {
        if (editItem == null) {
            return;
        }
        boolean b = Utils.isEmptyEditText(edtName, edtPrice, edtTime);
        if (b) {
            return;
        }
        String title = edtName.getText().toString();
        String price = edtPrice.getText().toString();
        String time = edtTime.getText().toString();
        String desc = edtDesc.getText().toString();
        String Typeprice = edtTypeprice.getText().toString();
        String TypeTime = edtTypeTime.getText().toString();
        showLoading();
        Repository.getInstance().updateService(editItem.getId() + "", title, time, price, desc,TypeTime,Typeprice, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                getData();
                edtName.setText("");
                edtPrice.setText("");
                edtTime.setText("");
                edtDesc.setText("");
                edtTypeTime.setText("");
                edtTypeprice.setText("");
                btnAddNew.setText(Utils.getString(R.string.add_service));
                G.toast(Utils.getString(R.string.saved));
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.try_again));
            }
        });

    }

    private void dialog(ServicesItem servicesItem) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder1.setMessage(Utils.getString(R.string.are_you_sure_for_delete));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                Utils.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deleteStaff(servicesItem);
                    }
                });

        builder1.setNegativeButton(
                Utils.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteStaff(ServicesItem servicesItem) {
        showLoading();
        Repository.getInstance().deleteService(String.valueOf(servicesItem.getId()), new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                adapterService.removeItem(servicesItem);
                G.toast(Utils.getString(R.string.deleted));
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.try_again));
            }
        });
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
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
        return null;
    }

    @Override
    public void onSelected() {
        getData();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
