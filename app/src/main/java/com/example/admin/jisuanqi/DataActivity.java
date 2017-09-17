package com.example.admin.jisuanqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_data1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.data_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        //下拉列表选择事件监听器
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                if(i>=0)
                    Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(i).toString(),
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_data2);
        adapter= ArrayAdapter.createFromResource(this,
                R.array.data_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        //下拉列表选择事件监听器
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                if(i>=0)
                    Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(i).toString(),
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
