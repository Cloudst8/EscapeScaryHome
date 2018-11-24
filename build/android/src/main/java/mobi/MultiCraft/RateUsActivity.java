package mobi.MultiCraft;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RatingBar;

import com.appodeal.ads.Appodeal;

public class RateUsActivity extends AppCompatActivity {
private RatingBar rb;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_rateus);
        String appKey = "b959914a0ec861468c259c89a503af8ef2874a8652435c7f";
        //Appodeal.disableLocationPermissionCheck();
        Appodeal.setTesting(true);
        Appodeal.setBannerViewId(R.id.appodeal_banner_view);
        Appodeal.setAutoCache(Appodeal.NATIVE, true);
        Appodeal.setMrecViewId(R.id.appodealMrecView);

        Appodeal.initialize(this, appKey,
                Appodeal.BANNER);
        Appodeal.initialize(this, appKey,
                Appodeal.MREC);
        Appodeal.show(this, Appodeal.MREC);
        Appodeal.show(this, Appodeal.BANNER);
        rb = (RatingBar) findViewById(R.id.ratingBar);
rb.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.scary.home.escape.craft"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
});
    }
}
