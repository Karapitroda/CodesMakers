package com.app.codesmakers.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.codesmakers.R;
import com.app.codesmakers.databinding.ActivityMainBinding;
import com.app.codesmakers.ui.account.AccountActivity;
import com.app.codesmakers.ui.base.BaseActivity;
import com.app.codesmakers.ui.base.BaseView;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.LocationUtils;
import com.app.codesmakers.utils.OnLocationFetchListener;
import com.app.codesmakers.utils.Utilities;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import static com.app.codesmakers.CMApplication.hyperLog;
import static com.app.codesmakers.utils.session.Keys.USER_LANGUAGE;

public class MainActivity extends BaseActivity implements BaseView {

    ActivityMainBinding binding;
    MainViewModel mainViewModel;
    public NavController navController;

    boolean isFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = MainActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class); // New
        mainViewModel.setBaseView(this);
        binding.setLifecycleOwner(this);

        setTransparentActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_stores, R.id.navigation_my_orders, R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_be_courier).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //ViewColorsUtils viewColorsUtils = new ViewColorsUtils();

        mainViewModel.getAccountLiveData().observe(this, accountModel -> {
            invalidateOptionsMenu();
            String language = accountModel.getLanguage();
            if (!language.isEmpty())
                SessionManager.getInstance().save(USER_LANGUAGE, language.toLowerCase());
        });

        initializeUI();

        if (SessionManager.isUserLoggedIn()) {
            LocationUtils.fetchCurrentLocation(this, new OnLocationFetchListener() {
                @Override
                public void locationReceived(LatLng latLng) {
                    if (!isFetched) {
                        mainViewModel.getUserAccount(latLng);
                        isFetched = true;
                    }
                }

                @Override
                public void permissionDenied() {

                }

                @Override
                public void errorInFetchLocation(Exception e, LatLng lastLatLng) {
                    if (!isFetched) {
                        mainViewModel.getUserAccount(lastLatLng);
                        isFetched = true;
                    }
                }
            });
        }

        new Thread(null, () -> {
            checkLocation((latLng) -> {
            });
        }, "location").start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeLanguageApp();

        updateLocale();

        Log.i("TAG", hyperLog.getDeviceLogsCount()+"");
        if (hyperLog.getDeviceLogsCount() >= 1000){
            callSettingsApi();
        }


    }

    int stringID = R.string.title_home;

    private void updateLocale() {
        binding.toolbar.setTitle(navController.getCurrentDestination().getLabel().toString());

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String lbl = navController.getCurrentDestination().getLabel().toString();
            Log.e("lbbb", "value " + lbl);
            switch (lbl) {
                case "البداية":
                case "Home":
                    stringID = R.string.title_home;
                    break;
                case "MyOrders":
                case "طلباتي":
                    stringID = R.string.title_orders;
                    break;
                case "Stores":
                case "المتاجر":
                    stringID = R.string.title_stores;
                    break;
                case "Requests for delivery":
                case "طلبات التوصيل":
                case "توصيل الطلبات":
                    stringID = R.string.title_courier;
                    break;
                case "Notifications":
                case "التنبيهات":
                    stringID = R.string.title_notifications;
                    break;
            }
            binding.toolbar.setTitle(getResources().getString(stringID));
        });


        MenuItem home = binding.navView.getMenu().findItem(R.id.navigation_home);
        MenuItem store = binding.navView.getMenu().findItem(R.id.navigation_stores);
        MenuItem orders = binding.navView.getMenu().findItem(R.id.navigation_my_orders);
        MenuItem notification = binding.navView.getMenu().findItem(R.id.navigation_notifications);
        MenuItem courier = binding.navView.getMenu().findItem(R.id.navigation_be_courier);

        home.setTitle(getResources().getString(R.string.title_home));
        store.setTitle(getResources().getString(R.string.title_stores));
        orders.setTitle(getResources().getString(R.string.title_orders));
        notification.setTitle(getResources().getString(R.string.title_notifications));
        courier.setTitle(getResources().getString(R.string.title_becourier));
    }

    @Override
    public void initializeUI() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            binding.toolbar.setTitle(controller.getCurrentDestination().getLabel());
        });
        //initBottomNavigationMenu();
    }

    private void initBottomNavigationMenu() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked} // unchecked
        };
        int[] colors = new int[]{Color.parseColor(AppConstants.TEXT_COLOR_PRIMARY), Color.parseColor(AppConstants.NAV_BAR_NORMAL_COLOR)};

        ColorStateList colorStateList = new ColorStateList(states, colors);
        binding.navView.setItemTextColor(colorStateList);
        binding.navView.setItemIconTintList(colorStateList);
        //ViewColorsUtils.changeProgressBarColor(binding.layoutProgressView.progressBar);
    }

    public void showHideBackButton(boolean isSHow) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(isSHow);
        if (isSHow)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        if (navController.getCurrentDestination() != null) {
            String lbl = navController.getCurrentDestination().getLabel().toString();
            binding.toolbar.setTitle(lbl);
            //binding.toolbarLayout.setTitle(lbl);
            return false;
        } else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_user_image) {
            startNewIntent(new Intent(activity, AccountActivity.class));
        }
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem userItem = menu.findItem(R.id.menu_user_image);
        if (mainViewModel.getAccountLiveData().getValue() != null) {
            String url = mainViewModel.getAccountLiveData().getValue().getPhoto();
            if (url.isEmpty()) {
                return super.onPrepareOptionsMenu(menu);
            }
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    return Utilities.getBitmapFromUrl(params[0]);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap == null)
                        return;
                    userItem.setIcon(new BitmapDrawable(getResources(), bitmap));
                }
            }.execute(url);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updateProgress(boolean isShow) {
        binding.layoutProgressView.progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // If User Checked 'Don't Show Again' checkbox for runtime permission, then navigate user to Settings
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Permission Required");
                    dialog.setCancelable(false);
                    dialog.setMessage("You have to Allow permission to access user location");
                    dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",
                                    getPackageName(), null));
                            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i, 1001);
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                }
                //code for deny
            }
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        switch (requestCode) {
            case 1001:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 10);
                    }
                }
                break;
            default:
                break;
        }
    }


}