package com.example.mapsandspeechrecognition;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.example.mapsandspeechrecognition.model.CountryDataSource;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  String receievedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent mainActivityIntent=this.getIntent();
        receievedCountry=mainActivityIntent.getStringExtra(CountryDataSource.Country_Key);
        Log.i("outqqqqqqq",receievedCountry);
        if(receievedCountry==null){

            receievedCountry=CountryDataSource.Default_Country;
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        double countryLatitude=CountryDataSource.Default_Country_Latitude;
        double countryLongitue=CountryDataSource.Default_Country_Longitude;
        CountryDataSource countryDataSource=MainActivity.countryDataSource;
        String countryMessage=countryDataSource.getInfoCountry(receievedCountry);
        Geocoder geocoder=new Geocoder(MapsActivity.this);




        try{
            String countryAddress=receievedCountry;
            //here 10 is maximum no of country recevied and if there after will get the best accurate result
            List<Address>countryAddresses=geocoder.getFromLocationName(countryAddress,10);
            Log.i("outqqqqqqq",countryAddress);
            if(countryAddress!=null){

                countryLatitude=countryAddresses.get(0).getLatitude();
                countryLongitue=countryAddresses.get(0).getLongitude();


            }
            else{
                receievedCountry=CountryDataSource.Default_Country;

            }
        }
        catch (IOException ioe){

            receievedCountry=CountryDataSource.Default_Country;

        }

        LatLng myCountryLocation=new LatLng(countryLatitude,countryLongitue);
        CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(myCountryLocation,5.0f);
        mMap.moveCamera(cameraUpdate);
        Log.i("outqqqqqqq","country");

            MarkerOptions markerOptions=new MarkerOptions();
               markerOptions.position(myCountryLocation);
                markerOptions.title("hello");
                markerOptions.snippet(CountryDataSource.Default_Message);
                mMap.addMarker(markerOptions);
                CircleOptions circleOptions=new CircleOptions();
                circleOptions.center(myCountryLocation);
                circleOptions.radius(150);
                circleOptions.strokeWidth(10.0f);
                circleOptions.strokeColor(Color.BLUE);
                mMap.addCircle(circleOptions);







    }
}
