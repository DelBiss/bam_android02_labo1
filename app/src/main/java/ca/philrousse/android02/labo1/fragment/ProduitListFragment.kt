package ca.philrousse.android02.labo1.fragment


import android.content.Context
import android.content.SharedPreferences

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.labo1.ProduitApplication
import ca.philrousse.android02.labo1.R
import ca.philrousse.android02.labo1.adapter.CategorySpinnerItemSelectedListener
import ca.philrousse.android02.labo1.adapter.ListSwipeDeleteGesture
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.ProduitFragmentListBinding
import ca.philrousse.android02.labo1.model.ProduitViewModel
import ca.philrousse.android02.labo1.model.ProduitViewModelFactory
import ca.philrousse.android02.labo1.adapter.ProduitAdapter
import ca.philrousse.android02.labo1.adapter.SimpleSpinnerArrayAdapter
import ca.philrousse.android02.labo1.data.TotalInventaire
import kotlinx.coroutines.launch
import kotlin.math.max



class ProduitListFragment : Fragment() {

    private var _binding: ProduitFragmentListBinding? = null
    private val binding get() = _binding!!

    private val produitsViewModel: ProduitViewModel by activityViewModels {
        ProduitViewModelFactory((activity!!.application as ProduitApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProduitFragmentListBinding.inflate(inflater, container, false)

        restoreSetting()
        hookUi()
        hookData()
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        saveSetting()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_produit_list, menu)
        super.onCreateOptionsMenu(menu, inflater)

        configSpinner(menu)
    }

    //////////////////////////
    // Setting

    private fun getSharedPreferences(): SharedPreferences? {
        return activity?.getSharedPreferences("ca.philrousse.android02.labo1", Context.MODE_PRIVATE)
    }

    private fun restoreSetting(){
        produitsViewModel.categoryFilter.value = getSharedPreferences()?.getString("category_filter",null)
    }

    private fun saveSetting(){
        getSharedPreferences()?.let {
            with (it.edit()) {
                putString("category_filter", produitsViewModel.categoryFilter.value)
                apply()
            }
        }
    }


    private fun hookUi() {
        setHasOptionsMenu(true)
        hookRecycleView()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    ///////////////////////////////////
    // Spinner

    private fun configSpinner(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        spinner.adapter = SimpleSpinnerArrayAdapter(activity!!)
        spinner.onItemSelectedListener = CategorySpinnerItemSelectedListener(produitsViewModel)
        hookDataCategorySpinner(spinner)

    }

    private fun addAllCategorie(list:Set<String>):Set<String>{
        val liste = list.toMutableList()
        liste.add(0,getString(R.string.spinner_all))
        return liste.toSet()
    }
    private fun hookDataCategorySpinner(spinner: Spinner){
        val spinnerArrayAdapter = spinner.adapter as ArrayAdapter<String>
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.listeCategorie.collect{
                    val categoryChoice = addAllCategorie(it)

                    spinnerArrayAdapter.clear()
                    spinnerArrayAdapter.addAll(categoryChoice)
                    spinner.setSelection(getCurrentFilterPosition(categoryChoice))
                }
            }
        }
    }

    private fun getCurrentFilterPosition(categorySet:Set<String>):Int{
        val currentFilter = produitsViewModel.categoryFilter.value
        val filterPos = categorySet.indexOf(currentFilter)
        return max(filterPos,0)
    }

    private fun hookDataRecycleView(adapter:ProduitAdapter){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.listeProduitsFiltered.collect {
                    if (it.isNotEmpty()) {
                        adapter.submitList(it as MutableList<Produit>)
                    } else {
                        adapter.submitList(null)
                    }
                }
            }
        }
    }

    private fun hookData(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.totalInventaire.collect {
                    binding.total = it
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.totalInventaireFiltered.collect {
                    Log.d("totalInventaireFiltered", it.toString())
                    binding.categ = it
                }
            }
        }
    }
    private fun hookRecycleView() {
        binding.total = TotalInventaire(null)
        binding.categ = TotalInventaire(null,true)

        val recyclerView: RecyclerView = binding.recyclerView
        val produitAdapter = ProduitAdapter()

        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayout.VERTICAL
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)
        recyclerView.adapter = produitAdapter

        hookDataRecycleView(produitAdapter)

        val listeSwipeTouchHelper = ListSwipeDeleteGesture(
            icon = resources.getDrawable(R.drawable.ic_baseline_delete_forever_24, null),
            color = Color.RED,
        ) {
            produitsViewModel.delete(produitAdapter.currentList[it])
        }

        ItemTouchHelper(listeSwipeTouchHelper).attachToRecyclerView(recyclerView)
    }
}