package com.example.nfccardemulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmulatorSingleton {
    public static final String TAG = "com.example.nfccardemulator";
    public static final String EXTRA_CAPDU = "MSG_CAPDU";
    public static final String EXTRA_RAPDU = "MSG_RAPDU";
    public static final String EXTRA_ERROR = "MSG_ERROR";
    public static final String EXTRA_DESELECT = "MSG_DESELECT";
    public static final String EXTRA_INSTALL = "MSG_INSTALL";

    private static Emulator emulator = null;
    private static boolean do_destroy = false;


    private static void destroyEmulatorIfRequested(Context context) {
        if (do_destroy) {
            emulator.destroy();
            emulator = null;
            do_destroy = false;
            Intent i = new Intent(TAG);
            i.putExtra(EXTRA_DESELECT, "Uninstalled all applets.");

        }
    }
    public static void destroyEmulator() {
        do_destroy = true;
    }


    public static void createEmulator(Context context) {
        // force reloading applets if requested
        destroyEmulatorIfRequested(context);

        if (emulator == null) {
            Log.d("", "Begin transaction");
            SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
            String str_emulator = SP.getString("emulator", "");
            emulator = new MyEmulator(context);
        }
    }
    public static byte[] process(Context context, byte[] capdu) {
        byte[] rapdu = emulator.process(capdu);

        Intent i = new Intent(TAG);
        i.putExtra(EXTRA_CAPDU, Utils.byteArrayToHexString(capdu));
        if (rapdu != null)
            i.putExtra(EXTRA_RAPDU, Utils.byteArrayToHexString(rapdu));


        return rapdu;
    }
    public static String[] getRegisteredAids(Context context) {
        List<String> aidList = new ArrayList<>();
        XmlResourceParser aidXmlParser = context.getResources().getXml(R.xml.aid_list);

        try {
            while (aidXmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (aidXmlParser.getEventType() == XmlPullParser.START_TAG) {
                    if (aidXmlParser.getName().equals("aid-filter")) {
                        int aid = aidXmlParser.getAttributeResourceValue(0, -1);
                        if (aid != -1) {
                            aidList.add(context.getResources().getString(aid));
                        }
                    }
                }

                aidXmlParser.next();
            }

            aidXmlParser.close();
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG.substring(0, 23), "Couldn't parse aid xml list.");
        }

        return aidList.toArray(new String[0]);
    }
    public static void deactivate() {
        emulator.deactivate();
    }

}