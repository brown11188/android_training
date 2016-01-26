package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by enclaveit on 1/26/16.
 */
public class MessageBroadcastReceiver extends BroadcastReceiver  {
    @Override
    public void onReceive(Context context, Intent intent) {
        String content = "Broadcast intent detected "
                + intent.getAction();
        String message = intent.getExtras().getString("message");
        Log.i("Broadcast Receiver"," " + message);
    }
}
