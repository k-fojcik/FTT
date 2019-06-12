package com.example.kasia.fingertappingtest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kasia.fingertappingtest.models.Patient;
import com.example.kasia.fingertappingtest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SaveActivity extends Activity {

    private List<String> rightHandTest;
    private List<String> leftHandTest;
    private List<String> accelRightHandTest;
    private List<String> accelLeftHandTest;
    private String patientID;
    EditText filenameRight;
    EditText filenameLeft;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        setTitle("Save Data");

        //patient = (Patient) getIntent().getSerializableExtra(EXTRA_DATA);
        patient=Patient.getActivePatient();
        //rightHandTest = getIntent().getStringArrayListExtra(MY_MESSAGE);
        //leftHandTest = getIntent().getStringArrayListExtra(MY_MESSAGE2);
        //accelRightHandTest = getIntent().getStringArrayListExtra(MY_MESSAGE3);
        //accelLeftHandTest = getIntent().getStringArrayListExtra(MY_MESSAGE4);

        rightHandTest = patient.getTapLineListR();
        leftHandTest = patient.getTapLineList();
        accelRightHandTest = patient.getAccelLineListR();
        accelLeftHandTest = patient.getAccelLineList();


        filenameRight = findViewById(R.id.filenameR);
        filenameLeft = findViewById(R.id.filenameL);
        patientID = Patient.getPatients().indexOf(patient) + "_" + patient.getName();

        String filename = (patientID+"_RightHand");
        String filename2 = (patientID+"_LeftHand");
        filenameRight.setText(filename);
        filenameLeft.setText(filename2);
    }

    public void onClickSaveBtn(View view){

        String f = filenameRight.getText().toString()+".csv";
        saveToFile(rightHandTest, "/TEST/", f);

        String f2 = filenameLeft.getText().toString()+".csv";
        saveToFile(leftHandTest, "/TEST/", f2);

        String f3 = filenameRight.getText().toString()+"accel.csv";
        saveToFile(accelRightHandTest, "/TEST/", f3);

        String f4 = filenameLeft.getText().toString()+"accel.csv";
        saveToFile(accelLeftHandTest, "/TEST/", f4);

    }

    public void onClickAgainBtn(View view){
        Intent intent = new Intent(SaveActivity.this, ThirdActivity.class);
        //intent.putExtra(ThirdActivity.EXTRA_PATIENTID, patient);
        startActivity(intent);
    }

    public void onClickBackBtn(View view){

        Intent intent = new Intent(SaveActivity.this, Registration.class);
        startActivity(intent);

    }

    private void saveToFile(List<String> data, String folder, String fileName) {



            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + folder);

            dir.mkdirs();
            File file = new File(dir, fileName);

            String test = file.getAbsolutePath();
            Log.i("My", "FILE LOCATION: " + test);

            try {
                FileOutputStream f = new FileOutputStream(file);
                PrintWriter pw = new PrintWriter(f);


                for (int i = 0; i < data.size(); i++) {
                    pw.println(data.get(i));
                }

                pw.flush();
                pw.close();
                f.close();

                Toast.makeText(getApplicationContext(),

                        "Data saved",

                        Toast.LENGTH_SHORT).show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("My", "******* File not found. Did you" +
                        " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
