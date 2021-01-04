package com.example.nfccardemulator;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import static com.example.nfccardemulator.EmulatorSingleton.TAG;

public class MyHostApduService extends HostApduService {

    @Override
    public void onCreate() {
        super.onCreate();
        EmulatorSingleton.createEmulator(this);
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

