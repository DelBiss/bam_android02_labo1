package ca.philrousse.android02.labo1.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.*
import java.util.*

class ProduitsRepository(private val produitDao: ProduitDAO) {

    val listeProduits: Flow<List<Produit>> = produitDao.obtenirListeProduits()

    fun listeProduitsParCategorie(cate:String) = produitDao.obtenirListeProduitsParCategorie(cate)

    fun totalInventaire(notUser:Any?):Flow<TotalInventaire> = produitDao.obtenirValeurInventaire()

    fun totalInventaire(cate:String):Flow<TotalInventaire> = produitDao.obtenirValeurInventaireParCategorie(cate)

    val listeCategories = produitDao.obtenirListeCategory().map {
        it.map { categorie -> categorie.categ }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(produit: Produit):Long {
        return produitDao.insert(produit)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(produit: Produit) {
        produitDao.delete(produit)
    }
}