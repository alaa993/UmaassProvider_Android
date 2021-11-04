package net.umaass_providers.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.IndustryResult;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.models.WorkingHoursItem;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.NewIndustry;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.cell.DayRangeTimeView;
import net.umaass_providers.app.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentStepHours extends BaseFragment implements BlockingStep, TimePickerDialog.OnTimeSetListener, DayRangeTimeView.TimeClickListener {

    private Toolbar toolbar;
    private ImageView btnSave;
    boolean inStep = false;

    private DayRangeTimeView vSunday;
    private DayRangeTimeView vMonday;
    private DayRangeTimeView vTuesday;
    private DayRangeTimeView vWednesday;
    private DayRangeTimeView vThursday;
    private DayRangeTimeView vFriday;
    private DayRangeTimeView vSaturday;

    private NewIndustry newIndustry;

    public void setNewIndustry(NewIndustry newIndustry) {
        this.newIndustry = newIndustry;
    }

    public static FragmentStepHours newInstance() {
        Bundle args = new Bundle();
        FragmentStepHours fragment = new FragmentStepHours();
        fragment.setArguments(args);
        return fragment;
    }

    public void setInStep(boolean inStep) {
        this.inStep = inStep;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_step_hours;
    }

    private List<DayRangeTimeView> rangeTimeViewList = new ArrayList<>();

    @Override
    public void readView() {
        super.readView();
        toolbar = baseView.findViewById(R.id.toolbar);
        vSunday = baseView.findViewById(R.id.vSunday);
        vMonday = baseView.findViewById(R.id.vMonday);
        vTuesday = baseView.findViewById(R.id.vTuesday);
        vWednesday = baseView.findViewById(R.id.vWednesday);
        vThursday = baseView.findViewById(R.id.vThursday);
        vFriday = baseView.findViewById(R.id.vFriday);
        vSaturday = baseView.findViewById(R.id.vSaturday);
        btnSave = baseView.findViewById(R.id.btnSave);
    }


    @Override
    public void functionView() {
        super.functionView();
        rangeTimeViewList.add(vSunday);
        rangeTimeViewList.add(vMonday);
        rangeTimeViewList.add(vTuesday);
        rangeTimeViewList.add(vWednesday);
        rangeTimeViewList.add(vThursday);
        rangeTimeViewList.add(vFriday);
        rangeTimeViewList.add(vSaturday);

        toolbar.setVisibility(inStep ? View.GONE : View.VISIBLE);

        vSunday.setListener(this);
        vMonday.setListener(this);
        vTuesday.setListener(this);
        vWednesday.setListener(this);
        vThursday.setListener(this);
        vFriday.setListener(this);
        vSaturday.setListener(this);

        btnSave.setVisibility(inStep ? View.GONE : View.VISIBLE);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
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
                        if (industry.getWorkingHours() != null) {
                            for (WorkingHoursItem workingHour : industry.getWorkingHours()) {
                                DayRangeTimeView timeView = rangeTimeViewList.get(workingHour.getDay());
                                timeView.setStartText(workingHour.getStart());
                                timeView.setEndText(workingHour.getEnd());
                                timeView.setStatus(true);
                            }
                        }

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

    private StepperLayout.OnNextClickedCallback callback;

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        this.callback = callback;
        createIndustry();
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
        newIndustry.working_hours.clear();
        newIndustry.working_hours.addAll(getItems());
        if (!isAllValid()) {
            verificationError = new VerificationError(Utils.getString(R.string.please_set_your_time));
        }
        return verificationError;
    }

    private boolean isAllValid() {
        for (DayRangeTimeView dayRangeTimeView : rangeTimeViewList) {
            if (dayRangeTimeView.isStatus()) {
                if (!dayRangeTimeView.isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<WorkingHoursItem> getItems() {
        List<WorkingHoursItem> workingHoursItems = new ArrayList<>();
        int i = 0;
        for (DayRangeTimeView dayRangeTimeView : rangeTimeViewList) {
            if (dayRangeTimeView.isStatus()) {
                if (dayRangeTimeView.isValid()) {
                    WorkingHoursItem workingHoursItem = new WorkingHoursItem();
                    workingHoursItem.setDay(i);
                    workingHoursItem.setEnd(dayRangeTimeView.getEndText());
                    workingHoursItem.setStart(dayRangeTimeView.getStartText());
                    workingHoursItems.add(workingHoursItem);
                }

            }
            i++;
        }
        return workingHoursItems;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void update() {
        if (!isAllValid()) {
            return;
        }
        showLoading();
        Repository.getInstance().updateWorkingHours(industry.getId() + "", getItems(), new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                Log.d("alaaisaa", String.valueOf(defualtResponseApi));
                hideLoading();
                G.toast(Utils.getString(R.string.saved));
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });

    }

    private DayRangeTimeView currentRangeTimeView;
    private boolean isStart = true;

    @Override
    public void onStartClick(DayRangeTimeView rangeTimeView) {
        currentRangeTimeView = rangeTimeView;
        isStart = true;
        openTimeDialog(false, null);
    }

    @Override
    public void onEndClick(DayRangeTimeView rangeTimeView) {
        currentRangeTimeView = rangeTimeView;
        isStart = false;
        if (rangeTimeView.getStartText() != null) {
            String[] strings = rangeTimeView.getStartText().split("\\:");
            if (strings.length == 2) {
                openTimeDialog(true, new Timepoint(Integer.parseInt(strings[0]), Integer.parseInt(strings[1])));
            }
        }

    }


    private void openTimeDialog(boolean end, Timepoint timepoint) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY), // Initial year selection
                now.get(Calendar.MINUTE), // Initial month selection
                true); // Inital day selection
        if (end && timepoint != null) {
            dpd.setMinTime(timepoint);
        }
        dpd.show(getChildFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute;
        if (currentRangeTimeView != null) {
            if (isStart) {
                currentRangeTimeView.setStartText(time);
            } else {
                currentRangeTimeView.setEndText(time);
            }

        }
    }


    private void createIndustry() {
        showLoading();
        Repository.getInstance().createIndustry(newIndustry, new CallBack<Api<IndustryResult>>() {
            @Override
            public void onSuccess(Api<IndustryResult> industryResultApi) {
                super.onSuccess(industryResultApi);
                hideLoading();
                if (industryResultApi.getData() != null) {
                    Preference.setActiveIndustryId(industryResultApi.getData().getId() + "");
                    Preference.setValidLogin(true);
                    if (callback != null) {
                        callback.goToNextStep();
                    }
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                if (e.getCode() == 422) {
                    G.toast(getString(R.string.start_and_date_time));
                } else {
                    G.toast(Utils.getString(R.string.try_again));
                }
            }
        });
    }


}
