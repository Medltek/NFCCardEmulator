package com.example.nfccardemulator;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Trace;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static com.example.nfccardemulator.EmulatorSingleton.TAG;

public class APDUService extends HostApduService {


    @Override
    public void onCreate() {
        super.onCreate();
        EmulatorSingleton.createEmulator(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    long startTime;
    int j = 0;
    @Override
    public byte[] processCommandApdu(byte[] capdu, Bundle extras) {

        try {
            Log.d(TAG, "processCommandApdu called with: parameter: " + capdu);
            return EmulatorSingleton.process(this, capdu);
        } finally {
            if(j==0){startTime = System.currentTimeMillis();}
            j=j+1;
            if(j==3){
                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime);
                System.out.format("Milli = %s, ( S_Start : %s, S_End : %s ) \n", duration, startTime, endTime);
                System.out.println("Human-Readable format : "  + millisToShortDHMS(duration)) ;}
        }
    }

    public static String millisToShortDHMS(long duration) {
        String res = "";    // java.util.concurrent.TimeUnit;
        long days       = TimeUnit.MILLISECONDS.toDays(duration);
        long hours      = TimeUnit.MILLISECONDS.toHours(duration) -
                TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes    = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds    = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        long millis     = TimeUnit.MILLISECONDS.toMillis(duration) -
                TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(duration));

        if (days == 0)      res = String.format("%02d:%02d:%02d.%04d", hours, minutes, seconds, millis);
        else                res = String.format("%dd %02d:%02d:%02d.%04d", days, hours, minutes, seconds, millis);
        return res;
    }

    @Override
    public void onDeactivated(int reason) {

        Intent i = new Intent(EmulatorSingleton.TAG);
        j = 0;
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

