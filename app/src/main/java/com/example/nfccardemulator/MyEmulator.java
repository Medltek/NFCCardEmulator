package com.example.nfccardemulator;

import android.content.Context;
import android.content.Intent;
import com.licel.jcardsim.base.Simulator;
import com.licel.jcardsim.base.SimulatorRuntime;
import com.licel.jcardsim.utils.APDUScriptTool;
import javacard.framework.*;
import com.licel.jcardsim.utils.*;

public class MyEmulator implements Emulator {

    private static Simulator simulator = null;

public MyEmulator(Context context) {

    String aid="F07465737420414944", name= "MyApplet", extra_install = "", extra_error = "";

    try {
        AID appletAID = AIDUtil.create(aid);
        //simulator.installApplet(appletAID, MyApplet.class);
        byte[] aid_bytes = Utils.hexStringToByteArray(aid);
        byte[] inst_params = new byte[aid.length() + 1];
        inst_params[0] = (byte) aid_bytes.length;
        System.arraycopy(aid_bytes, 0, inst_params, 1, aid_bytes.length);
        simulator.installApplet(AIDUtil.create(aid), MyApplet.class, inst_params, (short) 0, (byte) inst_params.length);
        extra_install += "\n" + name + " (AID: " + aid + ")";
    } catch (Exception e) {
        e.printStackTrace();
        extra_error += "\n" + "Could not install " + name + " (AID: " + aid + ")";
    }

    Intent i = new Intent(EmulatorSingleton.TAG);
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
        return simulator.transmitCommand(commandAPDU);
    }

    public void deactivate() {
        simulator.reset();
    }

}
