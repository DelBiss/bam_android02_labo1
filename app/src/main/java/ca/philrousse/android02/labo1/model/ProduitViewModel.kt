package ca.philrousse.android02.labo1.model

import android.util.Log
import androidx.lifecycle.*
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.data.ProduitsRepository
import ca.philrousse.android02.labo1.data.TotalInventaire

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProduitViewModel(private val repository: ProduitsRepository):ViewModel() {

    var categoryFilter = MutableStateFlow<String?>(null)

    var listeProduitsFiltered:StateFlow<List<Produit>> = categoryFilter.flatMapLatest { categ ->
        Log.d("Update",categ.toString())
        if(categ.isNullOrEmpty()){
            repository.listeProduits
        } else {
            repository.listeProduitsParCategorie(categ)
        }
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    var totalInventaire = categoryFilter.flatMapLatest {
            repository.totalInventaire(null)}.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TotalInventaire(null)
    )

    var totalInventaireFiltered:StateFlow<TotalInventaire> = categoryFilter.flatMapLatest { categ ->
        if(categ.isNullOrEmpty()){
            flow { emit(TotalInventaire(null,true)) }
            //repository.totalInventaire(null)
        } else {
            repository.totalInventaire(categ)
        }
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TotalInventaire(null, true)
    )

    val listeCategorie = repository.listeCategories.map {
        it.toSortedSet()
    }

    fun insert(produit: Produit, callback: ((Long)->Unit)? = null) = CoroutineScope(Dispatchers.IO).launch {
        val insertedId = repository.insert(produit)
        callback?.let {
            it(insertedId)
        }
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