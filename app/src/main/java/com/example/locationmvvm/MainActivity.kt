package com.example.locationmvvm

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: LocationVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.setLocationRepository(this)
        setUpBtnAndObserver()
    }

    private fun setUpBtnAndObserver() {
        //Set on click listener for Find Location button
        find_location_btn.setOnClickListener(View.OnClickListener {
            getDeviceLocation()
        })
        val locationObserver = Observer<Location> { location ->
            // Update the UI
            Log.i("LiveData location", "" + location.latitude + " / " + location.longitude)
            location?.let {
                location_text.text = "" + it.latitude + "\n" + it.longitude
            }
        }
        viewModel.location?.observe(this, locationObserver)
    }

    private fun getDeviceLocation() {
        if (isPermissionGranted()) {
            progress_bar.visibility = View.VISIBLE
            viewModel.enableLocationServices()

            viewModel.locationRepository?.let {
                if (!it.hasObservers()) {
                    it.observe(this, Observer<Location?> { location ->
                        location?.let {
                            viewModel.location?.value = it
                            progress_bar.visibility = View.INVISIBLE
                        }
                    })
                }
            }
        } else requestPermission()
    }

    private fun isPermissionGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        if(!isPermissionGranted()) {
            ActivityCompat.requestPermissions(this,arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), 1001)
        }
    }
}