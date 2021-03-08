package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {


    private Button search_bt;
    private EditText search_txt;
    private TextView result,update,desc,temp,tempmin,tempmax;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_bt = findViewById(R.id.btn_search);
        search_txt = findViewById(R.id.country_ed);
        result = findViewById(R.id.result_tv);
        update = findViewById(R.id.update_txt);
        desc = findViewById(R.id.desc_text);
        temp = findViewById(R.id.temp_text);
        tempmin = findViewById(R.id.mintemp_text);
        tempmax = findViewById(R.id.maxtemp_text);

        search_txt.setText("");
        result.setText("");
        update.setText("");
        desc.setText("");
        temp.setText("");
        tempmax.setText("");
        tempmin.setText("");


        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search_txt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter a country to search for  ...", Toast.LENGTH_SHORT).show();
                }else{
                    searchCountry(search_txt.getText().toString());
                }

            }
        });



    }


    public void searchCountry(String input_txt)
    {
        String api_url = "http://api.openweathermap.org/data/2.5/weather?q="+input_txt+"&appid=32b79446f0ba9d989a0e3701367c9d9d";
        Ion.with(MainActivity.this)
                .load(api_url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if(e != null) // fama une Exception
                        {
                            Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onCompleted: ERROR = "+e.getMessage() );
                        }
                        else
                        {
//
                            String response = result.get("cod").getAsString();
                            if(response == "404")
                            {
                                Toast.makeText(MainActivity.this, "The City is not found Search for another one", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                JsonArray weather_json = result.get("weather").getAsJsonArray();
                                JsonObject description_json = weather_json.get(0).getAsJsonObject();
                                String description = description_json.get("description").getAsString();


                                JsonObject main = result.get("main").getAsJsonObject();
                                double temperature = main.get("temp").getAsDouble();

                                double tempe = temperature - 273.15;

                                double tempemin = main.get("temp_min").getAsDouble();
                                double tempemax = main.get("temp_max").getAsDouble();



                                String time_zone = result.get("timezone").getAsString();


                                update.setText("Updated at "+time_zone);

                                tempmax.setText(tempemax-273+"°C");
                                tempmin.setText(tempemin-273+"°C");

                                temp.setText(tempe+"°C");

                                desc.setText(description);


                            }
                        }

                    }
                });


    }
}