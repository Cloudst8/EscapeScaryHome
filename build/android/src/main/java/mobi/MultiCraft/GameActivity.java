package mobi.MultiCraft;

import android.app.NativeActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;


import java.io.Console;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends NativeActivity {
    static {
        System.loadLibrary("MultiCraft");
    }

    private int messageReturnCode;
    private String messageReturnValue;
    private int height, width;
    private boolean isAdsShowing;
    private String appKey = "b959914a0ec861468c259c89a503af8ef2874a8652435c7f";
    public static native void putMessageBoxResult(String text);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        height = bundle != null ? bundle.getInt("height", 0) : getResources().getDisplayMetrics().heightPixels;
        width = bundle != null ? bundle.getInt("width", 0) : getResources().getDisplayMetrics().widthPixels;
        // pf = PreferencesHelper.getInstance(this);

        YandexMetricaConfig.Builder configBuilder = YandexMetricaConfig.newConfigBuilder("9a8034c4-2269-44b5-92c4-690250064ca2");
        YandexMetrica.activate(getApplicationContext(), configBuilder.build());
        // Отслеживание активности пользователей
        YandexMetrica.enableActivityAutoTracking(getApplication());
Appodeal.setTesting(false);
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);

        Appodeal.isLoaded(Appodeal.INTERSTITIAL);
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean isPrecache) {

                Log.d("Appodeal", "onInterstitialLoaded");
            }
            @Override
            public void onInterstitialFailedToLoad() {

                Appodeal.initialize(GameActivity.this, appKey, Appodeal.INTERSTITIAL);
                Log.d("Appodeal", "onInterstitialFailedToLoad");
            }
            @Override
            public void onInterstitialShown() {
                isAdsShowing = true;
                Log.d("Appodeal", "onInterstitialShown");
            }
            @Override
            public void onInterstitialClicked() {

                Log.d("Appodeal", "onInterstitialClicked");
            }
            @Override
            public void onInterstitialClosed() {
                isAdsShowing = false;
                Appodeal.initialize(GameActivity.this, appKey, Appodeal.INTERSTITIAL);
                Log.d("Appodeal", "onInterstitialClosed");
            }
            public void onInterstitialExpired() {

                Appodeal.initialize(GameActivity.this, appKey, Appodeal.INTERSTITIAL);
                Log.d("Appodeal", "onInterstitialClosed");
            }
        });
        Timer timer = new Timer();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {

                                      @Override
                                      public void run() {
                                          Log.d("Timer", "Pow");
                                          if(Appodeal.isLoaded(Appodeal.INTERSTITIAL) && !isAdsShowing){
                                              Appodeal.show(GameActivity.this, Appodeal.INTERSTITIAL);
                                          }
                                      }
                                  },
                0, 120000);   // 1000 Millisecond  = 1 second
        // if (pf.isAdsEnabled()) setAdsCallbacks(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        messageReturnCode = -1;
        messageReturnValue = "";
    }



    public void makeFullScreen() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            makeFullScreen();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*if (pf.isAdsEnabled()) {
            stopAd();
            startAd(this, false);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeFullScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if (pf.isAdsEnabled())
            stopAd();*/
    }

    @Override
    public void onBackPressed() {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("text");
                messageReturnCode = 0;
                messageReturnValue = text;
            } else {
                messageReturnCode = 1;
            }
        }
    }

    public void copyAssets() {
    }

    public void showDialog(String acceptButton, String hint, String current, int editType) {
        Intent intent = new Intent(this, InputDialogActivity.class);
        Bundle params = new Bundle();
        params.putString("acceptButton", acceptButton);
        params.putString("hint", hint);
        params.putString("current", current);
        params.putInt("editType", editType);
        intent.putExtras(params);
        startActivityForResult(intent, 101);
        messageReturnValue = "";
        messageReturnCode = -1;
    }

    public int getDialogState() {
        return messageReturnCode;
    }

    public String getDialogValue() {
        messageReturnCode = -1;
        return messageReturnValue;
    }

    public float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    public int getDisplayHeight() {
        return height;
    }

    public int getDisplayWidth() {
        return width;
    }

}