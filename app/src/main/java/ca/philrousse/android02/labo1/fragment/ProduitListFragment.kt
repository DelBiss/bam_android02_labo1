package ca.philrousse.android02.labo1.fragment


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
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
import ca.philrousse.android02.labo1.adapter.setCurrencyAmount
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.ProduitFragmentListBinding
import ca.philrousse.android02.labo1.model.ProduitViewModel
import ca.philrousse.android02.labo1.model.ProduitViewModelFactory
import ca.philrousse.android02.labo1.adapter.ProduitAdapter
import kotlinx.coroutines.launch
import kotlin.math.min


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProduitListFragment : Fragment() {

    private var _binding: ProduitFragmentListBinding? = null
    private var total: Double = 0.0
    private var categ: Double = 0.0

    private val binding get() = _binding!!

    private val produitsViewModel: ProduitViewModel by activityViewModels {
        ProduitViewModelFactory((activity!!.application as ProduitApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ProduitFragmentListBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        hookRecycleView()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_produit_list, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val item: MenuItem = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner
        val add =
            ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                produitsViewModel.categoryFilter.value = if (position == 0) {
                    binding.viewCateg.visibility = View.GONE
                    null
                } else {
                    binding.viewCateg.visibility = View.VISIBLE
                    parent.getItemAtPosition(position) as String
                }
                Log.d("Spinner", "$position -> ${parent.getItemAtPosition(position)}")
            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        produitsViewModel.spinnerCategoriesListe.observe(viewLifecycleOwner) {
            add.clear()
            add.addAll(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hookRecycleView() {
        binding.total = total
        binding.categ = categ

        val produitAdapter = ProduitAdapter()

        val recyclerView: RecyclerView = binding.recyclerView

        val mDividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayout.VERTICAL
        )
        recyclerView.addItemDecoration(mDividerItemDecoration)
        recyclerView.adapter = produitAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.totalInventaire.collect {
                    Log.d("TotalInventaire", it.toString())
                    setCurrencyAmount(binding.txtTotalValue, it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.listeProduitsFiltered.collect {
                    Log.d("viewModel", it.toString())
                    it.let {
                        if (it.isNotEmpty()) {
                            produitAdapter.submitList(it as MutableList<Produit>)
                        } else {
                            produitAdapter.submitList(null)
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                produitsViewModel.totalInventaireFiltered.collect {
                    Log.d("totalInventaireFiltered", it.total.toString())
                    setCurrencyAmount(binding.txtCategValue, it.total)
                }
            }
        }


        val myCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT
        ) {

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

                Log.d(
                    "Swipe",
                    "It Swipe -> ${produitAdapter.currentList[viewHolder.adapterPosition]}"
                )
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
                c.clipRect(
                    0f, viewHolder.itemView.top.toFloat(),
                    dX, viewHolder.itemView.bottom.toFloat()
                )

                val myHSLColor = FloatArray(3)
                Color.colorToHSV(Color.RED, myHSLColor)
                val cx = 0.6F
                myHSLColor[1] = (min(dX / (viewHolder.itemView.width / 2), 1F) * cx) + (1 - cx)

                myHSLColor[2] = (min(dX / (viewHolder.itemView.width / 2), 1F) * cx) + (1 - cx)
                val myColor = Color.HSVToColor(myHSLColor)

                Log.d(
                    "Color",
                    "R:${Color.red(myColor)}, G:${Color.green(myColor)}, B:${Color.blue(myColor)}"
                )
                c.drawColor(Color.HSVToColor(myHSLColor))


                val textMargin = resources.getDimension(R.dimen.default_margin).toInt() * 2

                val iconSize = viewHolder.itemView.height - (textMargin * 2)

                trashBinIcon.bounds = Rect(
                    textMargin,
                    viewHolder.itemView.top + textMargin,
                    textMargin + iconSize,//trashBinIcon.intrinsicWidth,
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