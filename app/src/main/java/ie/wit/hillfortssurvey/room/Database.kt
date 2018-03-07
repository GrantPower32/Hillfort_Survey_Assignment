package ie.wit.hillfortssurvey.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ie.wit.hillfortssurvey.models.HillfortModel


@Database(entities = arrayOf(HillfortModel::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun HillfortDao(): HillfortDao
}