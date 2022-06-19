package ca.philrousse.android02.labo1.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import ca.philrousse.android02.labo1.model.ProduitViewModel

class SimpleSpinnerArrayAdapter(context: Context) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item) {

    init {
       this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
}

class CategorySpinnerItemSelectedListener(private val viewModel: ProduitViewModel): AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val filterString = parent.getItemAtPosition(position) as String
        Log.d("Filter","Pos: $position, String: $filterString")
        viewModel.categoryFilter.value = if (position > 0) filterString else null
    }
}