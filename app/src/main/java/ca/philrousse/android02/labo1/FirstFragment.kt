package ca.philrousse.android02.labo1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.FragmentFirstBinding
import ca.philrousse.android02.labo1.model.ProduitViewModel
import ca.philrousse.android02.labo1.model.ProduitViewModelFactory
import ca.philrousse.android02.labo1.view.ProduitAdapter


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val produitsViewModel: ProduitViewModel by activityViewModels {
        ProduitViewModelFactory((activity!!.application as ProduitApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

       hookRecycleView()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hookRecycleView(){

        val produitAdapter = ProduitAdapter()

        val recyclerView: RecyclerView = binding.recyclerView

        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayout.VERTICAL
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)
        recyclerView.adapter = produitAdapter

        produitsViewModel.listeProduits.observe(viewLifecycleOwner) {
            it?.let {
                if(it.isNotEmpty()) {
                    produitAdapter.submitList(it as MutableList<Produit>)
                } else {
                    produitAdapter.submitList(null)
                }
            }
        }
    }
}