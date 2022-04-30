package com.example.nfccardemulator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.licel.jcardsim.base.Simulator;

import javacard.framework.AID;

//import com.example.nfccardemulator.EmulatorSingleton.TAG;

public class CardEmulator implements Emulator {

    public static final String TAG = "com.example.nfccardemulator";

    private static Simulator simulator = null;

    private AID appletAID;

public CardEmulator(Context context) {
    Log.d(TAG, "CardEmulator: Started");

    //VSBAccessApplet installation
    String aid="F000000001", name= "VSBAccessApplet", extra_install = "", extra_error = "";
    simulator = new Simulator();
    try {
        byte[] aid_bytes = ByteUtil.hexStringToByteArray(aid);
        byte[] inst_params = new byte[aid.length() + 1];
        inst_params[0] = (byte) aid_bytes.length;
        System.arraycopy(aid_bytes, 0, inst_params, 1, aid_bytes.length);
        appletAID = simulator.installApplet(AIDUtil.create(aid), VSBAccessApplet.class ,inst_params, (short) 0, (byte) inst_params.length);
        extra_install += "\n" + name + " (AID: " + aid + ")";
        Log.d(TAG, "CardEmulator: Applet Installation Successful" + " " + aid);
    } catch (Exception e) {
        Log.d(TAG, "CardEmulator: Applet Installation failed");
        e.printStackTrace();
        extra_error += "\n" + "Could not install " + name + " (AID: " + aid + ")";
    }

    Intent i = new Intent(TAG);
    if (!extra_error.isEmpty())
        i.putExtra(EmulatorSingleton.EXTRA_ERROR, extra_error);
    if (!extra_install.isEmpty())
        i.putExtra(EmulatorSingleton.EXTRA_INSTALL, extra_install);
}


    public void destroy() {
        if (simulator != null) {
            simulator.reset();
            simulator = null;
        }
    }

    public byte[] process(byte[] commandAPDU) {
        //byte[] response = simulator.transmitCommand(new byte[]{0,2,0,0});
       // ByteUtil.requireSW(response, 0x9000);

        simulator.selectApplet(appletAID);
        return simulator.transmitCommand(commandAPDU);
    }

    public void deactivate() {

    simulator.reset();
    }

}
