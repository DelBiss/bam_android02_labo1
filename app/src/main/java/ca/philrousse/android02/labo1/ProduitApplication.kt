package ca.philrousse.android02.labo1

import android.app.Application
import ca.philrousse.android02.labo1.data.ProduitsRepository
import ca.philrousse.android02.labo1.data.ProduitsRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ProduitApplication():Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { ProduitsRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { ProduitsRepository(database.produitDao()) }
}