package com.matheuscampos.coronavirus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<State> states;
    private List<Country> countries;
    private TextView labelTxt1;
    private TextView txt1;
    private TextView labelTxt2;
    private TextView txt2;
    private TextView labelTxt3;
    private TextView txt3;
    private TextView labelTxt4;
    private TextView txt4;
    private TextView txtLetalidade;
    private Spinner spnStates;
    private String selectedState;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initAttributes();
        runThread();
        spnStatesItemSelectedListener();
    }

    private void runThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Task task = new Task(getApplicationContext(), states, countries, txt1, txt2, txt3, txt4, txtLetalidade, spnStates);
                task.execute();
            }
        });
    }

    private void spnStatesItemSelectedListener() {
        spnStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedState != "" && position > 0) {

                    toggleVisibility(View.GONE);

                    State state = states.get(position);

                    labelTxt1.setText("Confirmados");
                    // Para os estados, "casos" representa os confirmados
                    txt1.setText(state.getCases().toString());
                    labelTxt2.setText("Mortes");
                    txt2.setText(state.getDeaths().toString());

                    int teste = labelTxt3.getDrawingCacheBackgroundColor();
                    labelTxt3.setText("");
                    txt3.setText("");
                    labelTxt4.setText("");
                    txt4.setText("");

                    Double letalidade = ( Double.valueOf(state.getDeaths()) / Double.valueOf(state.getCases()) ) * 100;
                    txtLetalidade.setText(new DecimalFormat("#.##").format(letalidade) + " %");

                } else {
                    if (selectedState == "") {
                        selectedState = String.valueOf(position);
                    } else {
                        
                        toggleVisibility(View.VISIBLE);

                        labelTxt1.setText("Confirmados");
                        txt1.setText(countries.get(0).getConfirmed().toString());
                        labelTxt2.setText("Casos");
                        txt2.setText(countries.get(0).getCases().toString());
                        labelTxt3.setText("Recuperados");
                        txt3.setText(countries.get(0).getRecovered().toString());
                        labelTxt4.setText("Mortes");
                        txt4.setText(countries.get(0).getDeaths().toString());

                        Double letalidade = ( Double.valueOf(countries.get(0).getDeaths()) / Double.valueOf(countries.get(0).getConfirmed()) ) * 100;
                        txtLetalidade.setText(new DecimalFormat("#.##").format(letalidade) + " %");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void toggleVisibility(int visibility) {
        txt3.setVisibility(visibility);
        labelTxt3.setVisibility(visibility);
        txt4.setVisibility(visibility);
        labelTxt4.setVisibility(visibility);
    }

    private void initAttributes() {
        labelTxt1 = findViewById(R.id.labelTxt1);
        txt1 = findViewById(R.id.txt1);
        labelTxt2 = findViewById(R.id.labelTxt2);
        txt2 = findViewById(R.id.txt2);
        labelTxt3 = findViewById(R.id.labelTxt3);
        txt3 = findViewById(R.id.txt3);
        labelTxt4 = findViewById(R.id.labelTxt4);
        txt4 = findViewById(R.id.txt4);

        txtLetalidade = findViewById(R.id.txtLetalidade);
        spnStates = findViewById(R.id.spnStates);

        countries = new ArrayList<Country>();
        states = new ArrayList<State>();

        selectedState = "";
    }
}
