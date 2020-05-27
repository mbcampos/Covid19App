package com.matheuscampos.coronavirus;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.JsonReader;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

class Task extends AsyncTask<Void, Void, Void> {

    private TextView txtCasos;
    private TextView txtConfirmados;
    private TextView txtMortes;
    private TextView txtRecuperados;
    private TextView txtLetalidade;
    private Spinner spnStates;

    private List<Country> countries;
    private List<State> states;

    private Context context;

    Task(Context context, List<State> states, List<Country> countries, TextView txtConfirmados, TextView txtCasos, TextView txtRecuperados, TextView txtMortes, TextView txtLetalidade, Spinner spnStates) {

        this.txtCasos = txtCasos;
        this.txtConfirmados = txtConfirmados;
        this.txtMortes = txtMortes;
        this.txtRecuperados = txtRecuperados;
        this.txtLetalidade = txtLetalidade;
        this.spnStates = spnStates;
        this.countries = countries;
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

            myConnection.setRequestProperty("User-Agent", "covid19app-v1.0");
            myConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            myConnection.setRequestProperty("Contact-Me", "matbcampos@gmail.com");

            if (myConnection.getResponseCode() == 200) {

                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                countries.add(readCountry(jsonReader));

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

            myConnectionB.setRequestProperty("User-Agent", "covid19app-v1.0");
            myConnectionB.setRequestProperty("Accept", "application/vnd.github.v3+json");
            myConnectionB.setRequestProperty("Contact-Me", "matbcampos@gmail.com");

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

    private Country readCountry(JsonReader jsonReader) {

        Country country = new Country();

        try {

            jsonReader.beginObject();
            jsonReader.nextName();
            jsonReader.beginObject();

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals("country")) {
                    country.setCountry(jsonReader.nextString());
                } else if (key.equals("cases")) {
                    country.setCases(jsonReader.nextInt());
                } else if (key.equals("confirmed")) {
                    country.setConfirmed(jsonReader.nextInt());
                } else if (key.equals("deaths")) {
                    country.setDeaths(jsonReader.nextInt());
                } else if (key.equals("recovered")) {
                    country.setRecovered(jsonReader.nextInt());
                    //} else if (key.equals("Date")) {
                    //country.setData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();
            jsonReader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return country;

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
                    //state.setData(jsonReader.nextString());
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
        Country country  = countries.get(0);

        txtCasos.setText(country.getCases().toString());
        txtConfirmados.setText(country.getConfirmed().toString());
        txtMortes.setText(country.getDeaths().toString());
        txtRecuperados.setText(country.getRecovered().toString());

        Double letalidade = ( Double.valueOf(country.getDeaths()) / Double.valueOf(country.getConfirmed()) ) * 100;
        txtLetalidade.setText(new DecimalFormat("#.##").format(letalidade) + " %");

        ArrayAdapter<State> arrayStates = new ArrayAdapter<State>(context, R.layout.spinner_layout, states);
        arrayStates.setDropDownViewResource(R.layout.spinner_layout);
        spnStates.setAdapter(arrayStates);
    }
}