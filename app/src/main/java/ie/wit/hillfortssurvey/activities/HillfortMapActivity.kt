package ie.wit.hillfortssurvey.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hillfortssurvey.R
import ie.wit.hillfortssurvey.main.MainApp
import kotlinx.android.synthetic.main.activity_hillfort_main.*
import kotlinx.android.synthetic.main.activity_hillfort_map.*
import kotlinx.android.synthetic.main.content_hillfort_map.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class HillfortMapActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_map)
        app = application as MainApp

        mView.onCreate(savedInstanceState);
        mView.getMapAsync {
            map = it
            configureMap()
        }
    }
        fun configureMap() {
            map.uiSettings.setZoomControlsEnabled(true)
            map.setOnMarkerClickListener(this)
            async(UI) {
                app.hillforts.findAll().forEach {
                    val loc = LatLng(it.lat, it.lng)
                    val options = MarkerOptions().title(it.title).position(loc)
                    map.addMarker(options).tag = it.id
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
                }
            }
        }

    override fun onMarkerClick(marker: Marker): Boolean {
        currentTitle.text = marker.title
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        mView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mView.onSaveInstanceState(outState)
    }
}