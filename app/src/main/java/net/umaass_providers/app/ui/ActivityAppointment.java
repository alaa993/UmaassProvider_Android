package net.umaass_providers.app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import net.umaass_providers.app.R;
import net.umaass_providers.app.ui.base.BaseActivity;

public class ActivityAppointment extends BaseActivity {

    AppCompatTextView txt_central_tel, txt_appointment_tel;
    AppCompatButton btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        readView();
        functionView();
        initViewModel();
    }

    @Override
    public void readView() {
        super.readView();
        txt_central_tel = findViewById(R.id.txt_central_tel);
        txt_appointment_tel = findViewById(R.id.txt_appointment_tel);
        btnBook = findViewById(R.id.btnBook);
    }

    @Override
    public void functionView() {
        super.functionView();
        txt_appointment_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + txt_appointment_tel.getText().toString()));
                startActivity(intent);
            }
        });
        txt_central_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + txt_central_tel.getText().toString()));
                startActivity(intent);
            }
        });
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAppointment.this, ActivityIndustryCreator.class);
                startActivity(intent);
            }
        });
    }
}
