package com.example.medqx.Locator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.medqx.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private static final int Request_User_Location_Code=99;
    private Marker currentUserLocationMarker;
    double latitude,longitude;
    private int proximityRadius=1000000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
       mMap.setMyLocationEnabled(true);
    }}

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

            }
            return false;

        }
        else
        {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient ==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }

                else
                {
                    Toast.makeText(this,"Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }


    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        lastLocation=location;
        if( currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng=new LatLng(location.getAltitude(),location.getAltitude());

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onClick(View view) {

        String hospital="hospital" , school= "temple",pharmacy="pharmacy";
        Object transferData[]= new Object[2];
        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();


        switch (view.getId())
        {
            case R.id.search:
                EditText addressField= findViewById(R.id.location_search);
                String address = addressField.getText().toString();

                List<Address> addressList= null;
                MarkerOptions userMarkerOptions = new MarkerOptions();
                if(!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder=new Geocoder(this);

                    try {
                        addressList=geocoder.getFromLocationName(address,6);
                        if(addressList!= null)
                        {
                            for (int i =0;i<addressList.size();i++)
                            {
                                Address useraddress= addressList.get(i);
                                LatLng latLng= new LatLng(useraddress.getLatitude(),useraddress.getLongitude());
                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                mMap.addMarker(userMarkerOptions);

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }
                        }
                        else
                            Toast.makeText(this,"Location Not Found", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                    Toast.makeText(this,"Please enter a location", Toast.LENGTH_SHORT).show();
                break;

            case R.id.hospital:
                mMap.clear();
                String url= geturl(latitude,longitude,hospital);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this,"Searching for Hospitals...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Showing Nearby Hospitals...", Toast.LENGTH_SHORT).show();

                break;
            case R.id.school:
                mMap.clear();
                 url= geturl(latitude,longitude,school);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this,"Searching for temples...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Showing Nearby temples...", Toast.LENGTH_SHORT).show();

                break;
            case R.id.pharmacy:
                mMap.clear();
                 url= geturl(latitude,longitude,pharmacy);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(this,"Searching for pharmacy...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"Showing Nearby pharmacy...", Toast.LENGTH_SHORT).show();

                break;




        }

    }

    private String geturl(double latitude, double longitude, String nearByPlace) {

        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+latitude+","+longitude);
        googleURL.append("&radius="+proximityRadius);
        googleURL.append("&type="+nearByPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key="+"AIzaSyDxgysIJqhKhLCbMQiuwOoLp_37vYCgrw8");

        Log.e("mapactivity","url--->"+googleURL.toString());

        return googleURL.toString();

    }
}
