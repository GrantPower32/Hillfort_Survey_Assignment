package ie.wit.hillfortssurvey.models

interface HillfortStore {

    fun create(hillfort: HillfortModel)
    fun update(hillfort: HillfortModel)
    suspend fun findAll(): List<HillfortModel>
}