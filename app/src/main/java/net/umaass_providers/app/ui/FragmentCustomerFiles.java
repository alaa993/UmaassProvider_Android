package net.umaass_providers.app.ui;

import android.content.Intent;
import android.view.View;

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
import net.umaass_providers.app.data.remote.models.GalleryItem;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.models.CustomerFile;
import net.umaass_providers.app.ui.adapter.AdapterCustomerFiles;
import net.umaass_providers.app.ui.adapter.AdapterGalleryFiles;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.EmptyView;
import net.umaass_providers.app.utils.EndlessRecyclerOnScrollListener;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentCustomerFiles extends BaseFragment {

    private AdapterCustomerFiles adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipe;

    private String isUser;

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_customer_files;
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
    public void functionView() {
        super.functionView();
        adapter = new AdapterCustomerFiles();
        adapter.setListener(new ItemClickListener<CustomerFile>() {
            @Override
            public void onClick(CustomerFile item) {
                if (mfragmentNavigation != null) {
                    // FragmentDetail fragmentDetail = new FragmentDetail();
                    // fragmentDetail.setAppointment(item);
                    //  mfragmentNavigation.pushFragment(fragmentDetail);
                }
            }
        });
        adapter.setDeleteListener(new ItemClickListener<CustomerFile>() {
            @Override
            public void onClick(CustomerFile item) {
                Intent intent = new Intent(ActivityUtils.getTopActivity(), ActivityEditFiles.class);
                intent.putExtra("id", item.appointment.getId() + "");
                startActivity(intent);
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

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                G.log("Token", "Bearer " + Preference.getToken());
                getData(1);
            }
        });

        getData(1);
    }

    @Override
    public void onPop() {
        super.onPop();
        if (G.changeFile) {
            G.changeFile = false;
            getData(1);
        }
    }

    private void getData(int page_number) {
        if (swipe != null && page_number == 1) {
            swipe.setRefreshing(true);
        }
        emptyView.setVisibility(View.INVISIBLE);
        Repository.getInstance().getAllAppointments(isUser, page_number, "done",
                                                    null, null, new CallBack<Api<List<Appointment>>>() {
                    @Override
                    public void onSuccess(Api<List<Appointment>> listApi) {
                        super.onSuccess(listApi);
                        swipe.setRefreshing(false);
                        if (listApi.getData() != null && listApi.getData().size() > 0) {
                            List<Appointment> list = listApi.getData();
                            List<CustomerFile> customerFiles = new ArrayList<>();
                            for (Appointment appointment : list) {
                                CustomerFile customerFile = new CustomerFile();
                                customerFile.appointment = appointment;
                                customerFile.adapterGalleryFiles = new AdapterGalleryFiles();
                                customerFile.adapterGalleryFiles.clearAndPut(appointment.getImages());
                                customerFile.adapterGalleryFiles.setListener(new AdapterGalleryFiles.GalleryChooser() {
                                    @Override
                                    public void onItem(GalleryItem item, List<GalleryItem> list) {
                                        Intent intent = new Intent(ActivityUtils.getTopActivity(), ActivityShowImage.class);
                                        ArrayList<String> images = new ArrayList<>();
                                        for (GalleryItem galleryItem : list) {
                                            images.add(galleryItem.getUrlMd());
                                        }
                                        intent.putExtra("list", images);
                                        intent.putExtra("index", images.indexOf(item.getUrlMd()));
                                        startActivity(intent);
                                    }
                                });
                                customerFiles.add(customerFile);
                            }
                            if (page_number == 1) {
                                adapter.clearAndPut(null);
                            }
                            adapter.put(customerFiles);
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
