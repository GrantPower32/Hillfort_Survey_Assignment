package ie.wit.hillfortssurvey.models

interface HillfortStore {

    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
    suspend fun findAll(): List<HillfortModel>
}