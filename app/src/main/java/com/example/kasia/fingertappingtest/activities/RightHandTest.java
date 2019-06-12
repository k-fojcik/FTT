package com.example.kasia.fingertappingtest.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kasia.fingertappingtest.models.Accelerometer;
import com.example.kasia.fingertappingtest.models.Patient;
import com.example.kasia.fingertappingtest.R;

import java.util.ArrayList;
import java.util.List;

public class RightHandTest extends Activity {

    public static final String EXTRA_DATA="patientData";
    public static final String MY_MESSAGE = "myMess";
    public static final String MY_MESSAGE2 = "myMess2";

    private Button startBtn;
    private Button rightBtn;
    private Button leftBtn;
    private boolean next=false;
    private boolean start2=false;
    private int counter=0;
    private List<String> tapLineList = new ArrayList<>();
    private List<String> accelLineList = new ArrayList<>();
    private List<String> tapList = new ArrayList<>();
    private List<Double> timeList = new ArrayList<>();
    long startTime;
    LinearLayout tapSpace;
    TextView tapCounter;
    String patientInfo;
    ProgressBar progressBar;
    private Patient patient;
    private boolean rightHand=true;

    private SensorManager mySensorManager; //manadzer czujnikow
    private Sensor myAccelerometer; //obiekt klasy Sensor (czyli czujnik)
    private Accelerometer accel = new Accelerometer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_test);

        patient = Patient.getActivePatient();
        patientInfo=patient.getName()+","+patient.getDescription();

        rightHand = getIntent().getExtras().getBoolean(EXTRA_DATA);

        //if(tapLineListR!=null) {
        if(!rightHand){
            //rightHand = false;
            setTitle("Left Hand Test");
        }
        else
            setTitle("Right Hand Test");



        startBtn = (Button) findViewById(R.id.startBtn);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        leftBtn = (Button) findViewById(R.id.leftBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
        tapSpace = (LinearLayout) findViewById(R.id.tapSpace2);
        tapCounter = (TextView) findViewById(R.id.tapsCounter);


        tapSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(start2) {
                        onTouchListener( x, y, "DOWN","N");
                    }
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(start2) {
                        onTouchListener( x, y, "UP","N");
                    }
                    return true;
                }
                return false;
            }
        });

        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(start2) {
                        onTouchListener( x, y, "DOWN","L");
                    }
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(start2) {
                        onTouchListener( x, y, "UP","L");
                    }
                    return true;
                }
                return false;
            }
        });

        rightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(start2) {
                        onTouchListener( x, y, "DOWN","R");
                    }
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(start2) {
                        onTouchListener( x, y, "UP","R");
                    }
                    return true;
                }
                return false;
            }
        });


        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mySensorManager.registerListener(accel, myAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

    }


    public void onClickStartBtn(View view){

        if(!next) {
            startBtn.setEnabled(false);

            new CountDownTimer(4000, 1000) {


                public void onTick(long millisUntilFinished) {
                    startBtn.setText(Integer.toString((int) millisUntilFinished / 1000));
                }

                public void onFinish() {
                    startBtn.setText("0!");
                    start2 = true;
                    tapLineList.clear();
                    tapLineList.add("name,age,sex,group,handedness,tremor,remarks");
                    tapLineList.add(patientInfo);
                    tapLineList.add("time,UP|DOWN,X,Y,R|L|N");
                }
            }.start();
        }
        else{

            if(rightHand){

                int id = Patient.getPatients().indexOf(patient);

                Patient.getPatients().get(id).setParams(rightHand, tapList, timeList);

                patient.setTapLineListR(tapLineList);
                patient.setAccelLineListR(accelLineList);
                Intent intent = new Intent(this,RightHandTest.class);
                intent.putExtra(RightHandTest.EXTRA_DATA,false);
                startActivity(intent);
            }
            else {

                int id = Patient.getPatients().indexOf(patient);

                Patient.getPatients().get(id).setParams(rightHand, tapList, timeList);
                Patient.getPatients().get(id).setModelClass();

                Intent intent = new Intent(this, SaveActivity.class);

                patient.setTapLineList(tapLineList);
                patient.setAccelLineList(accelLineList);

                startActivity(intent);
            }
        }

    }

    private void onTouchListener(float x, float y, String k, String l){

        if (startTime == 0) {
            startTime = SystemClock.elapsedRealtime();
            startBtn.setVisibility(View.INVISIBLE);
            accel.startRunning();
        }
        if(k=="DOWN")
        counter++;

        tapCounter.setText(Integer.toString(counter));

        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        double elapsedSeconds = elapsedMilliSeconds / 1000.0;
        if (elapsedSeconds > 20.0) {
            start2 = false;
            rightBtn.setEnabled(false);
            leftBtn.setEnabled(false);
            progressBar.setProgress(20);
            startBtn.setText("NEXT >>");
            startBtn.setVisibility(View.VISIBLE);
            startBtn.setEnabled(true);
            next=true;
            accel.stopRunning();
            accelLineList = accel.getLineList();
        } else{
            tapLineList.add(Double.toString(elapsedSeconds) + "," + k + "," + Float.toString(x) + "," + Float.toString(y)+","+l);
            progressBar.setProgress(Math.round((float)elapsedSeconds));
            if(k=="DOWN") {
                tapList.add(l);
                timeList.add(elapsedSeconds);
            }
        }
    }

}
