package com.fast0n.majoranaopd;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static com.fast0n.majoranaopd.DeviceListActivity.EXTRA_ADDRESS;


public class MainActivity extends AppCompatActivity {


    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ImageView bulb, fan, motor;

    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    int statusBulb = 0;
    int statusFan = 0;
    int statusMotor = 0;
    TextView mTitle;
    ImageButton imageButton;
    private ProgressDialog progress;
    private boolean isBtConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        address = getIntent().getStringExtra(EXTRA_ADDRESS); //receive the address of the bluetooth device
        setContentView(R.layout.activity_main);
        new ConnectBT().execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(toolbar.getTitle());
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // region set statusbar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(android.R.color.background_light));
        // endregion


        bulb = findViewById(R.id.imageView);
        fan = findViewById(R.id.imageView2);
        motor = findViewById(R.id.imageView3);
        imageButton = findViewById(R.id.imageButton);


        Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);


        bulb.setOnClickListener(view -> {
            if (statusBulb == 0) {
                bulb.setImageResource(R.drawable.ic_lightbulb_on);
                sendCommand("L");
                statusBulb++;
            } else {
                bulb.setImageResource(R.drawable.ic_lightbulb_off);
                sendCommand("l");
                statusBulb--;
            }

        });

        fan.setOnClickListener(view -> {
            if (statusFan == 0) {
                sendCommand("V");
                statusFan++;
                fan.startAnimation(
                        AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));
            } else {
                fan.clearAnimation();
                sendCommand("v");
                statusFan--;
            }
        });


        motor.setOnClickListener(view -> {
            if (statusMotor == 0) {
                Objects.requireNonNull(vibe).vibrate(50);
                sendCommand("M");
                statusMotor++;
                motor.startAnimation(
                        AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));
            } else {
                Objects.requireNonNull(vibe).vibrate(50);
                motor.clearAnimation();
                sendCommand("m");
                statusMotor--;
            }
        });

        imageButton.setOnClickListener(view -> {
            BottomSheetDialogSettings bottomSheetDialog = new BottomSheetDialogSettings();
            bottomSheetDialog.show(getSupportFragmentManager(), "show");

        });


    }


    private void sendCommand(String command) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(command.getBytes());
            } catch (IOException e) {
                Snackbar.make(bulb, R.string.error, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, getString(R.string.connection), getString(R.string.plasewait));  //show a progress dialog
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                Snackbar.make(fan, R.string.connected, Snackbar.LENGTH_SHORT).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
