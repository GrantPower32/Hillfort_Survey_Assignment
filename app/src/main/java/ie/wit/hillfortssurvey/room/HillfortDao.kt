package ie.wit.hillfortssurvey.room

import android.arch.persistence.room.*
import ie.wit.hillfortssurvey.models.HillfortModel


@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)
}
