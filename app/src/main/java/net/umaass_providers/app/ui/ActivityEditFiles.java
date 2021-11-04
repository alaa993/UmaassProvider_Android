package net.umaass_providers.app.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.AppointmentDetail;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.GalleryItem;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.models.UpdateAppointmentTime;
import net.umaass_providers.app.ui.adapter.AdapterGalleryFiles;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.components.RoundCornerButton;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;
import net.umaass_providers.app.utils.permission.helper.PermissionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ActivityEditFiles extends BaseActivity {

    EditText edtPrescription;
    AdapterGalleryFiles adapterGallery;
    RoundCornerButton btnDone;
    RoundCornerButton btnAddImage;
    RecyclerView recyclerView;

    String appointmentId;

    private static final int PICK_FROM_FILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_files);
        appointmentId = getIntent().getStringExtra("id");
        readView();
        functionView();
        initViewModel();
    }

    @Override
    public void readView() {
        super.readView();
        edtPrescription = findViewById(R.id.edtPrescription);
        recyclerView = findViewById(R.id.recyclerView);
        btnDone = findViewById(R.id.btnDone);
        btnAddImage = findViewById(R.id.btnAddImage);
    }

    @Override
    public void functionView() {
        super.functionView();
        adapterGallery = new AdapterGalleryFiles();
        recyclerView.setAdapter(adapterGallery);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapterGallery.setListener(new AdapterGalleryFiles.GalleryChooser() {
            @Override
            public void onItem(GalleryItem item, List<GalleryItem> list) {
                dialogDelete(item);
            }
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionHelper.requestStorage(new PermissionHelper.OnPermissionGrantedListener() {
                    @Override
                    public void onPermissionGranted() {
                        startPickImage();
                    }
                });
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        // btnDone.setVisibility(View.INVISIBLE);
        getData();
    }

    private void getData() {
        Repository.getInstance().getAppointmentDetail(appointmentId, new CallBack<Api<AppointmentDetail>>() {
            @Override
            public void onSuccess(Api<AppointmentDetail> appointmentDetailApi) {
                super.onSuccess(appointmentDetailApi);
                AppointmentDetail detail = appointmentDetailApi.getData();
                edtPrescription.setText(detail.getPrescription());
                adapterGallery.clearAndPut(detail.getImages());
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
            }
        });
    }

    private void sendImage(String image) {
        showLoading();
        Repository.getInstance().uploadAppointmentFiles(appointmentId, image, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
                G.toast(Utils.getString(R.string.pls_try_again));
            }
        });
    }

    private void update() {
        boolean b = check(edtPrescription);
        if (b) {
            return;
        }
        String pres = edtPrescription.getText().toString();
        showLoading();

        UpdateAppointmentTime updateAppointmentTime = new UpdateAppointmentTime();
        updateAppointmentTime.setStatus("done");
        updateAppointmentTime.setPrescription(pres);
        updateAppointmentTime.setAppointmentId(appointmentId);

        Repository.getInstance().updateAppointment(appointmentId, updateAppointmentTime, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                G.toast(Utils.getString(R.string.saved));
                G.changeFile = true;
                finish();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }


    private void dialogDelete(final GalleryItem item) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityUtils.getTopActivity());
        builder1.setMessage(Utils.getString(R.string.are_you_sure_for_delete));
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                Utils.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        deleteItem(item);
                    }
                });

        builder1.setNegativeButton(
                Utils.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteItem(GalleryItem galleryItem) {
        showLoading();
        Repository.getInstance()
                  .deleteAppointmentImage(appointmentId,
                                          galleryItem.getId() + "",
                                          new CallBack<Api<DefualtResponse>>() {
                                              @Override
                                              public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                                                  super.onSuccess(defualtResponseApi);
                                                  hideLoading();
                                                  G.changeFile = true;
                                                  getData();
                                              }

                                              @Override
                                              public void onFail(RequestException e) {
                                                  super.onFail(e);
                                                  hideLoading();
                                              }
                                          });
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


    private boolean check(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText() != null) {
                if (editText.getText().length() == 0) {
                    editText.setError(Utils.getString(R.string.not_empty));
                    return true;
                }
            }
        }
        return false;
    }

    private void startPickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);

    }

    List<GalleryItem> list = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    String imagePath = resultUri.getPath();
                    Utils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            sendImage(imagePath);
                        }
                    }, 500);
                    break;
                case PICK_FROM_FILE:
                    String path = Utils.getPath(data.getData());
                    if (path != null) {
                        File file = new File(path);
                        Uri uri = Uri.fromFile(file);
                        startCropImageActivity(uri);
                    }
                    break;
            }
        }
    }

    private void startCropImageActivity(Uri mImageCaptureUri) {
        CropImage.activity(mImageCaptureUri)
                 .setAutoZoomEnabled(true)
                 .setAspectRatio(1, 1)
                 .setRequestedSize(600, 600)
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .start(this);
    }

}
