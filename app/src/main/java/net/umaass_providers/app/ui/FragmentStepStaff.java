package net.umaass_providers.app.ui;

import android.content.DialogInterface;
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
import com.google.gson.Gson;
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
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.models.StaffItem;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.models.CommonItem;
import net.umaass_providers.app.ui.adapter.AdapterStaff;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.ui.dialog.DialogList;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentStepStaff extends BaseFragment implements BlockingStep {

    private AdapterStaff adapterStaff;
    private RecyclerView recyclerView;
    private RoundCornerButton btnAddNew;
    private EditText edtPermission;
    private EditText edtPhone;
    private EditText edtRoll;
    private EditText edtUserName;
    private TextView btnCancel;

    private Toolbar toolbar;
    private boolean inStep = false;

    private StaffItem editItem;

    public void setInStep(boolean inStep) {
        this.inStep = inStep;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_staff;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
        toolbar = baseView.findViewById(R.id.toolbar);
        btnAddNew = baseView.findViewById(R.id.btnAddNew);
        edtPermission = baseView.findViewById(R.id.edtPermission);
        edtPhone = baseView.findViewById(R.id.edtPhone);
        edtRoll = baseView.findViewById(R.id.edtRoll);
        edtUserName = baseView.findViewById(R.id.edtUserName);
        btnCancel = baseView.findViewById(R.id.btnCancel);
    }

    @Override
    public void functionView() {
        super.functionView();
        toolbar.setVisibility(inStep ? View.GONE : View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        adapterStaff = new AdapterStaff();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterStaff);
        adapterStaff.setDeleteListener(new ItemClickListener<StaffItem>() {
            @Override
            public void onClick(StaffItem item) {
                dialog(item);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem = null;
                btnCancel.setVisibility(View.GONE);
                edtPermission.setText("");
                edtPhone.setText("");
                edtRoll.setText("");
                edtUserName.setText("");
                btnAddNew.setText(Utils.getString(R.string.add_staff));
                edtUserName.setEnabled(true);
                edtPhone.setEnabled(true);
            }
        });
        adapterStaff.setEditListener(new ItemClickListener<StaffItem>() {
            @Override
            public void onClick(StaffItem item) {
                editItem = item;
                btnAddNew.setText(Utils.getString(R.string.update_staff));
                btnCancel.setVisibility(View.VISIBLE);
                edtPermission.setText(item.getEmail());
                edtPhone.setText(item.getPhone());
                edtRoll.setText(item.getRole());
                edtUserName.setText(item.getName());
                edtUserName.setEnabled(false);
                edtPhone.setEnabled(false);
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editItem == null) {
                    createStaff();
                } else {
                    updateStaff();
                }
            }
        });
        edtRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseRoll();
            }
        });
        edtPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePermission();
            }
        });
        if (!inStep) {
            getData();
        }

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


    private void chooseRoll() {
        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem(null, "admin"));
        list.add(new CommonItem(null, "coworker"));
        list.add(new CommonItem(null, "assistant"));
        DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
        dialogList.setTitle(Utils.getString(R.string.choose_roll));
        dialogList.clearAndPut(list);
        dialogList.setListener(new ItemClickListener<ListItem>() {
            @Override
            public void onClick(ListItem item) {
                edtRoll.setError(null);
                edtRoll.setText(item.getItemName());
            }
        });
        dialogList.show();
    }

    private List<Integer> selectedPermision = new ArrayList<>();

    private void choosePermission() {
        if (industry == null) {
            return;
        }
        DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
        dialogList.setTitle(Utils.getString(R.string.choose_roll));
        dialogList.setMultiSelect(true);
        dialogList.setMultiSelectListener(new ItemClickListener<List<ListItem>>() {
            @Override
            public void onClick(List<ListItem> item) {
                StringBuilder s = new StringBuilder();
                selectedPermision.clear();
                for (int i = 0; i < item.size(); i++) {
                    ListItem listItem = item.get(i);
                    if (i > 0) {
                        s.append(", ");
                    }
                    selectedPermision.add(Integer.parseInt(listItem.getItemId()));
                    s.append(listItem.getItemName());
                }
                edtPermission.setError(null);
                edtPermission.setText(s.toString());
            }
        });
        dialogList.clearAndPut(industry.getServices());
        dialogList.show();
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
                        adapterStaff.clearAndPut(listApi.getData().getStaff());
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

    private Gson gson = new Gson();

    private void createStaff() {
        if (industry == null || selectedPermision == null) {
            return;
        }
        boolean b = Utils.isEmptyEditText(edtUserName, edtRoll, edtPhone, edtPermission);
        if (b) {
            return;
        }
        String name = edtUserName.getText().toString();
        String phone = edtPhone.getText().toString();
        String role = edtRoll.getText().toString();
        String email = "";
        showLoading();
        Repository.getInstance().createStaff(String.valueOf(industry.getId()), name, phone, email, role, selectedPermision, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                G.toast(Utils.getString(R.string.saved));
                selectedPermision.clear();
                edtUserName.setText("");
                edtPhone.setText("");
                edtRoll.setText("");
                edtPermission.setText("");
                btnAddNew.setText(Utils.getString(R.string.add_staff));
                getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.try_again));
            }
        });
    }


    private void updateStaff() {
        boolean b = Utils.isEmptyEditText(edtUserName, edtRoll, edtPhone, edtPermission);
        if (b) {
            return;
        }
        if (selectedPermision == null || selectedPermision.size() == 0) {
            return;
        }
        String name = edtUserName.getText().toString();
        String phone = edtPhone.getText().toString();
        String role = edtRoll.getText().toString();
        String email = "";
        showLoading();
        Repository.getInstance().updateStaff(String.valueOf(editItem.getId()), name, phone, email, role, selectedPermision, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                G.toast(Utils.getString(R.string.saved));
                selectedPermision.clear();
                edtUserName.setText("");
                edtPermission.setText("");
                edtPhone.setText("");
                edtRoll.setText("");
                btnAddNew.setText(Utils.getString(R.string.add_staff));
                edtUserName.setEnabled(true);
                edtPhone.setEnabled(true);
                getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.try_again));
            }
        });
    }


    private void dialog(StaffItem staffItem) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder1.setMessage(Utils.getString(R.string.are_you_sure_for_delete));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                Utils.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deleteStaff(staffItem);
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

    private void deleteStaff(StaffItem staffItem) {
        showLoading();
        Repository.getInstance().deleteStaff(String.valueOf(staffItem.getId()), new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                adapterStaff.removeItem(staffItem);
                G.toast(Utils.getString(R.string.deleted));
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

}
