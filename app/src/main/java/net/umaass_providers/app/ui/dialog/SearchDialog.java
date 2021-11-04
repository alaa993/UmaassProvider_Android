package net.umaass_providers.app.ui.dialog;

import android.annotation.SuppressLint;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.ui.adapter.AdapterCategory;
import net.umaass_providers.app.ui.base.BaseDialog;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class SearchDialog extends BaseDialog {

    RecyclerView rv_gender, rv_place;
    RangeSeekBar seekbar_distance;
    AppCompatTextView txt_distance;
    SwitchButton switch_button;
    LinearLayout ll_grade, ll_specialty, ll_city;

    @Override
    public int getViewLayout() {
        return R.layout.dialog_search;
    }

    @Override
    public void readView() {
        super.readView();
        rv_gender = baseView.findViewById(R.id.rv_gender);
        rv_place = baseView.findViewById(R.id.rv_place);
        txt_distance = baseView.findViewById(R.id.txt_distance);
        seekbar_distance = baseView.findViewById(R.id.seekbar_distance);
        switch_button = baseView.findViewById(R.id.switch_button);
        ll_grade = baseView.findViewById(R.id.ll_grade);
        ll_specialty = baseView.findViewById(R.id.ll_specialty);
        ll_city = baseView.findViewById(R.id.ll_city);
    }

    @Override
    public void functionView() {
        super.functionView();


        ll_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradeDialog gradeDialog = new GradeDialog();
                gradeDialog.show(getChildFragmentManager(), "gradeDialog");
            }
        });

        ll_specialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityDialog cityDialog = new CityDialog();
                cityDialog.show(getChildFragmentManager(), "specialtyDialog");
            }
        });

        ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityDialog cityDialog = new CityDialog();
                cityDialog.show(getChildFragmentManager(), "cityDialog");
            }
        });
        seekbar_distance.setVisibility(View.GONE);
        seekbar_distance.setRange(500, 10000);
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    seekbar_distance.setVisibility(View.VISIBLE);
                    seekbar_distance.setValue(500);
                } else {
                    seekbar_distance.setVisibility(View.GONE);
                    txt_distance.setText(getString(R.string.unlimit));
                }
            }
        });
        seekbar_distance.setOnRangeChangedListener(new OnRangeChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                int value = (int) leftValue;
                if (value < 1000)
                    txt_distance.setText(value + " " + getString(R.string.meter));
                else {
                    int kValue = value / 1000;
                    int mValue = (value % 1000) / 10;
                    txt_distance.setText(String.format(Utils.getCurrentLocale(getContext()),
                            "%d.%d " + getString(R.string.kilometer), kValue, mValue));
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        AdapterCategory adapterGender = new AdapterCategory(getActivity());
        AdapterCategory adapterPlace = new AdapterCategory(getActivity());

        List<Category> test = new ArrayList<>();
        test.add(new Category());
        test.add(new Category());
        test.add(new Category());

        List<Category> test2 = new ArrayList<>(test);

        adapterGender.enableChoosingMode();
        adapterPlace.enableChoosingMode();

        adapterGender.set(test);
        adapterPlace.set(test2);

        LinearLayoutManager categoryManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_gender.setLayoutManager(categoryManager);
        rv_gender.setAdapter(adapterGender);


        LinearLayoutManager categoryManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_place.setLayoutManager(categoryManager2);
        rv_place.setAdapter(adapterPlace);
    }

}
