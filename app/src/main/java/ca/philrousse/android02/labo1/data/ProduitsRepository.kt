package ca.philrousse.android02.labo1.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import java.util.*

class ProduitsRepository(private val produitDao: ProduitDAO) {

    val listeProduits: Flow<List<Produit>> = produitDao.obtenirListeProduits()
    fun cat(cate:String) = produitDao.obtenirListeProduitsParCategorie(cate)

    val totalInventaire:LiveData<Double> = Transformations.map(listeProduits.asLiveData()){ liste->
        liste.sumOf{
            it.total
        }
    }

    val listeCategories: LiveData<Set<String>> = Transformations.map(listeProduits.asLiveData()){ liste ->
        liste.map {
            it.categ
        }.toSet()
    }



    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(produit: Produit) {
        produitDao.insert(produit)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(produit: Produit) {
        produitDao.delete(produit)
    }


}