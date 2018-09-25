package bg.tu.thesis.medicalportal.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bg.tu.thesis.medicalportal.R;

/**
 * Created by filip on 26.05.16.
 */
public class SelectAddressActivity extends Activity {
    private Button btnSave;
    private GoogleMap googleMap;
    Marker marker;
    private  String address="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_address);
        btnSave = (Button) findViewById(R.id.btn_save);
        initilizeMap();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        googleMap.setMyLocationEnabled(true);
       // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(icon_new LatLng(googleMap.getMyLocation().getLatitude(),googleMap.getMyLocation().getLongitude()),18));

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            Log.i("SelectAddressActivity","on location 1");
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
        else
        {
            Log.e("SelectAddressActivity","on location 2");
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));

                btnSave.setVisibility(View.VISIBLE);

                // find address form gps location
                Geocoder gCoder = new Geocoder(SelectAddressActivity.this);
                List<Address> addresses = new ArrayList<Address>();
                try {
                    addresses = gCoder
                            .getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude, 1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (addresses != null && addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SelectAddressActivity","latitude: " + marker.getPosition().latitude);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", marker.getPosition().latitude);
                resultIntent.putExtra("longitude", marker.getPosition().longitude);
                resultIntent.putExtra("address", address);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(SelectAddressActivity.this,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }
}
