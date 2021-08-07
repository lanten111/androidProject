package co.za.delivernow.Utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class Utils {

    public static boolean isValidEmail(String email){
            return  !(Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmpty(String string){
        return TextUtils.isEmpty(string);
    }
}
