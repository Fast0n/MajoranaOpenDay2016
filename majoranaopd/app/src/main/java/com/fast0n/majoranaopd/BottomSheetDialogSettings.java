package com.fast0n.majoranaopd;

import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

public class BottomSheetDialogSettings extends BottomSheetDialogFragment {

    BluetoothSocket btSocket = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_settings, container, false);
        inflater.getContext().setTheme(R.style.BottomSheetDialogTheme);


        ConstraintLayout bottom_sheet_disconnect = view.findViewById(R.id.bottom_sheet_disconnect);
        ConstraintLayout bottom_sheet_info = view.findViewById(R.id.bottom_sheet_info);


        bottom_sheet_disconnect.setOnClickListener(view12 -> {
            if (btSocket != null) {
                try {
                    btSocket.close(); //close connection
                } catch (IOException e) {
                    Log.e(getString(R.string.error), getString(R.string.error));
                }
            }

            getActivity().finish();
        });

        bottom_sheet_info.setOnClickListener(view1 -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Fast0n");
            alertDialog.setMessage("L'applicazione che stai visualizzando è stata creata da Massimiliano Montaleone. Se ti serve un applicazione contattami al 3291847071 oppure sulla mia mail Theplayergame97@gmail.com.");
            alertDialog.show();
        });

        return view;

    }
}
