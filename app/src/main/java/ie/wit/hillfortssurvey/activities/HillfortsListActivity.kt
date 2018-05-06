package ie.wit.hillfortssurvey.activities

/**
 * Created by grantpower on 05/03/2018.
 */
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import ie.wit.hillfortssurvey.R
import ie.wit.hillfortssurvey.main.MainApp
import ie.wit.hillfortssurvey.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult


class HillfortsListActivity : AppCompatActivity(), HillfortListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainApp

        toolbarMain.title = title
        setSupportActionBar(toolbarMain)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortsMainActivity>(200)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortsMainActivity>().putExtra("hillfort_edit", hillfort),201)
    }

    private fun loadHillforts() {
        async(UI) {
            showHillforts(app.hillforts.findAll())
        }
    }

    override fun onHillfortLongClick(hillfort: HillfortModel) {
        app.hillforts.delete(hillfort)
        loadHillforts()
    }

    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter.notifyDataSetChanged()
    }

}