package com.google.android.gms.samples.vision.barcodereader;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.server_client_communication.Locazione;
import com.example.server_client_communication.TerzoOcchio_Server;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.ui.camera.ActivityPagina;
import com.google.android.gms.vision.barcode.Barcode;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    public MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiM29jY2hpIiwiYSI6ImNqdHk1emd6YTJoOXkzenBpbjE1dXdlcTcifQ.33DorUfbdBAxaoIy2rpdlg");

        setContentView(R.layout.activity_main1);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                TerzoOcchio_Server server = new TerzoOcchio_Server();
                ArrayList<Locazione> loc = null;


                try {
                    loc = server.location();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                List<Feature> symbolLayer = new ArrayList<>();
                for(Locazione locazione : loc){
                    symbolLayer.add(Feature.fromGeometry(Point.fromLngLat(locazione.getLongitudine(), locazione.getLatitudine())));
                }


                mapboxMap.setStyle(new Style.Builder().fromUrl("mapbox://styles/3occhi/cjui32etx00un1fmc97ny7ah1")
                        .withImage("marker-icon-id", BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))
                        .withSource(new GeoJsonSource("source-id", FeatureCollection.fromFeatures(symbolLayer)))
                        .withLayer(new SymbolLayer("layer-id", "source-id")
                            .withProperties(PropertyFactory.iconImage("marker-icon-id")))
                        , new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {}
                });
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.qr_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //codice che fa aprire la scansione del qr

                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);

                //fine codice
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.register) {
            //Codice che gestisce la registrazione
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(getApplicationContext(), barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    Intent i = new Intent(this, ActivityPagina.class);
                    i.putExtra("pagina", barcode.displayValue);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    public void generateMapMarker(Style style) {
        TerzoOcchio_Server server = new TerzoOcchio_Server();
        ArrayList<Locazione> loc = null;

        try {
            loc = server.location();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /*ciclo che scorre un arraylist contenente latitudini, longitudini di tutti i punti
         * da inserire sulla mappa che assegna ad un nuovo marker che apparirà sulla mappa*/
        for (Locazione location : loc) {

            // Add the marker image to map
            style.addImage("marker-icon-id",
                    BitmapFactory.decodeResource(
                            MainActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));

            GeoJsonSource geoJsonSource = new GeoJsonSource("source-id-" + location.getId(), Feature.fromGeometry(
                    Point.fromLngLat(location.getLongitudine(), location.getLatitudine())));
            style.addSource(geoJsonSource);

            SymbolLayer symbolLayer = new SymbolLayer("layer-id" + location.getId(), "source-id");
            symbolLayer.withProperties(
                    PropertyFactory.iconImage("marker-icon-id")
            );
            style.addLayer(symbolLayer);
        }
    }
}