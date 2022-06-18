package ca.philrousse.android02.labo1


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
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


        setHasOptionsMenu(true)
       hookRecycleView()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val item: MenuItem = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner
        val add = ArrayAdapter<String>(activity!!,android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        produitsViewModel.spinnerCategoriesListe.observe(viewLifecycleOwner){
            add.clear()
            add.addAll(it)
        }
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

        produitsViewModel.listeProduitsFiltered.observe(viewLifecycleOwner) {
            Log.d("viewModel",it.toString())
            it?.let {
                if(it.isNotEmpty()) {
                    produitAdapter.submitList(it as MutableList<Produit>)
                } else {
                    produitAdapter.submitList(null)
                }
            }
        }


        val myCallback = object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {

            val trashBinIcon = resources.getDrawable(
                R.drawable.ic_baseline_delete_forever_24,
                null
            )
            // More code here
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                Log.d("Swipe","It Swipe -> ${produitAdapter.currentList[viewHolder.adapterPosition]}")
                produitsViewModel.delete(produitAdapter.currentList[viewHolder.adapterPosition])
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                c.clipRect(0f, viewHolder.itemView.top.toFloat(),
                    dX, viewHolder.itemView.bottom.toFloat())

                if(dX < viewHolder.itemView.width / 2)
                    c.drawColor(Color.GRAY)
                else
                    c.drawColor(Color.RED)

                val textMargin = resources.getDimension(R.dimen.default_margin).toInt() * 2

                val iconSize = viewHolder.itemView.height - (textMargin*2)

                trashBinIcon.bounds = Rect(
                    textMargin,
                    viewHolder.itemView.top + textMargin,
                    textMargin + iconSize ,//trashBinIcon.intrinsicWidth,
                    viewHolder.itemView.top + textMargin + iconSize
                )
                trashBinIcon.draw(c)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        val myHelper = ItemTouchHelper(myCallback)
        myHelper.attachToRecyclerView(recyclerView)
    }
}