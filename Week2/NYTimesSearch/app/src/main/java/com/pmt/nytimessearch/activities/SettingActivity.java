package com.pmt.nytimessearch.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.pmt.nytimessearch.R;
import com.pmt.nytimessearch.models.SettingData;
import com.pmt.nytimessearch.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spSort;
    private Button btnSave;
    private CheckBox cbArts;
    private CheckBox cbFashionAndStyle;
    private CheckBox cbSports;
    private static SettingData data;

    private EditText etBeginDate;
    private EditText etEndDate;
    private DatePickerDialog beginDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = (SettingData) getIntent().getSerializableExtra("SettingData");
        Log.d("DEBUG", data.toString());
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        fineView();
        setDateTimeField(data.beginDate, data.endDate);
        fetchDataToDialog();
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                data.beginDate
                data.sortType = spSort.getSelectedItem().toString().toLowerCase();
                if (cbArts.isChecked()) {
                    data.newsDeskValues.add(cbArts.getText().toString());
                }
                if (cbFashionAndStyle.isChecked()) {
                    data.newsDeskValues.add(cbFashionAndStyle.getText().toString());
                }
                if (cbSports.isChecked()) {
                    data.newsDeskValues.add(cbSports.getText().toString());
                }

                //push back data
                Intent returnIntent = new Intent();
                returnIntent.putExtra("SettingData",data);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == etBeginDate) {
            beginDatePickerDialog.show();
        }else if(view == etEndDate) {
            endDatePickerDialog.show();
        }
    }

    private void setDateTimeField(Calendar dateBegin, Calendar dateEnd) {
        etBeginDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        if(dateBegin == null) dateBegin = Calendar.getInstance();
        if(dateEnd == null) dateEnd = Calendar.getInstance();
        beginDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etBeginDate.setText(dateFormatter.format(newDate.getTime()));
                data.beginDate = newDate;
            }

        },dateBegin.get(Calendar.YEAR), dateBegin.get(Calendar.MONTH), dateBegin.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etEndDate.setText(dateFormatter.format(newDate.getTime()));
                data.endDate = newDate;
            }

        },dateEnd.get(Calendar.YEAR), dateEnd.get(Calendar.MONTH), dateEnd.get(Calendar.DAY_OF_MONTH));
    }

    private void fineView(){
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashionAndStyle = (CheckBox) findViewById(R.id.cbFashionAndStyle);
        cbSports = (CheckBox) findViewById(R.id.cbSports);
        etBeginDate = (EditText) findViewById(R.id.etBeginDate);
        etEndDate = (EditText) findViewById(R.id.etEndDate);
        spSort = (Spinner) findViewById(R.id.spSortType);
        btnSave = (Button) findViewById(R.id.btnSave);
        //set default data
        List<String> categories = new ArrayList<String>();
//        categories.add(Constants.SORT_TYPE_NEW);
//        categories.add(Constants.SORT_TYPE_OLD);
        categories.add("newest");
        categories.add("oldest");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spSort.setAdapter(dataAdapter);
        spSort.setSelection(0);
    }

    private void fetchDataToDialog(){
        if(data.beginDate != null){
            etBeginDate.setText(dateFormatter.format(data.beginDate.getTime()));
        }
        if(data.endDate != null){
            etEndDate.setText(dateFormatter.format(data.endDate.getTime()));
        }
        if(data.sortType.equalsIgnoreCase(Constants.SORT_TYPE_OLD)){
            spSort.setSelection(1);
        }else{
            spSort.setSelection(0);
        }

        if(data.newsDeskValues != null && data.newsDeskValues.size() > 0){
            for (String str : data.newsDeskValues) {
                if(str.equals(Constants.NEWS_DESK_VALUES_ARTS)){
                    cbArts.setChecked(true);
                }else if(str.equals(Constants.NEWS_DESK_VALUES_FASHION_AND_STYLE)){
                    cbFashionAndStyle.setChecked(true);
                }else if(str.equals(Constants.NEWS_DESK_VALUES_SPORTS)){
                    cbSports.setChecked(true);
                }
            }
        }
    }
}
