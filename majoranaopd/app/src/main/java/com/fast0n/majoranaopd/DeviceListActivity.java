package com.fast0n.majoranaopd;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    public static String EXTRA_ADDRESS = "device_address";
    //widgets
    Button btnPaired;
    ListView devicelist;
    String info, address, name;
    TextView mTitle;

    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private long mBackPressed;
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            info = ((TextView) v).getText().toString();
            address = info.substring(0, 17);
            name = info.substring(0, info.length() - 17);


            if (name.equals("HC-O5") || name.equals("HC-O6"))
                startActivity(new Intent(DeviceListActivity.this, MainActivity.class).putExtra(EXTRA_ADDRESS, address));
            else
                Snackbar.make(devicelist, getString(R.string.deviceNS), Snackbar.LENGTH_SHORT).show();


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);


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
        btnPaired = findViewById(R.id.button);
        devicelist = findViewById(R.id.listView);


        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        pairedDevicesList();

        if (myBluetooth == null) {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), getString(R.string.notavailable), Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBluetooth.isEnabled())
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);


        btnPaired.setOnClickListener(v -> pairedDevicesList());


    }

    private void pairedDevicesList() {
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                if (bt.getName().equals("HC-O5") || bt.getName().equals("HC-O6"))
                    list.add(bt.getName() + "\n" + bt.getAddress());
                else
                    list.add(getString(R.string.deviceNS) + "\n" + bt.getAddress());
            }
        } else
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            this.finish();
            System.exit(0);
            finish();
            moveTaskToBack(true);
        } else {
            Snackbar.make(devicelist, getString(R.string.back), Snackbar.LENGTH_SHORT).show();
            mBackPressed = System.currentTimeMillis();
        }
    }


}
