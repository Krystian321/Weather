package com.example.studentwsb.appka;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView resultTextView;
    EditText editText;

    public void getWeather(View view){
        DownloadTask task = new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b79d5066d8a2e4503061b4dadc0f1ad6&lang=pl");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        editText = findViewById(R.id.editText);
    }

    public class DownloadTask extends AsyncTask<String,Void,String > {


        @Override
        protected String doInBackground(String... urls) {
            String json = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    json += current;
                    data = reader.read();
                }
                return json;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Blad";
            } catch (IOException e) {
                e.printStackTrace();
                return "Blad";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray array = new JSONArray(weatherInfo);


                for(int i = 0; i<array.length();i++){
                    JSONObject jsonPart = array.getJSONObject(i);
                    Log.i("Main", jsonPart.getString("main"));
                    Log.i("Description", jsonPart.getString("description"));
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");;
                    if(!main.equals("") && !description.equals("")){
                        resultTextView.setText(main+ " : " + description);
                    }

                }













            } catch (JSONException e) {
                e.printStackTrace();

            }
        }



    }
}
