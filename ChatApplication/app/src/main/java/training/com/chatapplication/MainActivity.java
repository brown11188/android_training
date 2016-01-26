package training.com.chatapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import config.AppConfig;
import content.GCMContent;
import services.APICallingService;

public class MainActivity extends AppCompatActivity {

    Button btn_intent, btn_send;

    String PROJECT_NUMBER = "396598309792";
    private APICallingService services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_intent = (Button) findViewById(R.id.btn_intent);
        btn_send = (Button) findViewById(R.id.btn_send);
        services = new APICallingService();
        final GCMContent content = createContent();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btn_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpstreamActivity.class));
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    services.sendPostMessage(AppConfig.API_KEY, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d("Registration id", registrationId);
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    public GCMContent createContent() {
        GCMContent content = new GCMContent();
        content.addRegId(AppConfig.REGISTRATION_ID);
        content.createData("This is my title","Hello world !!!");
        return content;
    }



}
