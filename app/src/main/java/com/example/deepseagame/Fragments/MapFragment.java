package com.example.deepseagame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.deepseagame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment{
    private GoogleMap gMap;
    public MapFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                gMap = googleMap;
                LatLng location = new LatLng(55.6761, 12.5683);
                googleMap.addMarker(new MarkerOptions().position(location).title("New York"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,11));
            }
        });
        return view;
    }

    public void moveToLocation(double lat, double lon) {
        reDirectCamera(lat, lon);
    }

    private void reDirectCamera(double lat, double lon) {
        if (gMap != null) {
            LatLng location = new LatLng(lat, lon);
            gMap.clear(); // Clear existing markers
            gMap.addMarker(new MarkerOptions().position(location).title("New Location"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lon))
                    .zoom(11)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

}