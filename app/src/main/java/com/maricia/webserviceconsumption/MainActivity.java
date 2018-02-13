package com.maricia.webserviceconsumption;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText customerIdEditText;  //customerid
    private EditText customerFirstNameEditText; //customer first name
    private EditText customerLastNameEditText; // customer last name
    private EditText customerCityEditText; //customer city
    private EditText customerStreetEditText; // customer street
    private Button pullBtn; // pull


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetEditTextFields();
        SetTextEditable();
        Log.d(TAG, "onCreate: 1");
    }

    private void GetEditTextFields(){
        Log.d(TAG, "GetEditTextFields: 2");
        customerIdEditText = this.findViewById(R.id.customerIdEditText);
        customerFirstNameEditText = this.findViewById(R.id.customerFirstNameEditText);
        customerLastNameEditText = this.findViewById(R.id.customerLastNameEditText);
        customerCityEditText = this.findViewById(R.id.customerCityEditText);
        customerStreetEditText = this.findViewById(R.id.customerStreetEditText);

    }

    private void SetTextEditable(){
        Log.d(TAG, "SetTextEditable: 3");
        customerIdEditText.setEnabled(true);
        customerFirstNameEditText.setEnabled(false);
        customerLastNameEditText.setEnabled(false);
        customerStreetEditText.setEnabled(false);
        customerCityEditText.setEnabled(false);
    }

    private void GetButtons(){
        Log.d(TAG, "GetButtons: 4");
        pullBtn = this.findViewById(R.id.pullBtn);
    }


    //button
    public void onClick(View arg0){
        GetButtons();
        Log.d(TAG, "onClick: 5");
        pullBtn.setEnabled(false); //disable the button
        //pullBtn.setClickable(true); //disable the button
        new LoadWebURL().execute(); //call inner class
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //allows menu to work
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    //menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.action_about){
            //TODO do something when clicked
            Intent intent =  new Intent (this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //inner class
    private class LoadWebURL extends AsyncTask<Void, Void, String> {

        private static final String TAG = "LoadWebURL";


        //method to read in website
        protected String readStream(InputStream in){

            StringBuilder sb = new StringBuilder();
            BufferedReader reader;

            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        /*
         * You should put those long process code inside this method
         * On Background thread
         * @see android.os.AsyncTask#doInBackground(Params[])
         */

        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: 6");
            URL url;
            HttpURLConnection urlConnection;
            String text = null;

            try {
                url = new URL("http://www.thomas-bayer.com/sqlrest/CUSTOMER/20");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream in = urlConnection.getInputStream();
                text = readStream(in);

            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            Log.d(TAG, "doInBackground: " + text);
            return text;
        }

        /*
                * Before the execution of doInBackground, you can do things here.
                * On UI thread
                * @see android.os.AsyncTask#onPreExecute()
                */
        @Override
        protected void onPreExecute() {
            //Toast.makeText(getApplicationContext(), "onPreExecute is working...", Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(String results){
            Log.d(TAG, "onPostExecute: 7 " + results);
            if (results != null){
                //TODO
                // call method to input data in frontend editText fields
                Log.d(TAG, "onPostExecute: 8" + results);
            }
            //TODO
                //enable button
                // pullBtn.setClickable(true);
                // pullBtn.setEnabled(false);

        }

    }//end class LoadWebURL

}
