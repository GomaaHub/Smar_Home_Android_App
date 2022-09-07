package com.example.myapplication;

import static android.view.View.FOCUS_DOWN;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ExpandableListAdapter;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG="DEBUG_MA";

    TextView tvMAReceivedMessage;
    TextView tv2MAReceivedMessage;
    TextView tvRedSeekBar;
    TextView tvYellowSeekBar;
    TextView tvGreenSeekBar;
    EditText editTextSendMessage;
    Spinner spinnerBTPairedDevices;
    ScrollView scroll1;


    static public final int BT_CON_STATUS_NOT_CONNECTED     =0;
    static public final int BT_CON_STATUS_CONNECTING        =1;
    static public final int BT_CON_STATUS_CONNECTED         =2;
    static public final int BT_CON_STATUS_FAILED            =3;
    static public final int BT_CON_STATUS_CONNECTiON_LOST   =4;
    static public int iBTConnectionStatus = BT_CON_STATUS_NOT_CONNECTED;

    static final int BT_STATE_LISTENING            =1;
    static final int BT_STATE_CONNECTING           =2;
    static final int BT_STATE_CONNECTED            =3;
    static final int BT_STATE_CONNECTION_FAILED    =4;
    static final int BT_STATE_MESSAGE_RECEIVED     =5;


    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothSocket BTSocket=null;
    BluetoothAdapter BTAdapter = null;
    Set<BluetoothDevice> BTPairedDevices=null;

    boolean bBTConnected=false;
    BluetoothDevice BTDevice=null;
    classBTInitDataCommunication cBTInitSendReceive =null;

    Button buttonSendMessage;
    Button buttonBTConnect;
    Button buttonShare;
    Button buttonMemory1;
    Button buttonMemory2;
    Button buttonMemory3;
    Button buttonMemory4;
    Button buttonMemory5;
    Button buttonMemory6;
    Button buttonMemory7;
    Button buttonMemory8;
    Button buttonMemory9;

    SeekBar RedSeekBar;
    SeekBar YellowSeekBar;
    SeekBar GreenSeekBar;

    Integer RedLedDutyCycle;
    Integer YellowLedDutyCycle;
    Integer GreenLedDutyCycle;
    Integer RedPercentage;
    Integer YellowPercentage;
    Integer GreenPercentage;


    String sRedLedDutyCycle;
    String sYellowLedDutyCycle;
    String sGreenLedDutyCycle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG,"onCreate-Start");

       // tvMAReceivedMessage=findViewById(R.id.idMATextViewReceivedMessages);
//        tvMAReceivedMessage.setMovementMethod(new ScrollingMovementMethod());

        tv2MAReceivedMessage=findViewById(R.id.idMATextViewScrollReceivedMessages);

        buttonSendMessage=findViewById(R.id.idMAButtonSendData);
        buttonBTConnect=findViewById(R.id.idMAButtonConnect);

        buttonMemory1=findViewById(R.id.idMAButtonStoreData1);
        buttonMemory2=findViewById(R.id.idMAButtonStoreData2);
        buttonMemory3=findViewById(R.id.idMAButtonStoreData3);
        buttonMemory4=findViewById(R.id.idMAButtonStoreData4);

        buttonMemory6=findViewById(R.id.idMAButtonStoreData6);

        buttonMemory8=findViewById(R.id.idMAButtonStoreData8);
        buttonMemory9=findViewById(R.id.idMAButtonStoreData9);

        RedSeekBar=findViewById(R.id.seekBarRed);
        YellowSeekBar=findViewById(R.id.seekBarYellow);
        GreenSeekBar=findViewById(R.id.seekBarGreen);

        tvRedSeekBar=findViewById(R.id.tvRedSeekBar);
        tvYellowSeekBar=findViewById(R.id.tvYellowSeekBar);
        tvGreenSeekBar=findViewById(R.id.tvGreenSeekBar);

        spinnerBTPairedDevices=findViewById(R.id.idMASpinnerBTPairedDevices);

        scroll1=findViewById(R.id.idMAmyscroll);


        editTextSendMessage=findViewById(R.id.idMAEditTextSendMessage);




        buttonSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Send Button Clicked");

                String sMessage = editTextSendMessage.getText().toString();
                tv2MAReceivedMessage.append("\n" + sMessage);
                scroll1.fullScroll(FOCUS_DOWN);
                editTextSendMessage.setText("");
                sendMessage(sMessage);
            }
        });

        buttonBTConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Click buttonBTConnect");

                if(bBTConnected==false) {
                    if (spinnerBTPairedDevices.getSelectedItemPosition() == 0) {
                        Log.d(TAG, "Please select BT device");
                        Toast.makeText(getApplicationContext(), "Please select Bluetooth Device", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String sSelectedDevice = spinnerBTPairedDevices.getSelectedItem().toString();
                    Log.d(TAG, "Selected device = " + sSelectedDevice);

                    for (BluetoothDevice BTDev : BTPairedDevices) {
                        if (sSelectedDevice.equals(BTDev.getName())) {
                            BTDevice = BTDev;
                            Log.d(TAG, "Selected device UUID = " + BTDevice.getAddress());

                            cBluetoothConnect cBTConnect = new cBluetoothConnect(BTDevice);
                            cBTConnect.start();

//
//                            try {
//                                Log.d(TAG, "Creating socket, my uuid " + MY_UUID);
//                                BTSocket = BTDevice.createRfcommSocketToServiceRecord(MY_UUID);
//                                Log.d(TAG, "Connecting to device");
//                                BTSocket.connect();
//                                Log.d(TAG, "Connected");
//                                buttonBTConnect.setText("Disconnect");
//                                bBTConnected = true;
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Log.e(TAG, "Exception = " + e.getMessage());
//                                bBTConnected = false;
//                            }


                        }
                    }
                }
                else {
                    Log.d(TAG, "Disconnecting BTConnection");
                    if(BTSocket!=null && BTSocket.isConnected())
                    {
                        try {
                            BTSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(TAG, "BTDisconnect Exp " + e.getMessage());
                        }
                    }
                    buttonBTConnect.setText("Connect");
                    bBTConnected = false;
                    tv2MAReceivedMessage.append("\nDEVICE DISCONNECTED!");

                }



            }
        });


        RedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //tv2MAReceivedMessage.append("\n" + "Y");
                sendMessage("Y");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RedLedDutyCycle=RedSeekBar.getProgress();
                RedPercentage=RedLedDutyCycle*100/255;
                sRedLedDutyCycle=RedLedDutyCycle.toString();
                sendMessage(sRedLedDutyCycle);
                tvRedSeekBar.setText(RedPercentage.toString() + "%");
            }
        });

        YellowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sendMessage("X");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                YellowLedDutyCycle=YellowSeekBar.getProgress();
                sYellowLedDutyCycle=YellowLedDutyCycle.toString();
                sendMessage(sYellowLedDutyCycle);
                tvYellowSeekBar.setText(YellowLedDutyCycle.toString() + "\u00B0");
            }
        });

        GreenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sendMessage("Z");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GreenLedDutyCycle=GreenSeekBar.getProgress();
                GreenPercentage=GreenLedDutyCycle;
                sGreenLedDutyCycle=GreenLedDutyCycle.toString();
                sendMessage(sGreenLedDutyCycle);
                tvGreenSeekBar.setText(GreenPercentage.toString() + "%");
            }
        });



        buttonMemory1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M1 Button Clicked");
                sendMessage("M");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });

        buttonMemory2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M2 Button Clicked");
                sendMessage("N");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });

        buttonMemory3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M3 Button Clicked");
                sendMessage("L");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });

        buttonMemory4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M4 Button Clicked");
                sendMessage("CLEAR");
                tv2MAReceivedMessage.setText(" ");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });



        buttonMemory6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M6 Button Clicked");

                sendMessage("3");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });



        buttonMemory8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M8 Button Clicked");
                sendMessage("5");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });

        buttonMemory9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M9 Button Clicked");
                sendMessage("6");
                scroll1.fullScroll(FOCUS_DOWN);
            }
        });

    }




    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case BT_STATE_LISTENING:
                    Log.d(TAG, "BT_STATE_LISTENING");
                    break;
                case BT_STATE_CONNECTING:
                    iBTConnectionStatus = BT_CON_STATUS_CONNECTING;
                    buttonBTConnect.setText("Connecting..");
                    Log.d(TAG, "BT_STATE_CONNECTING");
                    break;
                case BT_STATE_CONNECTED:

                    iBTConnectionStatus = BT_CON_STATUS_CONNECTED;

                    Log.d(TAG, "BT_CON_STATUS_CONNECTED");
                    buttonBTConnect.setText("Disconnect");
                    tv2MAReceivedMessage.append("\nSUCCESSFULLY CONNECTED!");

                    cBTInitSendReceive = new classBTInitDataCommunication(BTSocket);
                    cBTInitSendReceive.start();

                    bBTConnected = true;
                    break;
                case BT_STATE_CONNECTION_FAILED:
                    iBTConnectionStatus = BT_CON_STATUS_FAILED;
                    Log.d(TAG, "BT_STATE_CONNECTION_FAILED");
                    bBTConnected = false;
                    break;

                case BT_STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    Log.d(TAG, "Message receive ( " + tempMsg.length() + " )  data : " + tempMsg);

                    tvMAReceivedMessage.append(tempMsg);


                    break;

            }
            return true;
        }
    });

    void getBTPairedDevices()
    {
        Log.d(TAG,"Get Paired Devices = Start");
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if(BTAdapter==null)
        {
            Log.e(TAG,"Get Paired Devices, BTAdapter = null");
            editTextSendMessage.setText("No Bluetooth Devices found");
            return;
        }
        else if(!BTAdapter.isEnabled())
        {
            Log.e(TAG,"Get Paired Devices, BT not enabled");
            editTextSendMessage.setText("Please turn on Bluetooth");
            return;
        }

        BTPairedDevices = BTAdapter.getBondedDevices();
        Log.d(TAG,"Get Paired Devices, Paired Devices Count = " + BTPairedDevices.size());

        for(BluetoothDevice BTDev: BTPairedDevices)
        {
            Log.d(TAG, BTDev.getName() + ", " + BTDev.getAddress());
        }
    }

    public class cBluetoothConnect extends Thread
    {
        private BluetoothDevice device;


        public cBluetoothConnect (BluetoothDevice BTDevice)
        {
            Log.i(TAG, "classBTConnect-start");

            device = BTDevice;
            try{
                BTSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (Exception exp)
            {
                Log.e(TAG, "classBTConnect-exp" + exp.getMessage());
            }
        }

        public void run()
        {
            try {
                BTSocket.connect();
                Message message=Message.obtain();
                message.what=BT_STATE_CONNECTED;
                handler.sendMessage(message);


            } catch (IOException e) {
                e.printStackTrace();
                Message message=Message.obtain();
                message.what=BT_STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }

    }

    public class classBTInitDataCommunication extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private InputStream inputStream =null;
        private OutputStream outputStream=null;

        public classBTInitDataCommunication (BluetoothSocket socket)
        {
            Log.i(TAG, "classBTInitDataCommunication-start");

            bluetoothSocket=socket;


            try {
                inputStream=bluetoothSocket.getInputStream();
                outputStream=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "classBTInitDataCommunication-start, exp " + e.getMessage());
            }


        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (BTSocket.isConnected())
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(BT_STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "BT disconnect from decide end, exp " + e.getMessage());
                    iBTConnectionStatus=BT_CON_STATUS_CONNECTiON_LOST;
                    try {
                        //disconnect bluetooth
                        Log.d(TAG, "Disconnecting BTConnection");
                        if(BTSocket!=null && BTSocket.isConnected())
                        {
                            BTSocket.close();
                        }
                        buttonBTConnect.setText("Connect");
                        bBTConnected = false;

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(String sMessage)
    {
        if( BTSocket!= null && iBTConnectionStatus==BT_CON_STATUS_CONNECTED)
        {
            if(BTSocket.isConnected() )
            {
                try {
                    cBTInitSendReceive.write(sMessage.getBytes());
                    scroll1.fullScroll(FOCUS_DOWN);
                    //tvMAReceivedMessage.append("\r\n-> " + sMessage);
                }
                catch (Exception exp)
                {

                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Please connect to bluetooth", Toast.LENGTH_SHORT).show();
            tv2MAReceivedMessage.append("\r\n Not connected to bluetooth");
        }

    }


    void PutPairedBTDevicesInSpinner()
    {
        ArrayList<String> AllPairedDevices = new ArrayList<String>();
        AllPairedDevices.add("Select Device");
        for(BluetoothDevice BTDev: BTPairedDevices)
        {
            AllPairedDevices.add(BTDev.getName());
        }
        final ArrayAdapter<String> EveryPairedDevice = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,AllPairedDevices);
        EveryPairedDevice.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spinnerBTPairedDevices.setAdapter(EveryPairedDevice);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"onResume-Resume");
        getBTPairedDevices();
        PutPairedBTDevicesInSpinner();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG,"onPause-Start");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"onDestroy-Start");
    }
}