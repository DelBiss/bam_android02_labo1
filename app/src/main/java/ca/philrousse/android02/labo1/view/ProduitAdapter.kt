package ca.philrousse.android02.labo1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.philrousse.android02.labo1.R
import ca.philrousse.android02.labo1.data.Produit
import ca.philrousse.android02.labo1.databinding.ProduitListviewItemBinding


class ProduitAdapter :
    ListAdapter<Produit, ProduitAdapter.ProduitViewHolder>(Produit.PRODUIT_COMPARATOR) {

    class ProduitViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var binding: ProduitListviewItemBinding
        private var produitCurrent: Produit? = null

        init {
            binding = ProduitListviewItemBinding.bind(itemView)

        }

        fun bind(produit: Produit) {
            produitCurrent = produit
            binding.produit = produit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.produit_listview_item, parent, false)
        return ProduitViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
        val produit = getItem(position)
        holder.bind(produit)
    }
}