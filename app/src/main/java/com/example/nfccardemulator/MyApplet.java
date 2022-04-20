/*
 * Copyright 2012 Licel LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.nfccardemulator;

import com.licel.jcardsim.samples.BaseApplet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.Cipher;

import javacard.framework.*;

import javacardx.apdu.ExtendedLength;

import static javacard.framework.ISO7816.INS_SELECT;

/**
 * Basic HelloWorld JavaCard Applet.
 * @author LICEL LLC
 */
public class MyApplet extends BaseApplet {

    private final static byte SEND_ENCRYPTED = (byte) 0x01;
    private final static byte SEND_ENCRYPTED2 = (byte) 0x10;
    static String publicKeyEncoded = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtJNu6qOiYMaRyIlFBHBkv5vVgBwX12oOHfBtUWXAvOtfb3WVJbVStMRJASEMh+fJOxR7TOKvIECQynaPpvXdQMFkNXFSKX0hRB96oTJBeeJrqHlM+07yO8R4ab00LaRAX84eP4S3gz1e44+QSQzgAxg3DlC29XTx2H/3Xl6CfpVPtRZKk2NrfkdJTK+Lrpw//eG7HSK0rPaUVqTjtdA2ElLQ76BsbG+oWhR0/3nBhefGEeLxVkWNANKoIRjbioFts/svFRIeDzUPbuMcKAdmxSjhZMTxvjVini7jg7VAoIGONJTh6DXHIH4EUF6dBq+CP+oPPeda/Fapv07IpaJ2yT46fwZaonYwWdRTSM2pyKxBhPugkL/2anlsX/2l3Noc8KARid0/McuXNnJgNJOJrt2s7SLlO/E8Ftr/q8d3+sCaJFvjbS1LRxneShKVTluZKVOFWWuzAA8Qd1rHukCABLSXTdBmPqCH1Kicbv43NkrSYZuaRyTwxNp27dEXSm+8CDLKXxY8wsnZmm7sm+sSJDdYSU1QQ9KtIHnbmOADT7Z4pA45IiK+CakYYwZSxunCSd+NL1O50RlYlExsdTkdUMe62x1i3R9Re00S+G+LdbFe2bR1cNfqTWiNJDNj4Zzx82tTS5/I7IORybPfl9/Rp0U2xE93ISUt2PNzd60oXRMCAwEAAQ==";
    static String token = "token";
    private static byte[] testingToken1 = new byte[]{0x01, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64};
    private static byte[] testingToken2 = new byte[]{0x02, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64};
    private static byte[] testingToken3 = new byte[]{0x03, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64};
    private static byte[] testingToken4 = new byte[]{0x04, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64};
    private static byte[] testingToken5 = new byte[]{0x05, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64};

    /**
     * Instruction: say hello
     */
    private final static byte SAY_HELLO_INS = (byte) 0x20;
    /**
     * Instruction: say echo v2
     */
    private final static byte SAY_ECHO2_INS = (byte) 0x03;
    /**
     * Instruction: get install params
     */
    private final static byte SAY_IPARAMS_INS = (byte) 0x04;
    /**
     * Instruction: NOP
     */
    private final static byte NOP_INS = (byte) 0x02;
    /**
     * Instruction: queue data and return 61xx
     */
    private final static byte SAY_CONTINUE_INS = (byte) 0x06;
    /**
     * Instruction: CKYListObjects (http://pki.fedoraproject.org/images/7/7a/CoolKeyApplet.pdf 2.6.17)
     */
    private final static byte LIST_OBJECTS_INS = (byte) 0x58;
    /**
     * Instruction: "Hello Java Card world!" + Application Specific SW 9XYZ
     */
    private final static byte APPLICATION_SPECIFIC_SW_INS = (byte) 0x7;
    /**
     * Instruction: return maximum data.
     */
    private final static byte MAXIMUM_DATA_INS = (byte) 0x8;
    /**
     * Byte array representing "Hello Java Card world!" string.
     */
    private static byte[] helloMessage = new byte[]{
            0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, // "Hello "
            0x77, 0x6F, 0x72, 0x6C, 0x64, 0x20, 0x21 // "world !"
    };
    //second half of encrypted message to send
    private byte[] responseArray2;

    private byte[] echoBytes;
    private byte[] initParamsBytes;
    private byte[] currentEF;
    private final byte[] transientMemory;
    private static final short LENGTH_ECHO_BYTES = 256;

    /**
     * Only this class's install method should create the applet object.
     * @param bArray the array containing installation parameters
     * @param bOffset the starting offset in bArray
     * @param bLength the length in bytes of the parameter data in bArray
     */
    protected MyApplet(byte[] bArray, short bOffset, byte bLength) {
        echoBytes = new byte[LENGTH_ECHO_BYTES];
        currentEF = new byte[LENGTH_ECHO_BYTES];
        if (bLength > 0) {
            byte iLen = bArray[bOffset]; // aid length
            bOffset = (short) (bOffset + iLen + 1);
            byte cLen = bArray[bOffset]; // info length
            bOffset = (short) (bOffset + 3);
            byte aLen = bArray[bOffset]; // applet data length
            initParamsBytes = new byte[aLen];
            Util.arrayCopyNonAtomic(bArray, (short) (bOffset + 1), initParamsBytes, (short) 0, aLen);
        }
        transientMemory = JCSystem.makeTransientByteArray(LENGTH_ECHO_BYTES, JCSystem.CLEAR_ON_RESET);
        register();
    }

    /**
     * This method is called once during applet instantiation process.
     * @param bArray the array containing installation parameters
     * @param bOffset the starting offset in bArray
     * @param bLength the length in bytes of the parameter data in bArray
     * @throws ISOException if the install method failed
     */
    public static void install(byte[] bArray, short bOffset, byte bLength)
            throws ISOException {
        new MyApplet(bArray, bOffset,bLength);
    }

    /**
     * This method is called each time the applet receives APDU.
     */

    public void process(APDU apdu) {
        // good practice
        if(selectingApplet()) return;
        byte[] buffer = apdu.getBuffer();
        // Now determine the requested instruction:
        switch (buffer[ISO7816.OFFSET_INS]) {
            case SAY_HELLO_INS:
                sayHello(apdu, (short)0x9000);
                return;
            case SEND_ENCRYPTED:
                try {
                    sendEncrypted(apdu, (short)0x9000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            case SEND_ENCRYPTED2:
                try {
                    sendSecondByteArray(apdu, (short)0x9000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            case SAY_ECHO2_INS:
                sayEcho2(apdu);
                return;
            case SAY_IPARAMS_INS:
                sayIParams(apdu);
                return;
            case SAY_CONTINUE_INS:
                sayContinue(apdu);
                return;
            case LIST_OBJECTS_INS:
                listObjects(apdu);
                return;
            case APPLICATION_SPECIFIC_SW_INS:
                sayHello(apdu, (short)0x9B00);
                return;
            case MAXIMUM_DATA_INS:
                maximumData(apdu);
                return;
            case NOP_INS:
                return;
            case INS_SELECT:
                selectEF(apdu);
            default:
                // We do nsot support any other INS values
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
    // sends second message of encrypted array
    private void sendSecondByteArray(APDU apdu, short sw) throws Exception {
        //Communication part

        // Here all bytes of the APDU are stored
        byte[] buffer = apdu.getBuffer();
        // receive all bytes
        // if P1 = 0x10 (echo)
        short incomeBytes = apdu.setIncomingAndReceive();
        byte[] echo = transientMemory;
        short echoLength;

        if (buffer[ISO7816.OFFSET_P1] == 0x01) {
            echoLength = incomeBytes;
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echo, (short) 0, incomeBytes);
        } else {
            echoLength = (short) responseArray2.length;
            Util.arrayCopyNonAtomic(responseArray2, (short) 0, echo, (short) 0, (short) responseArray2.length);
        }
        // Tell JVM that we will send data
        apdu.setOutgoing();
        // Set the length of data to send
        apdu.setOutgoingLength(echoLength);
        // Send our message starting at 0 position
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        // Set application specific sw
        if(sw!=0x9000) {
            ISOException.throwIt(sw);
        }
    }
    private void sendEncrypted(APDU apdu, short sw) throws Exception {
        //Encryption part
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyEncoded);
        System.out.println("decoded byte array length: " + decodedPublicKey.length);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                decodedPublicKey);
        PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);
        //RSAPublicKeySpec publicKeySpec2 = keyFactory.getKeySpec(publicKey2, RSAPublicKeySpec.class);

        // Here all bytes of the APDU are stored
        byte[] buffer = apdu.getBuffer();
        // receive all bytes
        // if P1 = 0x10 (echo)
        short incomeBytes = apdu.setIncomingAndReceive();
        // take incoming data bytes and send them to encrypt method in order for them to be added to cipher
        byte[] incomingDataFromCAPDU = new byte[incomeBytes];
        System.arraycopy(buffer, 5, incomingDataFromCAPDU, 0, incomingDataFromCAPDU.length);

        byte[] cipherTextArray = encrypt(incomingDataFromCAPDU, testingToken1, publicKey2); //cipherTextArray je pole bytu, obsahujici zasifrovany token, je potreba rozpulit na 2x 256B
        byte[] responseArray1 = Arrays.copyOfRange(cipherTextArray, 0, cipherTextArray.length/2);
        responseArray2 = Arrays.copyOfRange(cipherTextArray, cipherTextArray.length/2, cipherTextArray.length);

        String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        System.out.println("Encrypted Text : " + encryptedText);
        byte[] textBytes = Base64.getDecoder().decode(encryptedText);
        System.out.println("Encrypted Text bytes count: " + textBytes.length);
        //


        //Communication part



        byte[] echo = transientMemory;
        short echoLength;

        if (buffer[ISO7816.OFFSET_P1] == 0x01) {
            echoLength = incomeBytes;
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echo, (short) 0, incomeBytes);
        } else {
            echoLength = (short) responseArray1.length;
            Util.arrayCopyNonAtomic(responseArray1, (short) 0, echo, (short) 0, (short) responseArray1.length);
        }
        // Tell JVM that we will send data
        apdu.setOutgoing();
        // Set the length of data to send
        apdu.setOutgoingLength(echoLength);
        // Send our message starting at 0 position
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        // Set application specific sw
        if(sw!=0x9000) {
            ISOException.throwIt(sw);
        }

        // reference HelloWorld communication below
        // delete after completing my own coms
        /*
        // Here all bytes of the APDU are stored
        byte[] buffer = apdu.getBuffer();
        // receive all bytes
        // if P1 = 0x10 (echo)
        short incomeBytes = apdu.setIncomingAndReceive();
        byte[] echo = transientMemory;
        short echoLength;
        if (buffer[ISO7816.OFFSET_P1] == 0x10) {
            echoLength = incomeBytes;
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echo, (short) 0, incomeBytes);
        } else {
            echoLength = (short) helloMessage.length;
            Util.arrayCopyNonAtomic(helloMessage, (short) 0, echo, (short) 0, (short) helloMessage.length);
        }
        // Tell JVM that we will send data
        apdu.setOutgoing();
        // Set the length of data to send
        apdu.setOutgoingLength(echoLength);
        // Send our message starting at 0 position
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        // Set application specific sw
        /*if(sw!=0x9000) {
            ISOException.throwIt(sw);
        }*/


    }

    public static byte[] encrypt(byte[] incomingBytes, byte[] testingToken, PublicKey publicKey2) throws Exception
    {

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, publicKey2);

        //byte[] a = plainText.getBytes();
        byte[] finalArray = new byte[incomingBytes.length + testingToken1.length];
        System.arraycopy(incomingBytes, 0, finalArray, 0, incomingBytes.length);
        System.arraycopy(testingToken, 0, finalArray, incomingBytes.length, testingToken.length);



        // Perform Encryption
        byte[] cipherText = cipher.doFinal(finalArray);

        return cipherText;
    }

    private void ContinueInParts(APDU apdu) {
        byte[] echo = transientMemory;
        short echoLength = (short) 6;
        Util.arrayCopyNonAtomic(helloMessage, (short)0, echo, (short)0, (short)6);
        apdu.setOutgoing();
        apdu.setOutgoingLength(echoLength);
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        ISOException.throwIt((short) (ISO7816.SW_BYTES_REMAINING_00 | 0x07));
    }

    /**
     * send some hello data, and indicate there's more
     */

    private void sayContinue(APDU apdu) {
        byte[] echo = transientMemory;
        short echoLength = (short) 6;
        Util.arrayCopyNonAtomic(helloMessage, (short)0, echo, (short)0, (short)6);
        apdu.setOutgoing();
        apdu.setOutgoingLength(echoLength);
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        ISOException.throwIt((short) (ISO7816.SW_BYTES_REMAINING_00 | 0x07));
    }

    private void selectEF(APDU apdu) {
        // select
        byte buffer[] = apdu.getBuffer();
        currentEF[0] = buffer[ISO7816.OFFSET_CDATA+1];

        ISOException.throwIt(ISO7816.SW_NO_ERROR);
    }

    /**
     * Sends hello message to host using given APDU.
     *
     * @param apdu APDU that requested hello message
     * @param sw response sw code
     */

    private void sayHello(APDU apdu, short sw) {
        // Here all bytes of the APDU are stored
        byte[] buffer = apdu.getBuffer();
        // receive all bytes
        // if P1 = 0x01 (echo)
        short incomeBytes = apdu.setIncomingAndReceive();
        byte[] echo = transientMemory;
        short echoLength;
        if (buffer[ISO7816.OFFSET_P1] == 0x01) {
            echoLength = incomeBytes;
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echo, (short) 0, incomeBytes);
        } else {
            echoLength = (short) helloMessage.length;
            Util.arrayCopyNonAtomic(helloMessage, (short) 0, echo, (short) 0, (short) helloMessage.length);
        }
        // Tell JVM that we will send data
        apdu.setOutgoing();
        // Set the length of data to send
        apdu.setOutgoingLength(echoLength);
        // Send our message starting at 0 position
        apdu.sendBytesLong(echo, (short) 0, echoLength);
        // Set application specific sw
        if(sw!=0x9000) {
            ISOException.throwIt(sw);
        }


    }


    /**
     * echo v2
     */

    private void sayEcho2(APDU apdu) {
        byte buffer[] = apdu.getBuffer();

        short bytesRead = apdu.setIncomingAndReceive();
        short echoOffset = (short) 0;

        while (bytesRead > 0) {
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, echoBytes, echoOffset, bytesRead);
            echoOffset += bytesRead;
            bytesRead = apdu.receiveBytes(ISO7816.OFFSET_CDATA);
        }

        apdu.setOutgoing();
        apdu.setOutgoingLength(echoOffset);
        // echo data
        apdu.sendBytesLong(echoBytes, (short) 0, echoOffset);

    }

    /**
     * echo install params
     */
    private void sayIParams(APDU apdu) {
        apdu.setOutgoing();
        apdu.setOutgoingLength((short)initParamsBytes.length);
        // echo install parmas
        apdu.sendBytesLong(initParamsBytes, (short) 0, (short)initParamsBytes.length);
    }




    /**
     * send the maximum amount of data the apdu will accept
     *
     * @param apdu APDU that requested hello message
     */

    private void maximumData(APDU apdu) {
        short maxData = APDU.getOutBlockSize();
        byte[] buffer = apdu.getBuffer();
        Util.arrayFillNonAtomic(buffer, (short) 0, maxData, (byte) 0);
        apdu.setOutgoingAndSend((short) 0, maxData);
    }

    // prototype

    private void listObjects(APDU apdu)
    {
        byte buffer[] = apdu.getBuffer();

        if (buffer[ISO7816.OFFSET_P2] != 0) {
            ISOException.throwIt((short)0x9C11);
        }

        byte expectedBytes = buffer[ISO7816.OFFSET_LC];

        if (expectedBytes < 14) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        ISOException.throwIt((short)0x9C12);
    }
}