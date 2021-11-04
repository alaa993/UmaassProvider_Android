package net.umaass_providers.app.ui.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import net.umaass_providers.app.R;
import net.umaass_providers.app.utils.SwitchButton;
import net.umaass_providers.app.utils.Utils;


public class DayRangeTimeView extends FrameLayout {

    private TextView txtName;

    public DayRangeTimeView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DayRangeTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DayRangeTimeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayRangeTimeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    String text;
    String startText;
    String endText;
    boolean status;

    TextView txtText;
    TextView txtStartText;
    TextView txtEndText;
    SwitchButton swStatus;

    TimeClickListener listener;

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        View view = inflate(context, R.layout.cell_day_range_time, this);

        txtText = view.findViewById(R.id.txtText);
        txtStartText = view.findViewById(R.id.txtStartText);
        txtEndText = view.findViewById(R.id.txtEndText);
        swStatus = view.findViewById(R.id.swStatus);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DayRangeTimeView, 0, 0);
            text = a.getString(R.styleable.DayRangeTimeView_dr_text);
            startText = a.getString(R.styleable.DayRangeTimeView_dr_startTime);
            endText = a.getString(R.styleable.DayRangeTimeView_dr_EndTime);
            status = a.getBoolean(R.styleable.DayRangeTimeView_dr_status, false);
            a.recycle();
        }
        txtStartText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onStartClick(DayRangeTimeView.this);
                }
            }
        });
        txtEndText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onEndClick(DayRangeTimeView.this);
                }
            }
        });
        swStatus.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                status = isChecked;
            }
        });
        update();
    }

    public String getStartText() {
        return startText;
    }

    public String getEndText() {
        return endText;
    }

    public boolean isValid() {
        if (startText == null || startText.isEmpty()) {
            txtStartText.setHintTextColor(Utils.getColor(R.color.ms_errorColor));
            return false;
        } else {
            txtStartText.setHintTextColor(Utils.getColor(R.color.secondaryPrimaryText));
        }
        if (endText == null || endText.isEmpty()) {
            txtEndText.setHintTextColor(Utils.getColor(R.color.ms_errorColor));
            return false;
        } else {
            txtEndText.setHintTextColor(Utils.getColor(R.color.secondaryPrimaryText));
        }
        return true;
    }

    public void setListener(TimeClickListener listener) {
        this.listener = listener;
    }

    public void setTxtName(TextView txtName) {
        this.txtName = txtName;
        update();
    }

    public void setStartText(String startText) {
        this.startText = startText;
        update();
    }

    public void setEndText(String endText) {
        this.endText = endText;
        update();
    }

    public void setStatus(boolean status) {
        this.status = status;
        update();
    }

    private void update() {
        txtText.setText(text);
        if (startText != null) {
            txtStartText.setText(startText);
        } else {
            txtStartText.setHint(Utils.getString(R.string.start));
        }
        if (endText != null) {
            txtEndText.setText(endText);
        } else {
            txtEndText.setHint(Utils.getString(R.string.end));
        }
        swStatus.setChecked(status);
    }

    public boolean isStatus() {
        return status;
    }

    public void setText(String text) {
        txtName.setText(text);
    }

    public void setTextSizeDp(int size) {
        txtName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public void setTextColor(int color) {
        txtName.setTextColor(color);
    }

/*

    public void setCellColor(int color) {
        makeRoundCorner(color, 200, layParent, 0, 0);
    }
*/


    public interface TimeClickListener {
        void onStartClick(DayRangeTimeView rangeTimeView);

        void onEndClick(DayRangeTimeView rangeTimeView);
    }


}
