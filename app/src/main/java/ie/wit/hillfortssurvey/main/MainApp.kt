package ie.wit.hillfortssurvey.main



import android.app.Application
import ie.wit.hillfortssurvey.models.HillfortStore
import ie.wit.hillfortssurvey.room.HillfortStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    override fun onCreate() {
        super.onCreate()

        hillforts = HillfortStoreRoom (applicationContext)
        info("Hillfort started")
    }
}