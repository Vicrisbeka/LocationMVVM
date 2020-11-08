package com.example.locationmvvm

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationVM: ViewModel() {

    var location: MutableLiveData<Location>? = MutableLiveData<Location>()
    var locationRepository: LocationListener? = null

    fun setLocationRepository(context: Context) {
        locationRepository = LocationListener.getInstance(context)
    }

    fun enableLocationServices(){
        locationRepository?.let {
            it.startService()
        }
    }

}