package net.umaass_providers.app.ui;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.models.CommonItem;
import net.umaass_providers.app.ui.adapter.AdapterAppointment;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.EmptyView;
import net.umaass_providers.app.ui.dialog.DialogList;
import net.umaass_providers.app.utils.EndlessRecyclerOnScrollListener;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentAppointments extends BaseFragment implements DatePickerDialog.OnDateSetListener {
    private RecyclerView recyclerViewDoctor;
    private Toolbar toolbar;
    private TextView btnTime;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipe;
    private AdapterAppointment adapter;

    private String startTime = "";
    private String endTime = "";


    @Override
    public int getViewLayout() {
        return R.layout.fragment_appointment;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerViewDoctor = baseView.findViewById(R.id.recyclerViewDoctor);
        toolbar = baseView.findViewById(R.id.toolbar);
        btnTime = baseView.findViewById(R.id.btnTime);
        emptyView = baseView.findViewById(R.id.emptyView);
        swipe = baseView.findViewById(R.id.swipe);
    }


    @Override
    public void functionView() {
        super.functionView();
        adapter = new AdapterAppointment();
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });
        LinearLayoutManager doctorManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewDoctor.setLayoutManager(doctorManager);
        recyclerViewDoctor.setAdapter(adapter);
        recyclerViewDoctor.addOnScrollListener(new EndlessRecyclerOnScrollListener(doctorManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getData(currentPage);
            }
        });
        adapter.setListener(new ItemClickListener<Appointment>() {
            @Override
            public void onClick(Appointment item) {
                if (mfragmentNavigation != null) {
                    FragmentDetail fragmentDetail = new FragmentDetail();
                    fragmentDetail.setAppointment(item);
                    mfragmentNavigation.pushFragment(fragmentDetail);
                }
            }
        });
        getData(1);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);
            }
        });
    }

    @Override
    public void onPop() {
        super.onPop();
        if (G.changeList) {
            G.changeList = false;
            getData(1);
        }
    }

    private void chooseDate() {
        List<CommonItem> list = new ArrayList<>();
        list.add(new CommonItem("0", Utils.getString(R.string.all)));
        list.add(new CommonItem("1",  Utils.getString(R.string.today)));
        list.add(new CommonItem("2",  Utils.getString(R.string.tomorrow)));
        list.add(new CommonItem("3", Utils.getString(R.string.select_a_date)));
        DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
        dialogList.setTitle(Utils.getString(R.string.select_date));
        dialogList.clearAndPut(list);
        dialogList.setListener(new ItemClickListener<ListItem>() {
            @Override
            public void onClick(ListItem item) {
                Calendar now = Calendar.getInstance();
                switch (item.getItemId()) {
                    case "0":
                        startTime = "";
                        endTime = "";
                        btnTime.setText(Utils.getString(R.string.all));
                        getData(1);
                        break;
                    case "1":
                        startTime = now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH);
                        endTime = startTime;
                        btnTime.setText(Utils.getString(R.string.today));
                        getData(1);
                        break;
                    case "2":
                        now.add(Calendar.DAY_OF_MONTH, 1);
                        startTime = now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH);
                        endTime = startTime;
                        btnTime.setText(Utils.getString(R.string.tomorrow));
                        getData(1);
                        break;
                    case "3":
                        openDialogDatePicker();
                        break;
                }
            }
        });
        dialogList.show();
    }

    private void openDialogDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FragmentAppointments.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                                                           );
        dpd.show(getChildFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        btnTime.setText(startTime);
        getData(1);
    }

    private void getData(int page_number) {
        if (swipe != null && page_number == 1) {
            adapter.clearAndPut(null);
            swipe.setRefreshing(true);
        }
        emptyView.setVisibility(View.INVISIBLE);
        Repository.getInstance().getAllAppointments(null, page_number, "confirmed", startTime, endTime, new CallBack<Api<List<Appointment>>>() {
            @Override
            public void onSuccess(Api<List<Appointment>> listApi) {
                super.onSuccess(listApi);
                swipe.setRefreshing(false);
                if (listApi.getData() != null && listApi.getData().size() > 0) {
                    adapter.put(listApi.getData());
                }
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
            }
        });
    }
}
