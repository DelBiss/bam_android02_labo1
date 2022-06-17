package ca.philrousse.android02.labo1.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take

class ProduitsRepository(private val produitDao: ProduitDAO) {
    val listeProduits: LiveData<List<Produit>> = produitDao.obtenirListeProduits().asLiveData()
    val listeCategories: LiveData<Set<String>> = Transformations.map(listeProduits){ liste ->
        liste.map {
            it.categ
        }.toSet()
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(produit: Produit) {
        produitDao.insert(produit)
    }
}