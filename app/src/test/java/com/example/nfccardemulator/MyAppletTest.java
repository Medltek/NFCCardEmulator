package com.example.nfccardemulator;

import android.annotation.SuppressLint;
import android.content.Context;

import com.licel.jcardsim.base.Simulator;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;

import javacard.framework.ISOException;


public class MyAppletTest {

    //final byte[] testingToken = new byte[]{0x56, 0x65, 0x72, 0x69, 0x66, 0x69, 0x63, 0x61, 0x74, 0x69, 0x6F, 0x6E, 0x20, 0x74, 0x6F, 0x6B, 0x65, 0x6E, 0x20, 0x6F, 0x66, 0x20, 0x73, 0x74, 0x75, 0x64, 0x65, 0x6E, 0x74, 0x20, 0x6E, 0x31};

    static String testingToken = "Testovaci token";

    final String publicKeyEncoded = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAw53Xu3mKeN1Uoigb4bFVdBd05nKUvTVnQrS50SQ9PPXa1bLq8SYdJv8" +
            "uQqO4D/TqCOjq6mLOn0rr4t5xI0VpErJXiP6sXG7ivQuAzhg9eIIulQhoBXuNfZO8Oh8KqxvlWZTuByKGWJ4vTFSL7IzPGrMI3Ayq061APjAMguLTG92W1fSrSHl" +
            "SEeWwHauB3/ao52pgfJ29vguBJZZiYe5RV57chLI7870TpdZNv4B4af2GknfXcAGfFzyCBwNWNZFHwQZQnzlH3Uh4tjymXB77ZbVJPha4sG3Uag30tZI0RflGEmC" +
            "j9m85n+8fQEI8HNOfri5qyIlASRSguLkYFp4zGtf54KidRTcMeq/Gv4MrKj+6N5p+Cy9mhuUCM9xAjQXCQJmWLhYmxy3MZl5aa80Ph17HEQ8om8Sh5DxydZdmfPjm" +
            "jnQNbSVHnnKwM79deC0uxxPTEbc5A0dQaFNtDCTT5lp3SmGEiw66mg1GGhz1Ws9GG8yDnn9lzk4P3lpoPU6Xh8gYFeEP3hL8qphKi4fAQNYzjwcCL+08dU3PvqXT" +
            "5i6ZyyZil6HioD0gx" +
            "OHh3X25cl7tKSxnxvaEVzi7SZ+ctLyK2J9nuifDCuEJ0Ej19TBUgeun5La/5f3RHUJXVMSe7HgUiOyi0Zk0CDTEz2JWLT8HFNxQgaIBc+087ECpHQsCAwEAAQ==";
    final String privateKeyEncoded = "MIIJQwIBADANBgkqhkiG9w0BAQEFAASCCS0wggkpAgEAAoICAQDDnde7eYp43VSiKBvhsVV0F3TmcpS9NWdCtLnRJD089dr" +
            "VsurxJh0m/y5Co7gP9OoI6OrqYs6fSuvi3nEjRWkSsleI/qxcbuK9C4DOGD14gi6VCGgFe419k7w6HwqrG+VZlO4HIoZYni9MVIvsjM8aswjcDKrTrUA+MA" +
            "yC4tMb3ZbV9KtIeVIR5bAdq4Hf9qjnamB8nb2+C4EllmJh7lFXntyEsjvzvROl1k2/gHhp/YaSd9dwAZ8XPIIHA1Y1kUfBBlCfOUfdSHi2PKZcHvtltUk+F" +
            "riwbdRqDfS1kjRF+UYSYKP2bzmf7x9AQjwc05+uLmrIiUBJFKC4uRgWnjMa1/ngqJ1FNwx6r8a/gysqP7o3mn4LL2aG5QIz3ECNBcJAmZYuFibHLcxmXlprz" +
            "Q+HXscRDyibxKHkPHJ1l2Z8+OaOdA1tJUeecrAzv114LS7HE9MRtzkDR1BoU20MJNPmWndKYYSLDrqaDUYaHPVaz0YbzIOef2XOTg/eWmg9TpeHyBgV4Q/eE" +
            "vyqmEqLh8BA1jOPBwIv7Tx1Tc++pdPmLpnLJmKXoeKgPSDE4eHdfblyXu0pLGfG9oRXOLtJn5y0vIrYn2e6J8MK4QnQSPX1MFSB66fktr/l/dEdQldUxJ7se" +
            "BSI7KLRmTQINMTPYlYtPwcU3FCBogFz7TzsQKkdCwIDAQABAoICAHTL17UuGomTzSaMwCj/UsBgCQJXQ4u9SQxBD/lZo960BJc3nqOhWCKiliJf0lRkrR/ow" +
            "ep0WF4vw6vPmMmwdc4eAbngJVWDN4ocRinHu1wqjlmZjVJItPBXtzkUs6/6lfPowj4sVP6wZBk1Gd6BLF8Kqq2IOq7scMEwuyei+t1WbusMjQBWv7PD6mWss3" +
            "zfr3WMxFNjc5j3EYM5yUAjpRml1pC8QqODLyoDTKSctkjCgQkO2OkdbrnW/OPnECAnwCn19Uk8UDZFQR2+FK5IKV5O4Y5azqc4zG0OLCAFTYTCEx6B6SQIwOV" +
            "HRud9JJspl9vOBx/5IxL/Z/8jNMi8aFIBcPvbDtOSR10giFSmKnEV+LsdzKuboY8Mwk8erzIPjFHQNVowrugl0b/HrAJ72HX0OVaqn4lkThy26vsvwX8BOusp" +
            "dBQjVi7vLpG7oG31ghJLoqRUzrI7B+uOTIk0tylH04yCib2t/XS8aHLt4K3aUIFDI+ltKhab5lD/E6UKEpIf+INYNdmQ7AkF5C2fKDjEpobDsF+t0gxHMtpks" +
            "oSiSqNxtD30xF/LU/4/jEMCuLzqFnsg4o8/6eCW6z0sKnquL43g27OpdDVFPvs5c4vC8OgYgaogFQLJH2D7jPPjM51Z1U/ts4teP/z7CBLrF3ejBuQ/MJWMG0" +
            "6hEFWKVvYhAoIBAQDmIjJcmo8kJt7ubQg9+1q535BxmPs80smRC2r7T0GcdgguwOVKKQjFsbkx1BwHCt0XnP3fD1XxrAren1gbOyPCU4Ou2K4Xr0nhKnTakvr" +
            "G49RAXrrTBVYPnWn2NI4n23teVQkoTUULmvANwu3Rp246UVKHTWCeqEZRu308Q0c1KwoJ8m17OTk1PfbfehSBtfMufEe1t8mGHLNZcjXcLyd2rhD2DvJYeO/Q" +
            "5M1gZR0wre/aUQsrr9fBPEc1V27eS26c4Di8tcHnwMRrWlM7BRmnfEEDW3cnBhcxbvHHcwSBEgv8o74enqStbbFfrGQ1n4jBR6xJ/hUt8sdnEdXKTAppAoIBA" +
            "QDZmnZwQ3Z2X3gWXVUlSt/c4YPoCS384VBKwbOH/c/IgwXXiEQPW0ey0WET5pz5OaIEYp7nx2OocE0UcsJzYpYuwBOgTKQ1FQ0GrAnNPNa6fe1pMDikuMeE9nQ" +
            "z2LqxRgx+nvmSXA2HHRTma+ZeM3thbT7nNFqUFLcdzpi+i07oAX9If9dd42cneqz1KmleHRfihc9AfuLtdx9vj/29nOKyHbL2vlbxVpuvh5k4pF4e778o4LLbd" +
            "KO0+s7OucfJUH8TmdmFPBZWR5sReA8gw7iLDTnskNEmX9UeCxdZkNnBd3DSaNadDqY0EkLRw9cWtzoBbXOMi9WBR758n5NtyjVTAoIBAQDenY3iDpERA5j7QvWK" +
            "H69vxlfPy5CdEbpyWH0whUaOt47Ky0YCKxeVRYpELU6/cT7WLMNYF1mn4ijRmYp9LQQ2YtsrM+eUebgZ3BwVo8B6zMDxU7Gdl7fcewWZaNVqsOHyKoglODnmNAa" +
            "rCTlMaY4zU59lyvAACf+b6KYIuXwUboHThXNqk7xHSJ2bKFjV8NBA02/61MJYvUKw784yJFskbtVxcJv2AWbax7nz2VwXTuqKYYNQy1UGU+z+KInS910U28GGqrv" +
            "cF5jp0AJbQV/DqGqreLsWQMoGw6+WpChjQrXdufebXxMlwMqoJfb5+E0tysta+DI8tHE3vwT0IAKhAoIBAHm4KDYl1nbicjb4YC9rEYvpbbJ2qA3DLvmSHk2ckQ" +
            "TVHYhxhbidU57X9hh+sCHMti8Dx2VZKArJPqSIiPKoG9/su024cUXsD4xC4Xy3YlmRJSLowVaKsbhTb66stcmBqG2PmYTgDwUcJFu70XGyFHrr8/q6U0xvxfpbXR" +
            "+Ap1OpurwgHqU7FbGEsoi0/5mQZpLkTIkEMQWp06DBB6tvvXLqn/fbgqCe1Jk8HV4Mu0VM0D4aapHNTeFnRhVm4DVg3KRnM8COWuRKS5ha8wNd17WYYWVNoGB5zK" +
            "lwhNAp8VSB7k8UK7BQyHFqIYxaos7nEQBJbBAbDAOz0P6++GW6bVcCggEBAMLuD+kx2oGP9o7nQSKxJ56Goel0aOvhcaBiW4dudPOOGdr/r2WjpoVgs5xIItfCPN" +
            "LVuFwnZQwXGvVft4kM9Nlc71qw8osfBNyXepWYlGEPMuPuycIdsw9jPmosxXKlqE/F7Y1i1WwiuHBkIAIkUasxa9Gg706zuGoFz41qfIIVZwmHpChjlv4SHCmnaxy" +
            "cUntvb52aQbCcDApLnFxMmA9AB51UzSjJA" +
            "gCyZkpVhYxxWFbIn8ftkOI/sFot7poaRdAEg1FDCncO9AF7MgBIv/U4rtZQ8ZJ1PrbF9fLPjHKtqPHGWiGbXkUX2Bv/SkTUQQ7XVFByr3yPKkmhNMMg83I=";

    final String encryptedText = "kUT+dRb4ZRD86M4kvAfW5+QaRbc22y1mgHZI0qVnLK8+" +
            "lKJfunzSUVk7CziE8nf2pDia0T5psF5+PO48QrzKBbbmDXtk+zmsaoguC0Yeyv3cvN" +
            "DiOZvzS9HJtgnozlNlLYENG2ts+RXsD0KBEAWWU0CdZsWHSG7Y6vqfDChT+Z1onPjMN" +
            "i7OS4mQpY60Ys726xw/nAse1WmC1oq5CZXyLqpG22hkP3AHNqhO97IgFpIvQDG/DwaNu" +
            "6+kWY6WL0keT7hQl2qG9/ca/gm9Hsj38/Uw6T4Uos5ov0rqOb4RhAiJRe3IQPoJ6cFlAEQ" +
            "BUJa4SeabQ0fgX/ngUlNC2IABQJs0YixMA7lCKhUYlSBfeurujfafLOowlMdmE8ufsWRM" +
            "v/ab9As70k9Vtiekgi0tspd9hWyDc8BjYko2S2dhLDW7ByQ0i55iR7jFVXFdxhT50d3P2r+" +
            "ZOxZsAtbiXQu6+j0Bf+iBVKVAaucO5OD+UrYCio8BjU5M6FOxfdXCQsrkzGVwpvhLWDKexh" +
            "/TXtvaZYYiMNVfm0In5+OFuRM5LbpkhGnU3dJp63zkelFYmGTTar3MVbnHpMep7tgtep63B6" +
            "d3u8fuNhpJ6BA3IyscCRq" +
            "BnR6W2jitWkoZqHy9wH2TAArm72+fgeQxGltYAptX8f+0tBcXtgWZehykXN8JlfY=";

    public static final byte[] incomingBytes = new byte[]{107, 0, -10, 65, 121, -60, -35, -128, -92, -105, 78, -85, -13, 84, -109, 127};

    @Test
    public void testEncryptOnResultAll0s() throws Exception {

        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyEncoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                decodedPublicKey);
        PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);

        byte[] cipherTextArray = MyApplet.encrypt(incomingBytes, testingToken.getBytes(), publicKey2);

        Assert.assertFalse(byteArrayCheck3(cipherTextArray));

    }

    @Test
    public void testEncryptResultIs512B() throws Exception {

        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyEncoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                decodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        byte[] cipherTextArray = MyApplet.encrypt(incomingBytes, testingToken.getBytes(), publicKey);

        Assert.assertEquals(512, cipherTextArray.length);

    }

    @Test
    public void testDecryption() throws Exception {
        //Get initial nonce + token byte array
        byte[] combined = new byte[incomingBytes.length + testingToken.getBytes().length];
        for (int i = 0; i < combined.length; ++i)
        {
            combined[i] = i < incomingBytes.length ? incomingBytes[i] : testingToken.getBytes()[i - incomingBytes.length];
        }

        //Decode public key from string to byte array
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyEncoded);

        //Recover public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                decodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        //Encrypt nonce and token
        byte[] cipherTextArray = MyApplet.encrypt(incomingBytes, testingToken.getBytes(), publicKey);

        //Assert decrypted message and initial nonce + token
        Assert.assertEquals(Arrays.toString(decrypt(privateKeyEncoded, cipherTextArray)), (Arrays.toString(combined)));
    }

    //Check if all bytes of array are not 0
    private boolean byteArrayCheck3(final byte[] array) {
        for (byte b : array) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    public static byte[] decrypt(String privateKeyEncodedInput, byte[] cipherTextArray) throws Exception
    {

        //Decoding BASE64 back to byte array
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyEncodedInput);

        //Read back the Private key
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                decodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Perform Decryption
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

        return decryptedTextArray;
    }

}