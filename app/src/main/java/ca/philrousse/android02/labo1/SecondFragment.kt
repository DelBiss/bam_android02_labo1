package ca.philrousse.android02.labo1

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.FragmentSecondBinding
import ca.philrousse.android02.labo1.model.ProduitViewModel
import ca.philrousse.android02.labo1.model.ProduitViewModelFactory
import com.google.android.material.textfield.TextInputLayout


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

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

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)



        return binding.root

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_new, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_save ->{
                save()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hookFocusListner(binding.produitNom)
        hookFocusListner(binding.produitCateg)
        hookFocusListner(binding.produitPrix)
        hookFocusListner(binding.produitQte)

        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    private fun hookFocusListner(til: TextInputLayout){
        val editText = til.editText

        editText?.let {

            it.setOnFocusChangeListener { view, b ->
                Log.d("FocusListner", "${view.id.toString()} - ${b.toString()}")
                if(!b){
                    validateIsNotNull(til)
                }
            }
        }
    }
    private fun validateIsNotNull(til: TextInputLayout):Boolean{
        if(til.editText?.text.toString().isEmpty()){
            til.error = getString(R.string.error_field_required_empty)
            return false
        }
        til.error = null
        return true
    }

    private fun validate():Boolean{
        var isValid = true
        isValid = validateIsNotNull(binding.produitNom) && isValid
        isValid = validateIsNotNull(binding.produitCateg) && isValid
        isValid = validateIsNotNull(binding.produitQte) && isValid
        isValid = validateIsNotNull(binding.produitPrix) && isValid
        return isValid
    }

    fun save(){
        if(validate()){
            produitsViewModel.insert(
                Produit(
                    binding.produitNom.editText!!.text.toString(),
                    binding.produitCateg.editText!!.text.toString(),
                    binding.produitPrix.editText!!.text.toString().toDouble(),
                    binding.produitQte.editText!!.text.toString().toInt()
                )
            )
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}