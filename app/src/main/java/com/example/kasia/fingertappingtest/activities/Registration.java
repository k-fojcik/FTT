package com.example.kasia.fingertappingtest.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;


import com.example.kasia.fingertappingtest.models.Patient;
import com.example.kasia.fingertappingtest.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Registration extends Activity {
    private EditText name;
    private EditText birthdate;
    private RadioButton male;
    private RadioButton female;
    private RadioButton control;
    private RadioButton PD;
    private RadioButton right;
    private RadioButton left;
    private RadioButton right2;
    private RadioButton left2;
    private RadioButton none;
    private RadioButton both;
    private EditText remarks;
    private List<String> patientInfo;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Patient Registration");

        try {
            Patient.setModel(getAssets().open("ACPD.model"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
// If do not grant write external storage permission.
        if(writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED)
        {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(Registration.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }


        name = findViewById(R.id.name);
        birthdate = findViewById(R.id.birthdate);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        remarks = findViewById(R.id.remarks);
        control = findViewById(R.id.control);
        PD = findViewById(R.id.PD);
        right = findViewById(R.id.right);
        right2 = findViewById(R.id.right2);
        left = findViewById(R.id.left);
        left2 = findViewById(R.id.left2);
        none = findViewById(R.id.none);
        both = findViewById(R.id.both);
        patientInfo = new ArrayList<>();
    }

    public void onClickRegisterBtn(View view){
        patientInfo.clear();
        String d1=name.getText().toString();
        String d2=birthdate.getText().toString();
        String d4=remarks.getText().toString();
        String d3="";
        String d5="";
        String d6="";
        String d7="";

        if(male.isChecked()) d3="male";
        else if (female.isChecked()) d3="female";

        if(control.isChecked()) d5="control";
        else if (PD.isChecked()) d5="PD";

        if(right.isChecked()) d6="right";
        else if (left.isChecked()) d6="left";

        if(right2.isChecked()) d7="right";
        else if (left2.isChecked()) d7="left";
        else if (none.isChecked()) d7="none";
        else if (both.isChecked()) d7="both";

        if (d1.equals("")||d2.equals("")||d3.equals("")||d5.equals("")||d6.equals("")||d7.equals("")){
            Toast.makeText(getApplicationContext(),

                    "Data incomplete",

                    Toast.LENGTH_LONG).show();
        }
        else{
            Patient.addPatient(new Patient(d1, d2, d3, d5, d6, d7, d4));
            Intent intent = new Intent(this,RegistredPatients.class);
            startActivity(intent);
        }

    }

    public void onClickRegPatBtn(View view){
        Intent intent = new Intent(this,RegistredPatients.class);
        startActivity(intent);

    }
}
