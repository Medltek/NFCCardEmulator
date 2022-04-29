package com.example.nfccardemulator;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobServiceEngine;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.os.Trace;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.Arrays;

import static android.os.Debug.startMethodTracing;
import static android.os.Debug.stopMethodTracing;
import static com.example.nfccardemulator.EmulatorSingleton.TAG;

public class MyHostApduService extends HostApduService {


    @Override
    public void onCreate() {
        Trace.beginSection("Create");
        super.onCreate();
        EmulatorSingleton.createEmulator(this);
        Trace.endSection();

    }

    @Override
    public void onDestroy() {


        super.onDestroy();


    }

    @Override
    public byte[] processCommandApdu(byte[] capdu, Bundle extras) {

            Log.d(TAG, "processCommandApdu called with: parameter: " + capdu);
            return EmulatorSingleton.process(this, capdu);


    }

    @Override
    public void onDeactivated(int reason) {

        Intent i = new Intent(EmulatorSingleton.TAG);

        Log.d("", "End transaction");
        switch (reason) {
            case DEACTIVATION_LINK_LOSS:
                i.putExtra(EmulatorSingleton.EXTRA_DESELECT, "link lost");
                EmulatorSingleton.deactivate();
                break;
            case DEACTIVATION_DESELECTED:
                EmulatorSingleton.deactivate();
                break;
        }


    }

}

