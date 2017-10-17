package mad9132.planets;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import mad9132.planets.services.MyService;
import mad9132.planets.utils.NetworkHelper;

/**
 * Set permissions and check the connection.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @author David Gassner (original)
 *
 * Reference: Chapter 3. Requesting Data over the Web
 *            "Android App Development: RESTful Web Services" with David Gassner
 */
public class MainActivity extends Activity {

    private static final Boolean IS_LOCALHOST;
    private static final String JSON_URL;

    private boolean networkOk;
    private TextView output;

    static {
        IS_LOCALHOST = false;
        JSON_URL = IS_LOCALHOST ? "http://10.0.2.2:3000/planets" : "https://planets.mybluemix.net/planets";
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            output.append(message + "\n");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        output.append("Network OK: " + networkOk + "\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setData(Uri.parse(JSON_URL));
        startService(intent);
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
