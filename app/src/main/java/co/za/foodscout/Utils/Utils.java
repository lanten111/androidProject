package co.za.foodscout.Utils;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    public static LatLng getLatLong(GeoPoint geoPoint){
       return new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public static LatLng getLatLong(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static void zoomInTwoPoints(GoogleMap mMap, LatLng mOrigin, LatLng mDestination ){
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(mOrigin).include(mDestination);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(b.build(), 200));
        mMap.setTrafficEnabled(true);
    }

    public static void ZoomInLocation(GoogleMap mMap,LatLng location){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
    }


}
