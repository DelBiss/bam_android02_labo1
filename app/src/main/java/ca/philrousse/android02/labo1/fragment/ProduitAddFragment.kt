package ca.philrousse.android02.labo1.fragment

import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ca.philrousse.android02.labo1.ProduitApplication
import ca.philrousse.android02.labo1.R
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.ProduitFragmentAddBinding
import ca.philrousse.android02.labo1.model.ProduitViewModel
import ca.philrousse.android02.labo1.model.ProduitViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class ProduitAddFragment : Fragment() {

    private var _binding: ProduitFragmentAddBinding? = null
    private val binding get() = _binding!!

    private val produitsViewModel: ProduitViewModel by activityViewModels {
        ProduitViewModelFactory((activity!!.application as ProduitApplication).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = ProduitFragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_produit_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        hookFocusListener(binding.produitNom)
        hookFocusListener(binding.produitCateg)
        hookFocusListener(binding.produitPrix)
        hookFocusListener(binding.produitQte)
    }

    private fun hookFocusListener(til: TextInputLayout){
        til.editText?.let {
            it.setOnFocusChangeListener { _, b ->
                if(!b){
                    validateIsNotNull(til)
                }
            }
        }
    }

    private fun validateIsNotNull(til: TextInputLayout):Boolean{
        if(getInputString(til).isEmpty()){
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

    private fun getInputString(til:TextInputLayout):String{
            return til.editText?.text.toString()
    }

    private fun getProduct():Produit?{
        if(validate()) {
            return Produit(
                getInputString(binding.produitNom),
                getInputString(binding.produitCateg),
                getInputString(binding.produitPrix).toDouble(),
                getInputString(binding.produitQte).toInt()
            )
        }
        return null
    }

    private fun save(){
        if(validate()){
            getProduct()?.let { newProduct ->
                produitsViewModel.insert(newProduct){ savedId->
                        saveCallback(savedId)
                }
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }
    }

    private fun saveCallback(saveId:Long)  {
        view?.let {
            val snackbarString = it.context.resources.getString(R.string.toas_new_product,saveId)
            Snackbar.make(it, snackbarString,Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}