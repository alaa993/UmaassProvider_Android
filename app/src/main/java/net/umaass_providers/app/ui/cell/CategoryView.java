package net.umaass_providers.app.ui.cell;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.utils.Utils;


public class CategoryView extends FrameLayout {

    private TextView txtName;

    public CategoryView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CategoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CategoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CategoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        View view = inflate(context, R.layout.cell_category, this);
        txtName = view.findViewById(R.id.txtName);
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


    public static void makeRoundCorner(int bgcolor, int radius, View v, int strokeWidth, int strokeColor) {
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setShape(GradientDrawable.RECTANGLE);
        gdDefault.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gdDefault.setColor(Utils.getColor(bgcolor));
        //gdDefault.setColorFilter(bgcolor, PorterDuff.Mode.SRC);
        gdDefault.setCornerRadius(radius);
        // gdDefault.setStroke(strokeWidth, strokeColor);
        v.setBackground(gdDefault);
    }

}
