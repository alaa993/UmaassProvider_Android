package net.umaass_providers.app.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.ui.adapter.AdapterBookReviewers;
import net.umaass_providers.app.ui.base.BaseFragment;

public class FragmentBooksReviewers extends BaseFragment {

    AdapterBookReviewers adapterBook;
    RecyclerView recyclerView;


    @Override
    public int getViewLayout() {
        return R.layout.fragment_books_deleted;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
    }

    @Override
    public void functionView() {
        super.functionView();
        adapterBook = new AdapterBookReviewers();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterBook);

    }

    private void getData() {

    }
}
