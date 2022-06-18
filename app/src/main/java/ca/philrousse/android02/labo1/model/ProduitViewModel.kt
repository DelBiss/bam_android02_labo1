package ca.philrousse.android02.labo1.model

import androidx.lifecycle.*
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.data.ProduitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProduitViewModel(private val repository: ProduitsRepository):ViewModel() {

    var listeProduitsFiltered: LiveData<List<Produit>> =repository.listeProduits.asLiveData()
        //repository.cat("Merci").asLiveData()




    val spinnerCategoriesListe: LiveData<Set<String>> = Transformations.map(repository.listeCategories){
        val liste = it.toSortedSet().toMutableList()
        liste.add(0,"All Product")
        return@map liste.toSet()
    }



    fun insert(produit: Produit) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(produit)
    }

    fun delete(produit: Produit) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(produit)
    }
}

class ProduitViewModelFactory(private val repository: ProduitsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProduitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProduitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}