package com.mahin.openweather;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Weather extends AsyncTask<String,Void,String>{
    String result;
    String[] tempForFiveDays =new String[50];
    String[] windForFiveDays =new String[50];
    String[] humidForFiveDays =new String[50];
    String[] timeForFiveDays =new String[5000];
    String[] detailsForFiveDays =new String[40];

    @Override
    protected String doInBackground(String... urls) {
        result="";
        URL link;
        HttpURLConnection myConnection=null;

        try{
            link= new URL(urls[0]);
            myConnection=(HttpURLConnection) link.openConnection();
            InputStream in=myConnection.getInputStream();
            InputStreamReader myStreamReader=new InputStreamReader(in);
            int data=myStreamReader.read();
            while(data!=-1)
            {
                char current=(char) data;
                result+=current;
                data=myStreamReader.read();
            }
            return result;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            String temp = "";
            JSONObject myObject = new JSONObject(result);
            JSONObject city = new JSONObject(myObject.getString("city"));
            JSONArray object = myObject.getJSONArray("list");

            for (int i = 0; i < object.length(); i++) {
                JSONObject objects = object.getJSONObject(i);
                JSONObject main = objects.getJSONObject("main");
                JSONObject wind = objects.getJSONObject("wind");
                temp = main.getString("temp");
                String humidity = main.getString("humidity");
                String windSpeed = wind.getString("speed");
                String timeDate = objects.getString("dt_txt");
                tempForFiveDays[i] = temp;
                humidForFiveDays[i] = humidity;
                windForFiveDays[i] = windSpeed;
                timeForFiveDays[i] = timeDate;

            }

            String placeName = city.getString("name");
            String countryName=city.getString("country");
            String tempNow = tempForFiveDays[1];
            String humidNow = humidForFiveDays[1];
            String windNow = windForFiveDays[1];

            if (MainActivity.temp.getText().toString().equals("Main°C")) {
                MainActivity.place.setText(placeName);
                MainActivity.temp.setText(tempNow);
                MainActivity.humidityHere.setText(humidNow);
                MainActivity.windHere.setText(windNow);
                MainActivity.country.setText(countryName);
                MainActivity.details.setVisibility(View.INVISIBLE);

            } else {
                MainActivity.place.setText(placeName);
                MainActivity.country.setText(countryName);
                MainActivity.temp.setText("");

                for (int i = 0; i < object.length(); i++) {

                    if (i!=39) {
                        detailsForFiveDays[i]= timeForFiveDays[i] + "\ntemp: "
                                + tempForFiveDays[i] + "°\nhumidity: "
                                + humidForFiveDays[i] + "%\nwind: "
                                + windForFiveDays[i] + "\n\n";
                    }
                    else
                    {
                        detailsForFiveDays[i]= timeForFiveDays[i] + "\ntemp: "
                                + tempForFiveDays[i] + "°\nhumidity: "
                                + humidForFiveDays[i] + "%\nwind: "
                                + windForFiveDays[i];
                    }
                    MainActivity.details.setText(String.join("",detailsForFiveDays));
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
