package net.umaass_providers.app.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.models.AppointmentDetail;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.Suggestion;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.models.CommonItem;
import net.umaass_providers.app.models.NewDateTime;
import net.umaass_providers.app.models.UpdateAppointmentTime;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.ui.dialog.DialogList;
import net.umaass_providers.app.utils.CircleImageView;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FragmentDetail extends BaseFragment {

    private Toolbar toolbar;
    private CircleImageView imgProfile;
    private TextView txtPatientName;
    private TextView txtPhone;
    private TextView txtStatus;
    private TextView btnDelete;
    private TextView txtDesc;
    private TextView btnFile;
    private TextView btnAddFile;
    private TextView txtDoctorName;
    private TextView txtTypeService;
    private TextView txtDate;
    private TextView txtTime;
    private RoundCornerButton btnSuggest;
    private Appointment appointment;

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_detail;
    }


    @Override
    public void readView() {
        super.readView();
        toolbar = baseView.findViewById(R.id.toolbar);
        txtPhone = baseView.findViewById(R.id.txtPhone);
        txtPatientName = baseView.findViewById(R.id.txtPatientName);
        imgProfile = baseView.findViewById(R.id.imgProfile);
        txtStatus = baseView.findViewById(R.id.txtStatus);
        btnDelete = baseView.findViewById(R.id.btnDelete);
        txtDoctorName = baseView.findViewById(R.id.txtDoctorName);
        txtTypeService = baseView.findViewById(R.id.txtTypeService);
        txtDate = baseView.findViewById(R.id.txtDate);
        txtTime = baseView.findViewById(R.id.txtTime);
        txtDesc = baseView.findViewById(R.id.txtDesc);
        btnSuggest = baseView.findViewById(R.id.btnSuggest);
        btnFile = baseView.findViewById(R.id.btnFile);
        btnAddFile = baseView.findViewById(R.id.btnAddFile);
    }


    @Override
    public void functionView() {
        super.functionView();

        txtPhone.setText(appointment.getClientPhone());
        txtPatientName.setText(appointment.getClientName());
        txtStatus.setText(appointment.getStatus());
        txtDoctorName.setText(appointment.getStaff().getName());
        txtTypeService.setText(appointment.getService().getTitle());
        txtDesc.setText(appointment.getDescription());

        String date = appointment.getStartTime() == null || appointment.getStartTime().isEmpty()
                      ? appointment.getFromTo() : appointment.getStartTime();
        StringTokenizer tk = new StringTokenizer(date);
        txtDate.setText(Utils.getString(R.string.date_));
        if (tk.hasMoreTokens()) {
            txtDate.append(tk.nextToken());
        }
        txtTime.setText(Utils.getString(R.string.time_));
        if (tk.hasMoreTokens()) {
            txtTime.append(tk.nextToken());
        }
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mfragmentNavigation != null && appointment != null) {
                    FragmentCustomerFiles fragmentDetail = new FragmentCustomerFiles();
                    fragmentDetail.setIsUser(appointment.getApplicant().getId() + "");
                    mfragmentNavigation.pushFragment(fragmentDetail);
                }
            }
        });

        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appointment != null) {
                    Intent intent = new Intent(ActivityUtils.getTopActivity(), ActivityEditFiles.class);
                    intent.putExtra("id", appointment.getId() + "");
                    startActivity(intent);
                }
            }
        });
        btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSuggest();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete();
            }
        });
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + appointment.getClientPhone()));
                startActivity(intent);
            }
        });
        txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusDialog();
            }
        });

    }

    private void changeStatusDialog() {
        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem("confirmed", Utils.getString(R.string.confirmed)));
        list.add(new CommonItem("pending", Utils.getString(R.string.pending)));
        list.add(new CommonItem("done", Utils.getString(R.string.done)));
        list.add(new CommonItem("no-show", Utils.getString(R.string.no_show)));
        DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
        dialogList.setTitle(Utils.getString(R.string.change_status));
        dialogList.clearAndPut(list);
        dialogList.setListener(new ItemClickListener<ListItem>() {
            @Override
            public void onClick(ListItem item) {
                update(item.getItemId(), null, null);
            }
        });
        dialogList.show();
    }

    private void dialogDelete() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder1.setMessage(Utils.getString(R.string.are_you_sure_for_delete));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                Utils.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        delete();
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

    private void getSuggest() {
        showLoading();
        Repository.getInstance().getSuggestionsAppointment(String.valueOf(appointment.getId()), new CallBack<Api<List<Suggestion>>>() {
            @Override
            public void onSuccess(Api<List<Suggestion>> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
                dialogList.setTitle(Utils.getString(R.string.choose_date));
                dialogList.clearAndPut(listApi.getData());
                dialogList.setListener(new ItemClickListener<ListItem>() {
                    @Override
                    public void onClick(ListItem item) {
                        Suggestion suggestion = (Suggestion) item;
                        update("confirmed", suggestion.getStart(), suggestion.getEnd());
                    }
                });
                dialogList.show();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

    private void update(String status, String start, String end) {
        showLoading();
        UpdateAppointmentTime updateAppointmentTime = new UpdateAppointmentTime();
        updateAppointmentTime.setStatus(status);
        NewDateTime newDateTime = new NewDateTime();
        newDateTime.setStart(start);
        newDateTime.setEnd(end);
        updateAppointmentTime.setDateTime(newDateTime);
        Repository.getInstance().updateAppointment(String.valueOf(appointment.getId()), updateAppointmentTime, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                G.changeList = true;
                getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

    private void getData() {
        showLoading();
        Repository.getInstance().getAppointmentDetail(String.valueOf(appointment.getId()), new CallBack<Api<AppointmentDetail>>() {
            @Override
            public void onSuccess(Api<AppointmentDetail> appointmentDetailApi) {
                super.onSuccess(appointmentDetailApi);
                hideLoading();
                AppointmentDetail detail = appointmentDetailApi.getData();
                if (detail != null) {
                    txtPhone.setText(detail.getClientPhone());
                    txtPatientName.setText(detail.getClientName());
                    txtStatus.setText(detail.getStatus());
                    txtDesc.setText(detail.getDescription());
                    if (detail.getStaff() != null) {
                        txtDoctorName.setText(detail.getStaff().getName());
                    }
                    if (detail.getService() != null) {
                        txtTypeService.setText(detail.getService().getTitle());
                    }
                    String date = detail.getStartTime() == null || detail.getStartTime().isEmpty()
                                  ? detail.getFromTo() : detail.getStartTime();
                    StringTokenizer tk = new StringTokenizer(date);
                    txtDate.setText(Utils.getString(R.string.date_));
                    if (tk.hasMoreTokens()) {
                        txtDate.append(tk.nextToken());
                    }
                    txtTime.setText(Utils.getString(R.string.time_));
                    if (tk.hasMoreTokens()) {
                        txtTime.append(tk.nextToken());
                    }
                }


            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

    private void delete() {
        showLoading();
        Repository.getInstance().deleteAppointment(String.valueOf(appointment.getId()), new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                G.changeList = true;
                G.toast(Utils.getString(R.string.deleted));
                onBackPress();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }
}
