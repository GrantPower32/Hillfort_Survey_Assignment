package ie.wit.hillfortssurvey.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import ie.wit.hillfortssurvey.R
import kotlinx.android.synthetic.main.activity_hillfort_splash.*
import org.jetbrains.anko.startActivity

class HillfortSplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_splash)
        openButton.setOnClickListener{
            startActivity<HillfortsListActivity>()
        }
    }
}
