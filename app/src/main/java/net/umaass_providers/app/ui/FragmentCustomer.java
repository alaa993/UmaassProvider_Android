package net.umaass_providers.app.ui;

import android.view.View;

import androidx.appcompat.widget.SearchView;
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
import net.umaass_providers.app.data.remote.models.Customer;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.ui.adapter.AdapterCustomer;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.EmptyView;
import net.umaass_providers.app.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentCustomer extends BaseFragment {

    private AdapterCustomer adapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private EmptyView emptyView;
    private SwipeRefreshLayout swipe;
    private SearchView searchView;


    @Override
    public int getViewLayout() {
        return R.layout.fragment_customer;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
        toolbar = baseView.findViewById(R.id.toolbar);
        emptyView = baseView.findViewById(R.id.emptyView);
        swipe = baseView.findViewById(R.id.swipe);
        searchView = baseView.findViewById(R.id.searchView);
    }


    @Override
    public void functionView() {
        super.functionView();
        adapter = new AdapterCustomer();
        adapter.setListener(new ItemClickListener<Customer>() {
            @Override
            public void onClick(Customer item) {
                if (mfragmentNavigation != null) {
                    FragmentCustomerFiles fragmentDetail = new FragmentCustomerFiles();
                    fragmentDetail.setIsUser(item.getId() + "");
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
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                getData(1);
                return true;
            }
        });
    }

    private String searchText;

    private List<Customer> customerList = new ArrayList<>();

    private void getData(int page_number) {
        if (swipe != null && page_number == 1) {
            swipe.setRefreshing(true);
        }
        emptyView.setVisibility(View.INVISIBLE);
        Repository.getInstance().getCustomers(searchText, page_number, new CallBack<Api<List<Customer>>>() {
            @Override
            public void onSuccess(Api<List<Customer>> listApi) {
                super.onSuccess(listApi);
                swipe.setRefreshing(false);

                if (listApi.getData() != null && listApi.getData().size() > 0) {
                    if (page_number == 1) {
                        customerList.clear();
                    }
                    customerList.addAll(listApi.getData());
                    adapter.clearAndPut(customerList);
                }
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                swipe.setRefreshing(false);
            }
        });
    }
}
