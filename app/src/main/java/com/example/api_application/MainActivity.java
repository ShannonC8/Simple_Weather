package com.example.api_application;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//https://openweathermap.org/current#name
public class MainActivity extends AppCompatActivity {
    private static final String url = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String key = "2cbb1a1acfa81cd5920476981398fc5d";
    private static final String weatherPlace = "Cincinnati";
    public String re = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWeather(weatherPlace);
    }

    public void getWeather(String place) {
        String urlFilled = url + place + "&appid=" + key + "&units=imperial";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String res = "";
                try {
                    TextView intro = findViewById(R.id.intro);
                    intro.setText("The weather in " + weatherPlace + " is");
                    TextView degrees = findViewById(R.id.degrees);
                    String result = "";
                    URL url = new URL(urlFilled);
                    URLConnection urlConn = url.openConnection();
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(urlConn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result += line;
                    }
                    rd.close();
                    Pattern pattern = Pattern.compile("\"temp\":(\\d+.\\d+)"); //"temp": (\d+.\d+)
                    Matcher matcher = pattern.matcher(result);
                    if (matcher.find()) {
                        res = matcher.group(1);
                    }
                    String finalRes = res;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            degrees.setText(finalRes);
                        }
                    });

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        thread.start();
    }
}