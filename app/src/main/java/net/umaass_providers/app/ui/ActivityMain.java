package net.umaass_providers.app.ui;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.ui.base.BaseActivity;
import net.umaass_providers.app.ui.base.BaseFragment;
import net.umaass_providers.app.utils.Utils;
import net.umaass_providers.app.utils.backStackManager.FragNavController;
import net.umaass_providers.app.utils.backStackManager.FragmentHistory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ActivityMain extends BaseActivity implements
                                               BaseFragment.FragmentNavigation,
                                               FragNavController.TransactionListener,
                                               AHBottomNavigation.OnTabSelectedListener,
                                               FragNavController.RootFragmentListener {
    String[] TABS;
    private AHBottomNavigation bottomNavigation;
    private FragNavController mNavController;
    private FragmentHistory fragmentHistory;
    private int[] mTabIconsSelected = {
            R.drawable.ic_view_list_black_24dp,
            R.drawable.ic_book_black_24dp,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_settings_black_24dp};

    AdView mAdView;

    CheckVersion checkVersion = new CheckVersion();
    private String latestVersion = "1.0";
    private String currentVersion = "1.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readView();
        functionView();
        fragmentHistory = new FragmentHistory();

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.root)
                                          .transactionListener(this)
                                          .rootFragmentListener(this, TABS.length)
                                          .build();
        checkVersion.execute();
    }

    @Override
    public void readView() {
        super.readView();
        bottomNavigation = findViewById(R.id.bottom_navigation);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void functionView() {
        super.functionView();
        setupBottomNav();
    }


    private void setupBottomNav() {

        TABS = getResources().getStringArray(R.array.tab_name);
        for (int i = 0; i < TABS.length; i++) {
            bottomNavigation.addItem(new AHBottomNavigationItem(TABS[i], mTabIconsSelected[i]));
        }
        bottomNavigation.setUseElevation(true);
        bottomNavigation.setDefaultBackgroundColor(Utils.getColor(R.color.card_color));
        bottomNavigation.setInactiveColor(Utils.getColor(R.color.icon_color));
        bottomNavigation.setAccentColor(Utils.getColor(R.color.colorPrimary));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setColoredModeColors(Utils.getColor(R.color.white), Color.BLACK);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setOnTabSelectedListener(this);
    }

    private void switchTab(int position) {
        mNavController.switchTab(position);
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case FragNavController.TAB1:
                return new FragmentRequests();
            case FragNavController.TAB2:
                return new FragmentAppointments();
            case FragNavController.TAB3:
                return new FragmentCustomer();
            case FragNavController.TAB4:
                return new FragmentSetting();
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        BaseFragment baseFragment = null;
        if (fragment instanceof BaseFragment) {
            baseFragment = (BaseFragment) fragment;
        }
        switch (transactionType) {
            case POP:
                if (baseFragment != null) {
                    baseFragment.onPop();
                }
                break;
            case REPLACE:
                break;
            case PUSH:
                if (baseFragment != null) {
                    baseFragment.onPush();
                }

                break;
        }
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (wasSelected) {
            mNavController.clearStack();
            switchTab(position);
        } else {
            fragmentHistory.push(position);
            switchTab(position);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mNavController.getCurrentFrag() instanceof BaseFragment) {
            BaseFragment baseFragment = (BaseFragment) mNavController.getCurrentFrag();
            if (baseFragment.backPress()) {
                checkBack();
            }
        } else {
            checkBack();
        }

    }

    private void checkBack() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
            BaseFragment baseFragment = (BaseFragment) mNavController.getCurrentFrag();
            baseFragment.backPress();
        } else {
            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {
                if (fragmentHistory.getStackSize() > 1) {
                    int position = fragmentHistory.popPrevious();
                    switchTab(position);
                    updateTabSelection(position);

                } else {
                    switchTab(TABS.length - 1);
                    updateTabSelection(TABS.length - 1);
                    fragmentHistory.emptyStack();
                }
            }

        }
    }

    private void updateTabSelection(int currentTab) {
        bottomNavigation.setOnTabSelectedListener(null);
        bottomNavigation.setCurrentItem(currentTab);
        bottomNavigation.setOnTabSelectedListener(this);
    }

    public class CheckVersion extends AsyncTask<String, String, String> {
        private String newVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("https://www.google.com")
                        .get();
                if (document != null) {
                    Log.d("VersionChecker", "document");
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null){
                            Elements sibElements = ele.siblingElements();
                            for (Element sibElement : sibElements) {
                                Log.d("VersionChecker", "sibElements" + "------>" + sibElement.text());
                                newVersion = sibElement.text();
                            }

                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
        @Override
        protected void onPostExecute(String newVersion) {
            super.onPostExecute(newVersion);
            latestVersion = newVersion;
            compareVersion();

        }
    }

    @SuppressLint("ResourceAsColor")
    private void compareVersion() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(),0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (versionCompare(latestVersion, currentVersion) >= 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.warning));
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.new_version_available));
            builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" +getApplicationContext().getPackageName()));
                    startActivity(i);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            // dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.menu_color);


        }
    }

    int versionCompare(String str1 ,String str2 ) {
        Log.d("VersionCompare", "======>" + str1 + "======>" + str2);
        if (str1.equals(str2)) {
            return -1;
        } else {
            String[] vals1 = str1.split("\\.");
            String[] vals2 = str2.split("\\.");
            int i = 0;
            while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
                i++;
            }
            if (i < vals1.length && i < vals2.length) {
                int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
                return Integer.signum(diff);
            }
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}
