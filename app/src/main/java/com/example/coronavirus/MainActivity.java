package com.example.coronavirus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private List<State> states;
    private List<Historico> historicoCasos;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        historicoCasos = new ArrayList<Historico>();
        states = new ArrayList<State>();

        selectedState = "";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Task task = new Task(getApplicationContext(), states, historicoCasos, txt1, txt2, txt3, txt4, txtLetalidade, spnStates);
                task.execute();

            }
        });

        spnStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedState != "" && position > 0) {

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
                        labelTxt1.setText("Confirmados");
                        txt1.setText(historicoCasos.get(0).getConfirmed().toString());
                        labelTxt2.setText("Casos");
                        txt2.setText(historicoCasos.get(0).getCases().toString());
                        labelTxt3.setText("Recuperados");
                        txt3.setText(historicoCasos.get(0).getRecovered().toString());
                        labelTxt4.setText("Mortes");
                        txt4.setText(historicoCasos.get(0).getDeaths().toString());

                        Double letalidade = ( Double.valueOf(historicoCasos.get(0).getDeaths()) / Double.valueOf(historicoCasos.get(0).getConfirmed()) ) * 100;
                        txtLetalidade.setText(new DecimalFormat("#.##").format(letalidade) + " %");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}


class Task extends AsyncTask<Void, Void, Void> {

    private TextView txtCasos;
    private TextView txtConfirmados;
    private TextView txtMortes;
    private TextView txtRecuperados;
    private TextView txtLetalidade;
    private Spinner spnStates;

    private List<Historico> historicoCasos;
    private List<State> states;

    private Context context;

    Task(Context context, List<State> states, List<Historico> historicoCasos, TextView txtConfirmados, TextView txtCasos, TextView txtRecuperados, TextView txtMortes, TextView txtLetalidade, Spinner spnStates) {

        this.txtCasos = txtCasos;
        this.txtConfirmados = txtConfirmados;
        this.txtMortes = txtMortes;
        this.txtRecuperados = txtRecuperados;
        this.txtLetalidade = txtLetalidade;
        this.spnStates = spnStates;
        this.historicoCasos = historicoCasos;
        this.states = states;
        this.context = context;

        State state = new State();
        state.setState("Selecione um estado");
        this.states.add(state);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Void... voids) {

        try {

            URL githubEndpoint = new URL("https://covid19-brazil-api.now.sh/api/report/v1/brazil");
            HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();

            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            myConnection.setRequestProperty("Contact-Me", "hathibelagal@example.com");

            if (myConnection.getResponseCode() == 200) {

                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                historicoCasos.add(readHistorico(jsonReader));

                myConnection.disconnect();

            } else {
                // Error handling code goes here
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        try {
            URL covid19BrazilEndpoint = new URL("https://covid19-brazil-api.now.sh/api/report/v1");
            HttpsURLConnection myConnectionB = (HttpsURLConnection) covid19BrazilEndpoint.openConnection();

            myConnectionB.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            myConnectionB.setRequestProperty("Accept", "application/vnd.github.v3+json");
            myConnectionB.setRequestProperty("Contact-Me", "hathibelagal@example.com");

            if (myConnectionB.getResponseCode() == 200) {

                InputStream responseBody = myConnectionB.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginObject();
                jsonReader.nextName();
                jsonReader.beginArray();

                while (jsonReader.hasNext()) {
                    states.add(readState(jsonReader));
                }

                jsonReader.endArray();
                jsonReader.endObject();

                myConnectionB.disconnect();

            } else {
                // Error handling code goes here
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Historico readHistorico(JsonReader jsonReader) {

        Historico historico = new Historico();

        try {

            jsonReader.beginObject();
            jsonReader.nextName();
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("country")) {
                    historico.setCountry(jsonReader.nextString());
                } else if (key.equals("cases")) {
                    historico.setCases(jsonReader.nextInt());
                } else if (key.equals("confirmed")) {
                    historico.setConfirmed(jsonReader.nextInt());
                } else if (key.equals("deaths")) {
                    historico.setDeaths(jsonReader.nextInt());
                } else if (key.equals("recovered")) {
                    historico.setRecovered(jsonReader.nextInt());
                //} else if (key.equals("Date")) {
                    //historico.setData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return historico;

    }

    private State readState(JsonReader jsonReader) {

        State state = new State();

        try {

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("state")) {
                    state.setState(jsonReader.nextString());
                } else if (key.equals("cases")) {
                    state.setCases(jsonReader.nextInt());
                } else if (key.equals("deaths")) {
                    state.setDeaths(jsonReader.nextInt());
                } else if (key.equals("refuses")) {
                    state.setRefuses(jsonReader.nextInt());
                } else if (key.equals("suspects")) {
                    state.setSuspects(jsonReader.nextInt());
                //} else if (key.equals("Date")) {
                //historico.setData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;

    }

    protected void onPostExecute(Void result) {
        Historico historico  = historicoCasos.get(0);

        txtCasos.setText(historico.getCases().toString());
        txtConfirmados.setText(historico.getConfirmed().toString());
        txtMortes.setText(historico.getDeaths().toString());
        txtRecuperados.setText(historico.getRecovered().toString());

        Double letalidade = ( Double.valueOf(historico.getDeaths()) / Double.valueOf(historico.getConfirmed()) ) * 100;
        txtLetalidade.setText(new DecimalFormat("#.##").format(letalidade) + " %");

        ArrayAdapter<State> arrayStates = new ArrayAdapter<State>(context, R.layout.spinner_layout, states);
        arrayStates.setDropDownViewResource(R.layout.spinner_layout);
        spnStates.setAdapter(arrayStates);
    }
}
