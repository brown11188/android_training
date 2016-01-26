package training.com.chatapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import config.AppConfig;

public class UpstreamActivity extends AppCompatActivity {

    Button btn_send_message;

    AtomicInteger msgId = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upstream);

        final Activity activity = this;
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        final String senderId = AppConfig.SENDER_ID;
        final String id = Integer.toString(msgId.incrementAndGet());
        final Bundle data  = new Bundle();
        data.putString("message","HELLO");
        btn_send_message = (Button) findViewById(R.id.btn_send);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gcm.send(senderId + "@gcm.googleapis.com", id, data );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
