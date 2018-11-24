

package mobi.MultiCraft;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.MrecCallbacks;
import com.appodeal.ads.MrecView;
import com.startapp.android.publish.ads.banner.Mrec;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;

//import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//import static mobi.MultiCraft.PreferencesHelper.TAG_BUILD_NUMBER;
//import static mobi.MultiCraft.PreferencesHelper.TAG_CONSENT_ASKED;
import static mobi.MultiCraft.PreferencesHelper.TAG_LAUNCH_TIMES;
//import static mobi.MultiCraft.PreferencesHelper.TAG_SHORTCUT_CREATED;

public class AboutUs extends AppCompatActivity implements WVersionManager.ActivityListener{
    private Button btnGo;
    public static TinyDB tb;


    private static final String[] EU_COUNTRIES = new String[]{
            "AT", "BE", "BG", "HR", "CY", "CZ",
            "DK", "EE", "FI", "FR", "DE", "GR",
            "HU", "IE", "IT", "LV", "LT", "LU",
            "MT", "NL", "PL", "PT", "RO", "SK",
            "SI", "ES", "SE", "GB", "IS", "LI", "NO"};

    private ImageView iv;
    private WVersionManager versionManager = null;
private boolean isConnected;
    private PermissionManager pm = null;
    private PreferencesHelper pf;
private boolean isMrecShowed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_mainmenu);
tb = new TinyDB(this);
        //pf = PreferencesHelper.getInstance(this);
       // IntentFilter filter = new IntentFilter(UnzipService.ACTION_UPDATE);
       // registerReceiver(myReceiver, filter);
       // if (!isTaskRoot()) {
         //   finish();
          //  return;
      //  }
//        addLaunchTimes();
        String appKey = "b959914a0ec861468c259c89a503af8ef2874a8652435c7f";
        //Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(false);
        Appodeal.setBannerViewId(R.id.appodeal_banner_view);
        Appodeal.setAutoCache(Appodeal.NATIVE, true);
        Appodeal.setMrecViewId(R.id.appodealMrecView);

        Appodeal.initialize(this, appKey,
                Appodeal.BANNER);
        Appodeal.initialize(this, appKey,

                Appodeal.MREC);

        final Context context = this;
        final Activity activity = this;
        Appodeal.show(this, Appodeal.BANNER);
        Appodeal.setMrecCallbacks(new MrecCallbacks() {
            @Override
            public void onMrecLoaded(boolean b) {
                Appodeal.show(activity, Appodeal.MREC);
                isMrecShowed = true;
            }

            @Override
            public void onMrecFailedToLoad() {

            }

            @Override
            public void onMrecShown() {

            }

            @Override
            public void onMrecClicked() {

            }

            @Override
            public void onMrecExpired() {

            }
        });
        if(isGdprSubject() && tb.getBoolean("GPDRapplied") != true){
            Intent gpdr = new Intent(this, GPDRActivity.class);
            startActivity(gpdr);
        }

        //getPermissions();
        btnGo = (Button) findViewById(R.id.go);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                isConnected = isConnected(context);
            }
        });
        thread.start();
        btnGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!isConnected){
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        isConnected = isConnected(context);
                    }
                });
                thread.start();}
                if(isMrecShowed) {
                    if (tb.getBoolean("HowToIsShowed") != true) {
                        tb.putBoolean("HowToIsShowed", true);
                        Intent howtoIntent = new Intent(context, HowToActivity.class);
                        startActivity(howtoIntent);
                        finish();
                    } else {


                        Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(mainIntent);
                        finish();
                    }}
                   else if(isConnected){
                        Toast.makeText(context, getResources().getString(R.string.loading), Toast.LENGTH_SHORT).show();
                    }
                    else {
                    Toast.makeText(context, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                }



            }
        });
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }

        return false;

    }
    @Override
    protected void onResume() {
        super.onResume();
        makeFullScreen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //helpful utilities
    private void addLaunchTimes() {
        int i = pf.getLaunchTimes();
        i++;
        pf.saveSettings(TAG_LAUNCH_TIMES, i);
    }



    private void deleteZip(String... filesArray) {
        for (String fileName : filesArray) {
            File file = new File(fileName);
            if (file.exists())
                file.delete();
        }
    }



    //interface
    private void addShortcut() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int size = 0;
        if (activityManager != null) {
            size = activityManager.getLauncherLargeIconSize();
        }
        try {
            Drawable icon = getPackageManager().getApplicationIcon(getPackageName());
            Bitmap shortcutIconBitmap = ((BitmapDrawable) icon).getBitmap();
            Bitmap temp;
            if (shortcutIconBitmap.getWidth() == size && shortcutIconBitmap.getHeight() == size)
                temp = shortcutIconBitmap;
            else
                temp = Bitmap.createScaledBitmap(shortcutIconBitmap, size, size, true);
           // pf.saveSettings(TAG_SHORTCUT_CREATED, false);
            Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent addIntent = new Intent();
            addIntent.putExtra("duplicate", false);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, temp);

            getApplicationContext().sendBroadcast(addIntent);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    private void addImageView(int pos) {
        int marginTop = pos == 0 ? 48 : 288;
        RelativeLayout rl = findViewById(R.id.activity_main);
        iv = new ImageView(this);
        iv.setBackgroundResource(R.drawable.logo);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.setMargins(0, marginTop, 0, 0);
        iv.requestLayout();
        iv.setLayoutParams(lp);
        rl.addView(iv);
    }





    public void makeFullScreen() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            makeFullScreen();
        }
    }





    //permission block








    //game logic
    private void checkRateDialog() {
        if (RateMe.shouldShowRateDialog()) {
           // hideViews();
           // RateMe.showRateDialog();
          //  RateMe.setListener(this);
        } else {
            // startBillingActivity();
           // startNative();
        }
    }

    @Override
    public void isShowUpdateDialog(boolean flag) {
        if (flag) {
            versionManager.showDialog();
            //versionManager.setListener(this);
        } else {
            checkRateDialog();
        }
    }

    private void checkUrlVersion() {
        //  versionManager = new WVersionManager(this);
        //versionManager.setVersionContentUrl(UPDATE_LINK);
        // versionManager.checkVersion();

    }



    /*private void startBillingActivity() {
        Intent intent = new Intent(this, BillingActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }*/



    private boolean isGdprSubject() {
        String locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            locale = getResources().getConfiguration().locale.getCountry();
        }
        return Arrays.asList(EU_COUNTRIES).contains(locale.toUpperCase());
    }















}
