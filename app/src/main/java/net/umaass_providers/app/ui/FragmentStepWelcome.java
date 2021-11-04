package net.umaass_providers.app.ui;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.data.remote.models.City;
import net.umaass_providers.app.data.remote.models.Country;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.dialog.DialogList;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.List;

public class FragmentStepWelcome extends BaseFragment implements BlockingStep {

    EditText edtIndustry;
    EditText edtCity;
    EditText edtBusiness;
    EditText edtPhone;
    EditText edtAddress;
    EditText edtBio;
    String countryId = "";
    EditText etCountry;

    private NewIndustry newIndustry;

    public void setNewIndustry(NewIndustry newIndustry) {
        this.newIndustry = newIndustry;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_welcome;
    }

    @Override
    public void readView() {
        super.readView();
        edtIndustry = baseView.findViewById(R.id.edtIndustry);
        edtBusiness = baseView.findViewById(R.id.edtBusiness);
        edtPhone = baseView.findViewById(R.id.edtPhone);
        edtAddress = baseView.findViewById(R.id.edtAddress);
        edtCity = baseView.findViewById(R.id.edtCity);
        edtBio = baseView.findViewById(R.id.edtBio);
        etCountry = baseView.findViewById(R.id.etCountry);


    }

    @Override
    public void functionView() {
        super.functionView();
        edtIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogIndustry();
            }
        });
        edtCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryId.equals("")){
                    G.toast(getString(R.string.select_country_first));
                    return;
                }
                openDialogCity();
            }
        });

        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogCountry();
            }
        });
    }

    private String categoryId;
    private String cityId;

    private void openDialogIndustry() {
        showLoading();
        Repository.getInstance().getAllCategories(Preference.getLanguage(), new CallBack<Api<List<Category>>>() {
            @Override
            public void onSuccess(Api<List<Category>> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
                dialogList.setTitle(Utils.getString(R.string.choose_industry));
                dialogList.clearAndPut(listApi.getData());
                dialogList.setListener(new ItemClickListener<ListItem>() {
                    @Override
                    public void onClick(ListItem item) {
                        categoryId = item.getItemId();
                        edtIndustry.setError(null);
                        edtIndustry.setText(item.getItemName());
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

    private void openDialogCountry() {
        showLoading();
        Repository.getInstance().getCountries(new CallBack<Api<List<Country>>>() {
            @Override
            public void onSuccess(Api<List<Country>> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
                dialogList.setTitle(Utils.getString(R.string.select_country));
                dialogList.clearAndPut(listApi.getData());
                dialogList.setListener(new ItemClickListener<ListItem>() {
                    @Override
                    public void onClick(ListItem item) {
                        countryId = item.getItemId();
                        etCountry.setText(item.getItemName());
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

    private void openDialogCity() {
        showLoading();
        Repository.getInstance().getCitys(countryId, new CallBack<Api<List<City>>>() {
            @Override
            public void onSuccess(Api<List<City>> listApi) {
                super.onSuccess(listApi);
                hideLoading();
                DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
                dialogList.setTitle(Utils.getString(R.string.choose_city));
                dialogList.clearAndPut(listApi.getData());
                dialogList.setListener(new ItemClickListener<ListItem>() {
                    @Override
                    public void onClick(ListItem item) {
                        cityId = item.getItemId();
                        edtCity.setError(null);
                        edtCity.setText(item.getItemName());
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
        VerificationError verificationError = null;
        boolean b = Utils.isEmptyEditText(edtIndustry, edtCity, edtBusiness, edtPhone, edtAddress);
        if (b) {
            verificationError = new VerificationError(Utils.getString(R.string.not_empty));
        } else {
            if (newIndustry != null) {
                newIndustry.address = edtAddress.getText().toString();
                newIndustry.phone = edtPhone.getText().toString();
                newIndustry.title = edtBusiness.getText().toString();
                newIndustry.description = edtBio.getText().toString();
                newIndustry.city_id = cityId;
                newIndustry.category_id = categoryId;
            }
        }
        return verificationError;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


}
