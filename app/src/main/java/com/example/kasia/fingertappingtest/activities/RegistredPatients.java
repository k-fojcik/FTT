package com.example.kasia.fingertappingtest.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kasia.fingertappingtest.models.Patient;
import com.example.kasia.fingertappingtest.R;

import java.util.List;


public class RegistredPatients extends Activity {

    public static final String MY_MESSAGE="myMessage";
    private ListView list ;
    private List<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registred_patients);
        setTitle("Registred Patients");
        patients = Patient.getPatients();

        list = (ListView) findViewById(R.id.patients);

        CustomAdapter customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);

        AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Patient.setActivePatient(patients.get(position));
                Intent intent = new Intent(RegistredPatients.this, ThirdActivity.class);
                //intent.putExtra(ThirdActivity.EXTRA_PATIENTID, patients.get(position));
                startActivity(intent);
            }
        };
        list.setOnItemClickListener(itemClickListener);
    }

    class CustomAdapter extends BaseAdapter {

        private Context context;

        @Override
        public int getCount() {
            return patients.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().
                    inflate(R.layout.listview_layout, viewGroup, false);
            TextView txtName = (TextView) view.findViewById(R.id.textView_name);
            TextView txtDescription = (TextView) view.findViewById(R.id.textView_description);

            txtName.setText(patients.get(i).getName());
            txtDescription.setText(patients.get(i).getDescription());

            return view;
        }
    }
}
