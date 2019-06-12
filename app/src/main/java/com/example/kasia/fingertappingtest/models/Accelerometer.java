package com.example.kasia.fingertappingtest.models;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kfojc on 04.10.2018.
 */

public class Accelerometer implements SensorEventListener {

    float aX, aY, aZ;
    boolean isRunning;
    double time0;
    boolean startTime;
    String time;
    List<String> lineList = new ArrayList<>();


    public void startRunning(){
        Patient p = Patient.getActivePatient();
        String patientDesc = p.getName()+","+p.getDescription();
        lineList.add("name,age,sex,group,handedness,tremor,remarks");
        lineList.add(patientDesc);
        lineList.add("timestamp, aX, aY, aZ");
        isRunning=true;
        startTime=true;
    }

    public void stopRunning(){
        isRunning=false;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isRunning) {

            int sensorType = sensorEvent.sensor.getType();
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {

                aX = sensorEvent.values[0]; //składowa X przyspieszenia
                aY = sensorEvent.values[1]; //składowa Y przyspieszenia
                aZ = sensorEvent.values[2]; //składowa Z przyspieszenia

                if (startTime) {
                    time0 = sensorEvent.timestamp / 1000000000.0;
                    startTime = false;
                }


                time = (Double.toString(Math.round((sensorEvent.timestamp / 1000000000.0 - time0) * 1000.0) / 1000.0));

                String line = time + "," + Float.toString(aX) + "," + Float.toString(aY) + "," + Float.toString(aZ);
                lineList.add(line);


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public List<String> getLineList(){

        return lineList;
    }
}
