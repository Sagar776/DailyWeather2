package com.sagar.dailyweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sagar.dailyweather.data.Channel;
import com.sagar.dailyweather.data.Item;
import com.sagar.dailyweather.services.WeatherServiceCallback;
import com.sagar.dailyweather.services.YahooWeatherService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherimageView;
    private TextView locationTextView;
    private TextView conditionTextView;
    private TextView tempTextView;

    private YahooWeatherService services;
    private ProgressDialog dialog;


    private Button forecast;


    int requestCode;


    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherimageView = (ImageView) findViewById(R.id.weatherIconimageView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTexttView);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        forecast=(Button)findViewById(R.id.forecastButton);

        services = new YahooWeatherService(this);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data");
        dialog.show();

        Log.d("daily", "getweatherdata() before");
        getweatherData();
        Log.d("daily", "getweatherdata() Invoked");
    }





    void getweatherData()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("daily", "getweather body entered");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("daily", "onLocationChanged: Invoked");



               double latitude=location.getLatitude();
                double longitude=location.getLongitude();

                Geocoder geocoder = new Geocoder((Context) locationListener, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                services.refreshWeather(cityName);

                Log.d("daily","Location="+cityName);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                    Log.d("daily","Disabled");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode);


            return;
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, 5000, 1000, locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getweatherData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(this.requestCode==requestCode){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
            else{

            }
        }

    }

    @Override
    public void serviceSuccess(Channel channel) {

        dialog.hide();

        Item item = channel.getItem();
        int resId=getResources().getIdentifier("drawable/icon"+ item.getCondition().getCode(),null,getPackageName());
        Drawable weatherIcon=getResources().getDrawable(resId);
        weatherimageView.setImageDrawable(weatherIcon);

        tempTextView.setText(item.getCondition().getTemperature()+"°"+channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());

        locationTextView.setText(services.getLocation());


    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT);

    }
}
