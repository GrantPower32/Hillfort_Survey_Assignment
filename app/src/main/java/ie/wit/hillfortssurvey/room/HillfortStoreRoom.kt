package ie.wit.hillfortssurvey.room


import android.arch.persistence.room.Room
import android.content.Context
import ie.wit.hillfortssurvey.models.HillfortModel
import ie.wit.hillfortssurvey.models.HillfortStore
import org.jetbrains.anko.coroutines.experimental.bg

class HillfortStoreRoom(val context: Context) : HillfortStore {

    var dao: HillfortDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
                .fallbackToDestructiveMigration()
                .build()
        dao = database.HillfortDao()
    }

    override suspend fun findAll(): List<HillfortModel> {
        val deferredHillforts = bg {
            dao.findAll()
        }
        val hillforts = deferredHillforts.await()
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        bg {
            dao.create(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        bg {
            dao.update(hillfort)
        }
    }

    override fun delete(hillfort: HillfortModel) {
        bg {
            dao.deleteHillfort(hillfort)
        }
    }
}