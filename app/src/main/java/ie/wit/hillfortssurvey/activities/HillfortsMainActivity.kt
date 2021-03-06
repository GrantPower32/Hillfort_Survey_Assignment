package ie.wit.hillfortssurvey.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.design.R.id.message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import checkLocationPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.hillfortssurvey.R
import kotlinx.android.synthetic.main.activity_hillfort_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import ie.wit.hillfortssurvey.main.MainApp
import ie.wit.hillfortssurvey.models.HillfortModel
import ie.wit.hillfortssurvey.models.Location
import ie.wit.hillfortssurveys.helpers.readImage
import ie.wit.hillfortssurveys.helpers.readImageFromPath
import ie.wit.hillfortssurveys.helpers.showImagePicker
import isPermissionGranted
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.intentFor
import java.text.SimpleDateFormat
import java.util.*


class HillfortsMainActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    var edit = false
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    lateinit var map: GoogleMap
    val defaultLocation = Location(52.245696, -7.139102, 15f)
    private lateinit var locationService: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_main)
        mapView.onCreate(savedInstanceState);
        app = application as MainApp
        locationService = LocationServices.getFusedLocationProviderClient(this)

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        chooseImage.setOnClickListener {
            info("Select image")
        }
        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        mapView.getMapAsync {
            map = it
            configureMap()
        }

        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        textview_date!!.text = "--/--/----"

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@HillfortsMainActivity,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })


        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
        } else {
            hillfort.lat = defaultLocation.lat
            hillfort.lng = defaultLocation.lng
            hillfort.zoom = defaultLocation.zoom

        }
        hillfortLocation.setOnClickListener {
            if (hillfort.zoom != 0f) {
                defaultLocation.lat = hillfort.lat
                defaultLocation.lng = hillfort.lng
                defaultLocation.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", defaultLocation), LOCATION_REQUEST)
        }
    }
    fun configureMap() {
        map.uiSettings.setZoomControlsEnabled(true)
        val loc = LatLng(hillfort.lat, hillfort.lng)
        val options = MarkerOptions().title(hillfort.title).position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, hillfort.zoom))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun save() {
        hillfort.title = hillfortTitle.text.toString()
        hillfort.description = hillfortDescription.text.toString()

        if (edit) {
            app.hillforts.update(hillfort.copy())
            setResult(201)
            finish()
        } else {
            if (hillfort.title.isNotEmpty()) {
                app.hillforts.create(hillfort.copy())
                setResult(200)
                finish()
            } else {
                toast(R.string.enter_hillfort_title)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                save()
            }
            R.id.item_cancel -> {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkLocationPermissions(this)) {
            btnHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            btnHere.isEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    fun setCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            defaultLocation.lat = it.latitude
            defaultLocation.lng = it.longitude
            hillfort.lat = it.latitude
            hillfort.lng = it.longitude
            configureMap()

            btnHere.setOnClickListener {
                setCurrentLocation()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}