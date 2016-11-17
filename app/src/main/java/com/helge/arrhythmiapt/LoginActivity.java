package com.helge.arrhythmiapt;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.parse.FindCallback;
//import com.parse.LogInCallback;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//
//import java.io.IOException;
//import java.util.List;
//
//public class LoginActivity extends AppCompatActivity {
//    /*
//         Lets the patient login with username and password using the ParseUser object.
//         Also includes a button for registering a new user.
//     */
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
//        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
//        final Button bLogin = (Button) findViewById(R.id.bLogin);
//        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);
//
//        assert etUsername != null;
//        assert etPassword != null;
//        assert bLogin != null;
//        assert registerLink != null;
//
//        // Start register user activity
//        registerLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//                LoginActivity.this.startActivity(registerIntent);
//            }
//        });
//
//        // Get entered username and password and send a login request in the background.
//        bLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//                bLogin.setEnabled(false);
//
//                // Login in background and start the main menu on success.
//                ParseUser.logInInBackground(username, password, new LogInCallback() {
//                    public void done(ParseUser user, ParseException e) {
//                        if (user != null) {
//                            Intent i = new Intent(LoginActivity.this, MainMenu.class);
//                            startActivity(i);
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//        // Fetch notes from Parse server in background and save locally
//        fetchNotes();
//
//        SignalProcessing signalProcessing = new SignalProcessing(this);
//        try {
//            signalProcessing.readECG();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        signalProcessing.detect_and_classify();
//    }
//
//    // Fetch notes from database
//    private void fetchNotes() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                ParseObject.pinAllInBackground(objects, new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        sendDoneBroadcast();
//                    }
//                });
//
//            }
//        });
//    }
//
//    // When fetchNote() is done, a broadcast is sent such that the NotesList will be update
//    private void sendDoneBroadcast() {
//        Intent intent = new Intent("doneFetchingData");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
//
//
//}



import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;


import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.helge.arrhythmiapt.osea.OSEAFactory;
import com.helge.arrhythmiapt.osea.classification.BeatDetectionAndClassification;
import com.helge.arrhythmiapt.osea.classification.ECGCODES;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> mSeries;
    private double graphLastXValue = 0d;
    private double graphLastYValue = 0d;
    public final String ACTION_USB_PERMISSION = "com.a2009pink.revive.USB_PERMISSION";
    Button startButton, sendButton, clearButton, stopButton;
    TextView textView, ECGClassTextView, numberOfSamples;
    EditText editText;
    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    int points = 1;
    List<Integer> pointList;
    int numberOfPoints = 1000;
    DataPoint[] dataPoints = new DataPoint[numberOfPoints];
    byte[] byteArray = new byte[numberOfPoints];
    int[] intECGArray = new int[numberOfPoints];
    private enum ArduinoState {
        STATIC, ECG, CAPACITOR, DELIVERSHOCK
    }
    String STATIC_ARDUINO_CODE = "0";
    String ECG_ARDUINO_CODE = "1";
    String CAPACITOR_ARDUINO_CODE = "2";
    String DELIVER_SHOCK_ARDUINO_CODE = "3";
    private ArduinoState currentAndroidState = ArduinoState.STATIC;


    private void QRSClassification(int[] ecgSamples) {
        int sampleRate = 990;
        tvAppend(ECGClassTextView, "successfully got to QRSclass method");
//        BeatDetectionAndClassification bdac = OSEAFactory.createBDAC(sampleRate, sampleRate/2);
//        for (int i = 0; i < ecgSamples.length; i++) {
//            BeatDetectionAndClassification.BeatDetectAndClassifyResult result = bdac.BeatDetectAndClassify(ecgSamples[i]);
//            if (result.samplesSinceRWaveIfSuccess != 0) {
//                int qrsPosition =  i - result.samplesSinceRWaveIfSuccess;
//                if (result.beatType == ECGCODES.UNKNOWN) {
//                    tvAppend(ECGClassTextView, "A unknown beat type was detected at sample: " + qrsPosition);
//                    System.out.println("A unknown beat type was detected at sample: " + qrsPosition);
//                } else if (result.beatType == ECGCODES.NORMAL) {
//                    tvAppend(ECGClassTextView, "A normal beat type was detected at sample: " + qrsPosition);
//                    System.out.println("A normal beat type was detected at sample: " + qrsPosition);
//                } else if (result.beatType == ECGCODES.PVC) {
//                    tvAppend(ECGClassTextView, "A premature ventricular contraction was detected at sample: " + qrsPosition);
//                    System.out.println("A premature ventricular contraction was detected at sample: " + qrsPosition);
//                }
//            }
//        }

    }

    private void sendArduinoNextState(ArduinoState nextState){
        String sendString = "";
        switch (nextState) {
            case ECG:
                sendString = ECG_ARDUINO_CODE;
                break;
            case DELIVERSHOCK:
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

        serialPort.write(sendString.getBytes());
        currentAndroidState = nextState;
    }


    public void startTestingButtonPressed(View view) {
        Intent intent = new Intent(this, ArduinoTestingActivity.class);
        startActivity(intent);
    }





    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            Integer dataInt = null;




           // if (points < numberOfPoints ){
                try {
                    data = new String(arg0, "UTF-8");
                    //data.concat("/n");
//                    dataInt = Integer.parseInt(data.trim());
//                    intECGArray[points-1] = dataInt;
//                    points += 1;
                    tvAppend(textView, data);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch(NumberFormatException e) {
                } catch(NullPointerException e) {
                }
            //} else if(points == numberOfPoints){
//                Double count = 0d;
//                DataPoint[] values = new DataPoint[points];
//                for(int i = 0; i < values.length ; i++){
//                    int value = pointList.get(i);
//                    values[i] = new DataPoint(count, value);
//                    count +=1;
//                }
//                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(values);
//                GraphView graph = (GraphView) findViewById(R.id.graph);
//                graph.addSeries(series);
            //    numberOfSamples.setText("heyyyyy");
              //  QRSClassification(intECGArray);
           // } else{

          //  }

//            try {
//                data = new String(arg0, "UTF-8");
//                //data.concat("/n");
//                try {
//                    dataInt = Integer.parseInt(data);
//                    points +=1;
//                    graphLastXValue += .1d;
//                    //pointList.add(dataInt);
//                    int pointsMod = points % numberOfPoints -1;
//                    dataPoints[pointsMod] = new DataPoint(pointsMod, dataInt);
//                    //mSeries.appendData(new DataPoint(graphLastXValue, dataInt), true, 1000);
//                } catch(NumberFormatException e) {
//                } catch(NullPointerException e) {
//                }
//
//                tvAppend(textView, data);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            if(points > 1000) {
//                mSeries.resetData(dataPoints);
//            }

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
                            setUiEnabled(true);
                            serialPort.setBaudRate(9600);
                            serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                            serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                            serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                            serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                            serialPort.read(mCallback);
                            tvAppend(textView,"Serial Connection Opened!\n");

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
                onClickStart(startButton);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                onClickStop(stopButton);

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usbManager = (UsbManager) getSystemService(this.USB_SERVICE);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        ECGClassTextView = (TextView) findViewById(R.id.ECGClassTextView);
        numberOfSamples = (TextView) findViewById(R.id.numberOfSamples);

        setUiEnabled(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(broadcastReceiver, filter);

//        GraphView graph = (GraphView) findViewById(R.id.graph);
//        mSeries = new LineGraphSeries<>();
//        System.out.println(graph.getViewport().getMaxX(true));
//        System.out.println("hello");
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(1000);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(1024);
//        graph.addSeries(mSeries);
//        System.out.println(graph.getSeries());

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);


    }

    public void setUiEnabled(boolean bool) {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }

    public void onClickStart(View view) {

        pointList = new ArrayList<Integer>();
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


    }

    public void onClickSend(View view) {
        String string = editText.getText().toString();
        serialPort.write(string.getBytes());
        tvAppend(textView, "\nData Sent : " + string + "\n");

    }

    public void onClickStop(View view) {
        setUiEnabled(false);
        if(serialPort != null){
            serialPort.close();
        }
        tvAppend(textView,"\nSerial Connection Closed! \n");

    }

    public void startTutorial(View view) {
        Intent intent = new Intent(this, FirstTutorialScreen.class);
        startActivity(intent);
    }

    public void onClickClear(View view) {
        textView.setText(" ");
//        graphLastXValue += .1;
//        graphLastYValue += .1;
//        mSeries.appendData(new DataPoint(graphLastXValue, graphLastYValue), true, 40);

    }

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
