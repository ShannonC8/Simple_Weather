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
    private static final String key = ""; //get your own api key
    private static final String weatherPlace = "London";
    public String re = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView intro = findViewById(R.id.intro);
        intro.setText("The weather in " + weatherPlace + " is");
        getWeather(weatherPlace);
    }

    /**
     * This is the function that will both get the weather and update it to the screen
     * @param place The city name that we want to choose
     */
    public void getWeather(String place) {
        //Creating the URL
        String urlFilled = url + place + "&appid=" + key + "&units=imperial";
        //We need to create a new thread because android studio has restrictions
        // on accessing the internet on the main thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //setting the textview
                    TextView degrees = findViewById(R.id.degrees);
                    String result = ""; //the url result
                    URL url = new URL(urlFilled); //creating new url
                    URLConnection urlConn = url.openConnection(); //creating url connections
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(urlConn.getInputStream())); //reading url
                    String line;
                    while ((line = rd.readLine()) != null) { //putting it into string
                        result += line;
                    }
                    rd.close(); //close
                    //regex pattern to search
                    Pattern pattern = Pattern.compile("\"temp\":(\\d+.\\d+)"); //"temp": (\d+.\d+)
                    Matcher matcher = pattern.matcher(result);
                    String res = "";
                    if (matcher.find()) { //if match is found
                        res = matcher.group(1); //first match is the result
                    }
                    String finalRes = res; //much be final in order to use in runOnUIThread
                    runOnUiThread(new Runnable() { //need to use this because degrees cannot be called in thread
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