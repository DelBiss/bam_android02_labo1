package ca.philrousse.android02.labo1

import android.app.Application
import android.content.Context
import android.util.Log
import ca.philrousse.android02.labo1.data.ProduitsRepository
import ca.philrousse.android02.labo1.data.ProduitsRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ProduitApplication:Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { ProduitsRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { ProduitsRepository(database.produitDao()) }

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences("ca.philrousse.android02.labo1",Context.MODE_PRIVATE)
        Log.d("Life","onCreate")
        sharedPref?.let {
            with (it.edit()) {
                putString("category_filter", null)
                apply()
            }
        }
        Log.d("Spinner","Resetting current filter: ${sharedPref?.getString("category_filter",null)}")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("Life","OnDestroy")
    }


}