package com.twitter.challenge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.twitter.challenge.api.Constants;
import com.twitter.challenge.model.Weather;
import com.twitter.challenge.model.WeatherData;
import com.twitter.challenge.util.Utils;
import com.twitter.challenge.viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    int dayCount = 1;

    ProgressDialog pd;
    TextView temperatureView;
    TextView windTextView;
    ImageView cloudImageView;

    float daySumTemp;
    float daySumWisespeed;
    double daySumCloudiness;
    LinearLayout linearLayout;

    WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage(getString(R.string.str_please_wait));
        pd.setCancelable(false);

        temperatureView = findViewById(R.id.temperature);
        windTextView = findViewById(R.id.windSpeed);
        cloudImageView = findViewById(R.id.cloudImageView);

        linearLayout = findViewById(R.id.nextFiveDaysLayout);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.INSTANCE.isConnected(MainActivity.this)) {
                    weatherViewModel.getDataLoading().setValue(true);
                    linearLayout.removeAllViews();
                    weatherViewModel.fetchFutureDayWeatherData(String.valueOf(dayCount));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.str_connection_error), Toast.LENGTH_LONG).show();
                }

            }
        });

        setupObservers();

        if (Utils.INSTANCE.isConnected(this)) {
            Weather value = weatherViewModel.getWeatherLiveData().getValue();
            if (value == null) {
                weatherViewModel.fetchWeatherData();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.str_connection_error), Toast.LENGTH_LONG).show();
        }

    }

    private void setupObservers() {
        weatherViewModel.getDataLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading == true) {
                pd.show();
            } else {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
        weatherViewModel.getWeatherLiveData().observe(this, weatherData -> {
            if (weatherData != null && weatherData.getWeather() != null) {
                float temp = (float) weatherData.getWeather().getTemp();
                float speed = (float) weatherData.getWind().getSpeed();
                double cloudiness = weatherData.getClouds().getCloudiness();

                temperatureView.setText(getString(R.string.temperature, temp, TemperatureConverter.celsiusToFahrenheit(temp)));
                windTextView.setText(String.format(getString(R.string.windSpeed), speed));
                cloudImageView.setVisibility(cloudiness > 50 ? View.VISIBLE : View.GONE);
            }
        });

        weatherViewModel.getFutureDayWeatherLiveData().observe(this, weatherData -> {
            if (weatherData != null && weatherData.getWeather() != null) {
                float temp = (float) weatherData.getWeather().getTemp();
                float speed = (float) weatherData.getWind().getSpeed();
                double cloudiness = weatherData.getClouds().getCloudiness();
                daySumTemp = daySumTemp + temp;
                daySumWisespeed = daySumWisespeed + speed;
                daySumCloudiness = daySumCloudiness + cloudiness;
                if (dayCount == Constants.NO_OF_DAYS) {
                    LayoutInflater inflater = getLayoutInflater();
                    View myLayout = inflater.inflate(R.layout.weather, null, false);
                    TextView textView1 = myLayout.findViewById(R.id.temperature);
                    TextView textView2 = myLayout.findViewById(R.id.windSpeed);
                    ImageView cloudImageView1 = myLayout.findViewById(R.id.cloudImageView);
                    textView1.setText(getString(R.string.temperature, temp, TemperatureConverter.celsiusToFahrenheit(daySumTemp / 5)));
                    textView2.setText(String.format(getString(R.string.windSpeed), daySumWisespeed / 5));
                    cloudImageView1.setVisibility((daySumCloudiness / 5) > 50 ? View.VISIBLE : View.GONE);
                    linearLayout.addView(myLayout);
                    weatherViewModel.getDataLoading().setValue(false);
                } else {
                    dayCount++;
                    weatherViewModel.fetchFutureDayWeatherData(String.valueOf(dayCount));
                }
            }
        });
    }


}
