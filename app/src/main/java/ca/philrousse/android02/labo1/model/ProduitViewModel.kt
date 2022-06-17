package ca.philrousse.android02.labo1.model

import androidx.lifecycle.*
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.data.ProduitsRepository
import kotlinx.coroutines.launch

class ProduitViewModel(private val repository: ProduitsRepository):ViewModel() {

    val listeProduits: LiveData<List<Produit>> = repository.listeProduits
    val listeCategories: LiveData<Set<String>> = repository.listeCategories

    fun insert(produit: Produit) = viewModelScope.launch {
        repository.insert(produit)
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