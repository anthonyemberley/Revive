package com.helge.arrhythmiapt;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ArduinoTestingActivity extends AppCompatActivity {
    //USB Stuff
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    public final String ACTION_USB_PERMISSION = "com.a2009pink.revive.USB_PERMISSION";


    Button capacitorButton, ecgButton, deliverShockButton, clearButton;
    TextView ecgTextView, capacitorTextView, deliverShockTextView;

    public boolean arduinoIsConnected = false;
    private enum ArduinoState {
        STATIC, ECG, CAPACITOR, DELIVER_SHOCK
    }
    String STATIC_ARDUINO_CODE = "0";
    String ECG_ARDUINO_CODE = "1";
    String CAPACITOR_ARDUINO_CODE = "2";
    String DELIVER_SHOCK_ARDUINO_CODE = "3";
    private ArduinoState currentAndroidState = ArduinoState.STATIC;


    int points = 1;
    int numberOfPoints = 1000;
    int[] ecgArray = new int[numberOfPoints];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_testing);

        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        clearButton = (Button) findViewById(R.id.clearDataButton);
        ecgButton = (Button) findViewById(R.id.ecgButton);
        deliverShockButton = (Button) findViewById(R.id.deliverShockButton);
        capacitorButton = (Button) findViewById(R.id.checkCapacitorButton);
        ecgTextView = (TextView) findViewById(R.id.ecgTextView);
        capacitorTextView = (TextView) findViewById(R.id.capacitorTextView);
        deliverShockTextView = (TextView) findViewById(R.id.deliveredShockTextView);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);

        startSerialConnection();
    }



    private void sendArduinoNextState(ArduinoState nextState){
        String sendString = "";
        switch (nextState) {
            case ECG:
                sendString = ECG_ARDUINO_CODE;
                break;
            case DELIVER_SHOCK:
                sendString = DELIVER_SHOCK_ARDUINO_CODE;
                break;
            case STATIC:
                sendString = STATIC_ARDUINO_CODE;
                break;
            case CAPACITOR:
                sendString = CAPACITOR_ARDUINO_CODE;
                break;
            default:
                break;
        }


            if(serialPort != null){
                serialPort.write(sendString.getBytes());
                currentAndroidState = nextState;
            }



    }




    //USB METHODS

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data = data.trim();
                switch (currentAndroidState) {
                    case ECG:
                        tvAppend(ecgTextView, data);
//                        int dataInt = Integer.parseInt(data);
//                        if(points < numberOfPoints){
//                            ecgArray[points] = dataInt;
//                        } else if(points == numberOfPoints){
//                            ecgTextView.setText("nilla we made it");
//                        }

                        break;
                    case DELIVER_SHOCK:
                        if (data.equals("1")){
                            tvSet(deliverShockTextView, "Shock Delivered");
                        }else if (data.equals("0")){
                            tvSet(deliverShockTextView, "Shock Preparing");
                        }
                        break;
                    case CAPACITOR:
                        if(data.equals("0")){
                            tvSet(capacitorTextView, "Capacitor Not Charged");
                        }else if(data.equals("1")){
                            tvSet(capacitorTextView, "Capacitor Almost Charged");
                        }else if(data.equals("2")){
                            tvSet(capacitorTextView, "Capacitor Charged!");
                        }
                        break;
                    default:
                        break;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch(NumberFormatException e) {
            } catch(NullPointerException e) {
            }
        }
    };


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { //Broadcast Receiver to automatically start and stop the Serial connection.
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    connection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
                    if (serialPort != null) {
                        if (serialPort.open()) { //Set Serial Connection Parameters.
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);

                        } else {
                            Log.d("SERIAL", "PORT NOT OPEN");
                        }
                    } else {
                        Log.d("SERIAL", "PORT IS NULL");
                    }
                } else {
                    Log.d("SERIAL", "PERM NOT GRANTED");
                }
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                startSerialConnection();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                stopSerialConnection();
            }
        }

        ;
    };

    public void stopSerialConnection() {
        arduinoIsConnected = false;
        if(serialPort != null){
            serialPort.close();
        }
    }

    public void startSerialConnection() {

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                //textView.setText(deviceVID);
//                if (deviceVID == 0x2341)//Arduino Vendor ID
//                {
                PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(device, pi);
                keep = false;
                //} else {
                //   connection = null;
                //   device = null;
                //}

                if (!keep)
                    break;
            }
        }
        //arduinoIsConnected = true;


    }


    //BUTTON CODE

    public void ecgButtonPressed(View view) {
        System.out.println("here");
        sendArduinoNextState(ArduinoState.ECG);
    }
    public void capacitorButtonPressed(View view) {

        sendArduinoNextState(ArduinoState.CAPACITOR);
    }
    public void deliverShockButtonPressed(View view) {


        sendArduinoNextState(ArduinoState.DELIVER_SHOCK);
    }

    public void clearDataPressed(View view) {
        ecgTextView.setText("");
    }


    //Helper Methods
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }

    private void tvSet(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ftv.setText(ftext);
            }
        });
    }

}
