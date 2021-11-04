package net.umaass_providers.app.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.Repository;
import net.umaass_providers.app.data.interfaces.CallBack;
import net.umaass_providers.app.data.remote.models.Api;
import net.umaass_providers.app.data.remote.models.DefualtResponse;
import net.umaass_providers.app.data.remote.models.GalleryItem;
import net.umaass_providers.app.data.remote.models.ShowIndustry;
import net.umaass_providers.app.data.remote.utils.RequestException;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.models.CommonItem;
import net.umaass_providers.app.ui.adapter.AdapterGallery;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.ui.components.EmptyView;
import net.umaass_providers.app.ui.dialog.DialogList;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.permission.ActivityUtils;
import net.umaass_providers.app.utils.permission.helper.PermissionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentGallery extends BaseFragment {

    private RecyclerView recyclerView;
    private ImageView btnAddNew;
    private AdapterGallery adapterGallery;
    EmptyView emptyView;
    SwipeRefreshLayout swipe;
    private static final int PICK_FROM_FILE = 3;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void readView() {
        super.readView();
        recyclerView = baseView.findViewById(R.id.recyclerView);
        btnAddNew = baseView.findViewById(R.id.btnAddNew);
        emptyView = baseView.findViewById(R.id.emptyView);
        swipe = baseView.findViewById(R.id.swipe);
    }


    @Override
    public void functionView() {
        super.functionView();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
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
        adapterGallery = new AdapterGallery();
        GridLayoutManager categoryManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(categoryManager);
        recyclerView.setAdapter(adapterGallery);

        adapterGallery.setListener(new ItemClickListener<GalleryItem>() {
            @Override
            public void onClick(GalleryItem galleryItem) {
                List<CommonItem> itemList = new ArrayList<>();
                itemList.add(new CommonItem("1", Utils.getString(R.string.delete)));
                DialogList dialogList = new DialogList(ActivityUtils.getTopActivity());
                dialogList.setTitle(Utils.getString(R.string.gallery));
                dialogList.clearAndPut(itemList);
                dialogList.setListener(new ItemClickListener<ListItem>() {
                    @Override
                    public void onClick(ListItem item) {
                        if (item.getItemId().equals("1")) {
                            dialogDelete(galleryItem);
                        }
                    }
                });
                dialogList.show();
            }
        });

        getData();

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

    private void deleteItem(GalleryItem item) {
        showLoading();
        Repository.getInstance().deleteImageIndustry(String.valueOf(item.getId()), new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                adapterGallery.removeItem(item);
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                hideLoading();
            }
        });
    }

    private void upload() {
        if (imagePath == null || industry == null) {
            return;
        }
        showLoading();
        Repository.getInstance().uploadImageIndustry(industry.getId() + "", "gallery", imagePath, new CallBack<Api<DefualtResponse>>() {
            @Override
            public void onSuccess(Api<DefualtResponse> defualtResponseApi) {
                super.onSuccess(defualtResponseApi);
                hideLoading();
                getData();
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                G.toast(Utils.getString(R.string.try_again));
                hideLoading();
            }
        });
    }

    private ShowIndustry industry;

    private void getData() {
        if (swipe != null) {
            swipe.setRefreshing(true);
        }
        emptyView.setVisibility(View.INVISIBLE);
        Repository.getInstance().showIndustry(Preference.getActiveIndustryId(), new CallBack<Api<ShowIndustry>>() {
            @Override
            public void onSuccess(Api<ShowIndustry> listApi) {
                super.onSuccess(listApi);
                swipe.setRefreshing(false);
                if (listApi != null) {
                    if (listApi.getData() != null) {
                        industry = listApi.getData();
                        adapterGallery.clearAndPut(industry.getGallery());
                        if (adapterGallery.getItemCount() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        YoYo.with(Techniques.SlideInDown)
                            .duration(500)
                            .playOn(recyclerView);
                    }
                }
            }

            @Override
            public void onFail(RequestException e) {
                super.onFail(e);
                swipe.setRefreshing(false);
            }
        });

    }


    private void startPickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);

    }

    private String imagePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    imagePath = resultUri.getPath();
                    Utils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            upload();
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
                 .setAutoZoomEnabled(false)
                 .setAspectRatio(1, 1)
                 .setRequestedSize(1024,1024)
                 .setGuidelines(CropImageView.Guidelines.ON)
                 .start(G.getInstance(), this);
    }


}
