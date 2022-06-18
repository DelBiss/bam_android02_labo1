package ca.philrousse.android02.labo1.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import java.util.*

class ProduitsRepository(private val produitDao: ProduitDAO) {

    val listeProduits: Flow<List<Produit>> = produitDao.obtenirListeProduits()
    fun cat(cate:String) = produitDao.obtenirListeProduitsParCategorie(cate)

    fun totalInventaire(notUser:Any?):Flow<TotalInventaire> = produitDao.obtenirValeurInventaire()

    fun totalInventaire(cate:String):Flow<TotalInventaire> = produitDao.obtenirValeurInventaireParCategorie(cate)

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