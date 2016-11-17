package com.helge.arrhythmiapt;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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


    Button capacitorButton, ecgButton, deliverShockButton;
    TextView ecgTextView, capacitorTextView, deliverShockTextView;

    public boolean arduinoIsConnected = false;
    private enum ArduinoState {
        STATIC, ECG, CAPACITOR, DELIVER_SHOCK
    }
    String STATIC_ARDUINO_CODE = "0";
    String ECG_ARDUINO_CODE = "1";
    String CAPACITOR_ARDUINO_CODE = "2";
    String DELIVER_SHOCK_ARDUINO_CODE = "5";
    private ArduinoState currentAndroidState = ArduinoState.STATIC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_testing);

        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        ecgTextView = (TextView) findViewById(R.id.ecgTextView);
        capacitorTextView = (TextView) findViewById(R.id.capacitorTextView);
        deliverShockTextView = (TextView) findViewById(R.id.deliveredShockTextView);

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

        if(arduinoIsConnected){
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
                switch (currentAndroidState) {
                    case ECG:
                        data.concat(", ");
                        tvAppend(ecgTextView, data);
                        break;
                    case DELIVER_SHOCK:
                        if (data.equals("1")){
                            deliverShockTextView.setText("Shock Delivered");
                        }else {
                            deliverShockTextView.setText(("Shock Preparing"));
                        }
                        break;
                    case CAPACITOR:
                        capacitorTextView.setText(data);
//                        if(data.equals("0")){
//                            capacitorTextView.setText("Capacitor Not Charged");
//                        }else if(data.equals("1")){
//                            capacitorTextView.setText("Capacitor Almost Charged");
//                        }else if(data.equals("2")){
//                            capacitorTextView.setText("Capacitor Charged!");
//                        }
                        break;
                    default:
                        break;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
        serialPort.close();
    }

    public void startSerialConnection() {

        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                if (deviceVID == 0x2341)//Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pi);
                    keep = false;
                    arduinoIsConnected = true;
                } else {
                    connection = null;
                    device = null;
                }

                if (!keep)
                    break;
            }
        }


    }


    //BUTTON CODE

    public void ecgButtonPressed(View view) {
        sendArduinoNextState(ArduinoState.ECG);
    }
    public void capacitorButtonPressed(View view) {
        sendArduinoNextState(ArduinoState.CAPACITOR);
    }
    public void deliverShockButtonPressed(View view) {
        sendArduinoNextState(ArduinoState.DELIVER_SHOCK);
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

}
