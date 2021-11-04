package net.umaass_providers.app.ui.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.shuhart.stepview.StepView;


import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;

import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.Industry;
import net.umaass_providers.app.data.remote.models.Login;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.ChangeLanguageModel;
import net.umaass_providers.app.ui.ActivityIndustryCreator;
import net.umaass_providers.app.ui.ActivityLogin;
import net.umaass_providers.app.ui.ActivityMain;
import net.umaass_providers.app.ui.ActivityRegister;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.utils.LocaleUtils;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class Authentication extends BaseActivity {

    private int currentStep = 0;
    LinearLayout layout1,layout2,layout3;
    StepView stepView;
    AlertDialog dialog_verifying,profile_dialog;

    private static String uniqueIdentifier = null;
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final long ONE_HOUR_MILLI = 60*60*1000;

    private static final String TAG = "FirebasePhoneNumAuth";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;

    private String phoneNumber;
    private Button sendCodeButton;
    private Button verifyCodeButton;
    private Button signOutButton;
    private Button button3;

    private EditText phoneNum;
    private PinView verifyCodeET;
    private TextView phonenumberText,get_code;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
   private CountryCodePicker countryCodePicker;


    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Intent intent = getIntent();
        String message = intent.getStringExtra("langaa");
       // G.sharedPref.getString("language",message);
        LocaleUtils.setLocale(getBaseContext(), Preference.getLanguage());

        mAuth = FirebaseAuth.getInstance();
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);
        sendCodeButton = (Button) findViewById(R.id.submit1);
        verifyCodeButton = (Button) findViewById(R.id.submit2);
        button3 = (Button) findViewById(R.id.submit3);
        firebaseAuth = FirebaseAuth.getInstance();
        phoneNum = (EditText) findViewById(R.id.phonenumber);
        get_code =  findViewById(R.id.get_code);
        verifyCodeET = (PinView) findViewById(R.id.pinView);
        phonenumberText = (TextView) findViewById(R.id.phonenumberText);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        phoneNum.setHint(Utils.getString(R.string.are_you_sure_for_delete));

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
        layout1.setVisibility(View.VISIBLE);

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = phoneNum.getText().toString();
                phonenumberText.setText("+"+countryCodePicker.getSelectedCountryCode()+phoneNumber);

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNum.setError("Enter a Phone Number");
                    phoneNum.requestFocus();
                } else if (phoneNumber.length() < 10) {
                    phoneNum.setError("Please enter a valid phone");
                    phoneNum.requestFocus();
                } else {

                    if (currentStep < stepView.getStepCount() - 1) {
                        currentStep++;
                        stepView.go(currentStep, true);
                    } else {
                        stepView.done(true);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+"+countryCodePicker.getSelectedCountryCode()+ phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            Authentication.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        mCallbacks =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
                AlertDialog.Builder show = new AlertDialog.Builder(Authentication.this);

                show.setView(alertLayout);
                show.setCancelable(false);
                dialog_verifying = show.create();
                dialog_verifying.show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                G.toast(Utils.getString(R.string.try_again));

            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String verificationCode = verifyCodeET.getText().toString();
                if(verificationCode.isEmpty()){
                    Toast.makeText(Authentication.this,"Enter verification code", Toast.LENGTH_SHORT).show();
                }else {

                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout= inflater.inflate(R.layout.processing_dialog,null);
                    AlertDialog.Builder show = new AlertDialog.Builder(Authentication.this);

                    show.setView(alertLayout);
                    show.setCancelable(false);
                    dialog_verifying = show.create();
                    dialog_verifying.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });


        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep = 0;
                stepView.go(currentStep, true);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                } else {
                    stepView.done(true);
                }
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout= inflater.inflate(R.layout.profile_create_dialog,null);
                AlertDialog.Builder show = new AlertDialog.Builder(Authentication.this);
                show.setView(alertLayout);
                show.setCancelable(false);
                profile_dialog = show.create();
                profile_dialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profile_dialog.dismiss();
                        startActivity(new Intent(Authentication.this, ActivityLogin.class));
                        finish();
                    }
                },3000);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog_verifying.dismiss();
                            String a = task.getResult().getUser().getPhoneNumber();
                            checkLogin(credential.getProvider(),task.getResult().getUser().getPhoneNumber());
                        } else {

                            dialog_verifying.dismiss();
                            Toast.makeText(Authentication.this,"Something wrong", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }


    private void checkLogin(final String access_token,String phoneNumber) {
        showLoading();
        Repository.getInstance().getLogin(access_token,phoneNumber, new CallBack<Api<Login>>() {
            @Override
            public void onSuccess(Api<Login> s) {
                super.onSuccess(s);
                hideLoading();
                Preference.setToken(s.getData().getToken());
                checkNeedInit();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                if (e.getCode() == 412) {
                    intentToRegister(access_token,phoneNumber);
                } else {
                    G.toast(Utils.getString(R.string.try_again));
                    finish();
                }

            }
        });
    }

    private void checkNeedInit() {
        Repository.getInstance().getAllIndustries(true, new CallBack<Api<List<Industry>>>() {
            @Override
            public void onSuccess(Api<List<Industry>> listApi) {
                super.onSuccess(listApi);
                if (listApi == null || listApi.getData() == null || listApi.getData().size() == 0) {
                    intentToInit();
                } else {
                    Preference.setActiveIndustryId(listApi.getData().get(0).getId() + "");
                    intentToMain();
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                G.toast(Utils.getString(R.string.try_again));
                finish();
            }
        });
    }

    private void intentToMain() {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(Authentication.this, ActivityMain.class);
        startActivity(intent);
        finish();
    }

    private void intentToInit() {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(Authentication.this, ActivityIndustryCreator.class);
        startActivity(intent);
        finish();
    }

    private void intentToRegister(String access_token,String phoneNumber) {
        ActivityUtils.finishAllActivitiesExceptNewest();
        Intent intent = new Intent(Authentication.this, ActivityRegister.class);
        intent.putExtra("access_token", access_token);
        intent.putExtra("phone", phoneNumber);
        startActivity(intent);
        finish();
    }

    ProgressDialog progressDialog;

    private void showLoading() {
        progressDialog = new ProgressDialog(ActivityUtils.getTopActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.pls_wait));
        progressDialog.show();
    }

    private void hideLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void getData() {
        showLoading();
        Repository.getInstance().aboutUs(Preference.getLanguage(), new CallBack<Api<String>>() {
            @Override
            public void onSuccess(Api<String> stringApi) {
                super.onSuccess(stringApi);
                hideLoading();


            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });

    }


}
