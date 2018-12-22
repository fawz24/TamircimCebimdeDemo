package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helpers {

    public static void goToHome(Context context, FirebaseUser currentUser){
        Intent intent = new Intent(context, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userEmail", currentUser.getEmail());
        bundle.putString("userName", currentUser.getDisplayName());
        bundle.putString("userId", currentUser.getUid());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void signOut(Context context, FirebaseAuth dbAuth){
        FirebaseUser user = dbAuth.getCurrentUser();
        if (user != null){
            dbAuth.signOut();
        }
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

//    public static void createUser(Context context, FirebaseUser authUser, )

    public static String hashPassword(String password) {
        String hashedPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            hashedPassword = new String(Hex.encode(hash));
        }
        catch (NoSuchAlgorithmException e){
        }
        catch (UnsupportedEncodingException e){
        }

        return hashedPassword;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
