package com.fast0n.majoranaopd;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    AnimationDrawable mframeAnimation = null;
    AnimationDrawable mframeAnimation2 = null;
    ImageView prima,seconda,terza;
    Button  start,stop,color;
    Switch switch1, switch2,switch3;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(com.fast0n.majoranaopd.DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_main);

        //call the widgtes

        start = (Button)findViewById(R.id.button2);
        stop = (Button)findViewById(R.id.button3);
        color = (Button)findViewById(R.id.button4);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch2 = (Switch)findViewById(R.id.switch2);

        prima = (ImageView)findViewById(R.id.imageView);
        seconda = (ImageView)findViewById(R.id.imageView2);
        terza = (ImageView)findViewById(R.id.imageView3);


        final Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);



        new ConnectBT().execute();




        BitmapDrawable frame0 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon0);
        BitmapDrawable frame1 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon1);
        BitmapDrawable frame2 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon2);
        BitmapDrawable frame3 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon3);
        BitmapDrawable frame4 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon4);
        BitmapDrawable frame5 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon5);
        BitmapDrawable frame6 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.funon6);

        BitmapDrawable frame7 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.motor0);
        BitmapDrawable frame8 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.motor1);
        BitmapDrawable frame9 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.motor2);
        BitmapDrawable frame10 = (BitmapDrawable) getResources().getDrawable(
                R.drawable.motor3);


        int reasonableDuration2 = 40;
        mframeAnimation2 = new AnimationDrawable();
        mframeAnimation2.setOneShot(false);
        mframeAnimation2.addFrame(frame7, reasonableDuration2);
        mframeAnimation2.addFrame(frame8, reasonableDuration2);
        mframeAnimation2.addFrame(frame9, reasonableDuration2);
        mframeAnimation2.addFrame(frame10, reasonableDuration2);
        terza.setBackgroundDrawable(mframeAnimation2);
        mframeAnimation2.setVisible(true, true);


        int reasonableDuration = 20;
        mframeAnimation = new AnimationDrawable();
        mframeAnimation.setOneShot(false);
        mframeAnimation.addFrame(frame0, reasonableDuration);
        mframeAnimation.addFrame(frame1, reasonableDuration);
        mframeAnimation.addFrame(frame2, reasonableDuration);
        mframeAnimation.addFrame(frame3, reasonableDuration);
        mframeAnimation.addFrame(frame4, reasonableDuration);
        mframeAnimation.addFrame(frame5, reasonableDuration);
        mframeAnimation.addFrame(frame6, reasonableDuration);
        seconda.setBackgroundDrawable(mframeAnimation);
        mframeAnimation.setVisible(true, true);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    prima.setImageResource(R.drawable.ledon);
                    turnOnLed();   //method to turn off
                } else {
                    prima.setImageResource(R.drawable.ledoff);
                    turnOffLed();   //method to turn on
                }

            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {


                if (isChecked) {
                    seconda.setImageResource(R.drawable.funon);
                    turnOnFan();   //method to turn off
                    mframeAnimation.start();

                } else {

                    turnOffFan();   //method to turn on
                    mframeAnimation.stop();

                }

            }
        });






        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mframeAnimation2.start();
                vibe.vibrate(50);
                turnOnMotor(); //close connection
                color.setBackgroundTintList(getResources().getColorStateList(R.color.start));
                terza.setImageResource(R.drawable.funon);

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibe.vibrate(50);
                turnOffMotor(); //close connection
                color.setBackgroundTintList(getResources().getColorStateList(R.color.stop));
                mframeAnimation2.stop();
            }
        });



    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {     Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}
        }
        finish(); //return to the first layout

    }












    private void turnOnLed()
    {if (btSocket!=null)
    {try{btSocket.getOutputStream().write("L".toString().getBytes());}
    catch (IOException e)
    {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}


    private void turnOffLed()
    {if (btSocket!=null)
        {try{btSocket.getOutputStream().write("l".toString().getBytes());}
            catch (IOException e)
            {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}


    private void turnOnFan()
    {if (btSocket!=null)
    {try{btSocket.getOutputStream().write("V".toString().getBytes());}
    catch (IOException e)
    {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}


    private void turnOffFan()
    {if (btSocket!=null)
    {try{btSocket.getOutputStream().write("v".toString().getBytes());}
    catch (IOException e)
    {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}




    private void turnOnMotor()
    {if (btSocket!=null)
    {try{btSocket.getOutputStream().write("M".toString().getBytes());}
    catch (IOException e)
    {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}

    private void turnOffMotor()
    {if (btSocket!=null)
    {try{btSocket.getOutputStream().write("m".toString().getBytes());}
    catch (IOException e)
    {Snackbar.make(prima,R.string.error, Snackbar.LENGTH_SHORT).show();}}}

















    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, getString(R.string.connection), getString(R.string.plasewait));  //show a progress dialog
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            if (!ConnectSuccess)
        /*    {Toast.makeText(MainActivity.this, R.string.error2, Toast.LENGTH_LONG).show();

                finish();
            }
            else
          */ {
            Snackbar.make(prima,R.string.connected, Snackbar.LENGTH_SHORT).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }





















































    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_disconnected) {

            Disconnect();
        }

        if (id == R.id.action_about) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Fast0n");
            alertDialog.setMessage("L'applicazione che stai visualizzando è stata creata da Massimiliano Montaleone. Se ti serve un applicazione contattami al 3291847071 oppure sulla mia mail Theplayergame97@gmail.com.");
            alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }



            });

            alertDialog.show();

        }


            return super.onOptionsItemSelected(item);
    }
}
