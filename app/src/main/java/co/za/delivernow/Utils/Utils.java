package co.za.delivernow.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

public class Utils {

    public static boolean isValidEmail(String email){
            return  !(Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmpty(String string){
        return TextUtils.isEmpty(string);
    }

//    public static void isConnectionAvailable(Context context){
//        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
//    }
}
