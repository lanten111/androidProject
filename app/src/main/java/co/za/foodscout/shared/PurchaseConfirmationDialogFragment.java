package co.za.foodscout.shared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import foodscout.R;

public class PurchaseConfirmationDialogFragment extends DialogFragment {
   @NonNull
   @Override
   public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       return new AlertDialog.Builder(requireContext())
               .setMessage("confimed")
               .setPositiveButton("OK", (dialog, which) -> {} )
               .create();
   }

   public static String TAG = "PurchaseConfirmationDialog";
}