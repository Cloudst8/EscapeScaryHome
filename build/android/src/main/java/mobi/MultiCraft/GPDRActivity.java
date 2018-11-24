package mobi.MultiCraft;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.appodeal.ads.Appodeal;

public class GPDRActivity extends AppCompatActivity {
    private Button btnYes;
    private Button btnNo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_gpdr);
        String appKey = "b959914a0ec861468c259c89a503af8ef2874a8652435c7f";
        //Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(true);
        Appodeal.setBannerViewId(R.id.appodeal_banner_view);



        Appodeal.initialize(this, appKey,
                Appodeal.BANNER);


        Appodeal.show(this, Appodeal.BANNER);
        btnYes = (Button) findViewById(R.id.yes);
        btnNo = (Button) findViewById(R.id.no);
        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AboutUs.tb.putBoolean("GPDRapplied", true);
                finish();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                System.exit(0);
                // THIS HANDLER WORKS FINE

            }
        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
