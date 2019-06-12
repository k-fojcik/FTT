package com.example.kasia.fingertappingtest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kasia.fingertappingtest.models.Patient;
import com.example.kasia.fingertappingtest.R;

public class ThirdActivity extends Activity {

    public static final String EXTRA_PATIENTID="patientId";
    private Patient patient;
    private TextView id_txt;
    private TextView name_txt;
    private TextView age_txt;
    private TextView sex_txt;
    private TextView group_txt;
    private TextView handedness_txt;
    private TextView tremor_txt;
    private TextView remarks_txt;
    private TextView results_txt;
    private TextView resultsInfo_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        setTitle("Patient Information");

        //patient = (Patient) getIntent().getSerializableExtra(EXTRA_PATIENTID);
        patient = Patient.getActivePatient();

        id_txt = (TextView) findViewById(R.id.ID);
        name_txt = (TextView) findViewById(R.id.name);
        age_txt = (TextView) findViewById(R.id.age);
        sex_txt = (TextView) findViewById(R.id.sex);
        tremor_txt = (TextView) findViewById(R.id.tremor);
        group_txt = (TextView) findViewById(R.id.group);
        handedness_txt = (TextView) findViewById(R.id.handedness);
        remarks_txt = (TextView) findViewById(R.id.remarks);

        resultsInfo_txt = (TextView) findViewById(R.id.wynikiInfo);
        results_txt = (TextView) findViewById(R.id.wyniki);

        int id = Patient.getPatients().indexOf(patient);
        id_txt.setText(Integer.toString(id));
        name_txt.setText(patient.getName());
        age_txt.setText(patient.getAge());
        sex_txt.setText(patient.getSex());
        group_txt.setText(patient.getGroup());
        handedness_txt.setText(patient.getHandedness());
        tremor_txt.setText(patient.getTremor());
        remarks_txt.setText(patient.getRemarks());

        String r = "Correct taps: R - " + Patient.getPatients().get(id).getCorrectTapsR() + "  L - "
                   + Patient.getPatients().get(id).getCorrectTapsL() + "\nAlternation Errors: R - "
                   + Patient.getPatients().get(id).getAlternationErrorsR() + "  L - "
                   + Patient.getPatients().get(id).getAlternationErrorsL() + "\nAccuracy Errors: R - "
                   + Patient.getPatients().get(id).getAccuracyErrorsR() + "  L - "
                   + Patient.getPatients().get(id).getAccuracyErrorsL();

        results_txt.setText(r);
        String s = "Results: class - "+Patient.getPatients().get(id).getModelClass();
        resultsInfo_txt.setText(s);
    }

    public void onClickStartBtn(View view){

        Intent intent = new Intent(ThirdActivity.this, RightHandTest.class);

        intent.putExtra(RightHandTest.EXTRA_DATA,true);
        startActivity(intent);
    }
}
