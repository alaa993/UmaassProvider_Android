package net.umaass_providers.app.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.ReferalModel;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.ui.adapter.AdapterAppointment;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.EmptyView;
import net.umaass_providers.app.utils.EndlessRecyclerOnScrollListener;

import java.util.List;

public class FragmentRequests extends BaseFragment {

    private AdapterAppointment adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipe;
    private String startTime = "";
    private String endTime = "";


    @Override
    public int getViewLayout() {
        return R.layout.fragment_request;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
        toolbar = baseView.findViewById(R.id.toolbar);
        emptyView = baseView.findViewById(R.id.emptyView);
        swipe = baseView.findViewById(R.id.swipe);
    }

    @Override
    public void onPop() {
        super.onPop();
        if (G.changeList) {
            G.changeList = false;
            getData(1);
        }
    }

    @Override
    public void functionView() {
        super.functionView();
        adapter = new AdapterAppointment();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getData(currentPage);
            }
        });
        getData(1);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                G.log("Token", "Bearer " + Preference.getToken());
                getData(1);
                getReferalData();

            }
        });
        getReferalData();

    }

    private void getData(int page_number) {
        if (swipe != null && page_number == 1) {
            adapter.clearAndPut(null);
            swipe.setRefreshing(true);
        }
        emptyView.setVisibility(View.INVISIBLE);
        Repository.getInstance().getAllAppointments(null,page_number, "pending", startTime, endTime, new CallBack<Api<List<Appointment>>>() {
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

    private void getReferalData() {
        Repository.getInstance().getReferalData(new CallBack<Api<List<ReferalModel>>>() {
            @Override
            public void onSuccess(Api<List<ReferalModel>> listApi) {
                super.onSuccess(listApi);
                swipe.setRefreshing(false);
                if (listApi.getData() != null && listApi.getData().size() > 0) {
                    dialogReferal(listApi.getData().get(0));
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
            }
        });
    }

    private void dialogReferal(final ReferalModel item) {
        LayoutInflater li = LayoutInflater.from(requireContext());
        View promptsView = li.inflate(R.layout.dialog_referal, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setView(promptsView);
        final AppCompatTextView txtTitle = promptsView.findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.is_your_introducer, item.getUser().getName()));
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(getString(R.string.accept), (dialog, which) -> {
            Repository.getInstance().statusReferal("APPROVED", item.getId()+"", new CallBack<Api<DefualtResponse>>() {
                @Override
                public void onSuccess(Api<DefualtResponse> listApi) {
                    super.onSuccess(listApi);
                    dialog.dismiss();
                }

                @Override
                public void onFail(RequestException e) {
                    super.onFail(e);

                }
            });

        });
        alertDialogBuilder.setNegativeButton(getString(R.string.reject), (dialog, which) -> {
            Repository.getInstance().statusReferal("REJECT", item.getId()+"", new CallBack<Api<DefualtResponse>>() {
                @Override
                public void onSuccess(Api<DefualtResponse> listApi) {
                    super.onSuccess(listApi);

                    dialog.dismiss();
                }

                @Override
                public void onFail(RequestException e) {
                    super.onFail(e);
                }
            });

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
