package ca.philrousse.android02.labo1.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*

@Entity(tableName = "Produits")
class Produit(_id: Int, nom: String, categ: String, prix: Double, qte: Int) {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Réf produit") val _id: Int = _id
        get() = field

    @ColumnInfo(name = "Nom du produit") var nom: String = nom
        get() = field
        set(value) {
            field = value
        }

    @ColumnInfo(name = "Catégorie") var categ: String = categ
        get() = field
        set(value) {
            field = value
        }
    @ColumnInfo(name = "Prix unitaire") var prix: Double = prix
        get() = field
        set(value) {
            field = value
        }
    @ColumnInfo(name = "Unité en stock") var qte: Int = qte
        get() = field
        set(value) {
            field = value
        }

    val total:Double
        get() = prix * qte

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Produit) return false
        if(
            this._id == other._id &&
            this.nom == other.nom &&
            this.prix == other.prix &&
            this.qte == other.qte &&
            this.categ == other.categ
        ) return true
        return false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "Produit(_id=$_id,nom=$nom,categ=$categ,prix=$prix,qte=$qte)"
    }

    companion object {
        val PRODUIT_COMPARATOR = object : DiffUtil.ItemCallback<Produit>() {
            override fun areItemsTheSame(oldItem: Produit, newItem: Produit): Boolean =
                oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: Produit, newItem: Produit): Boolean =
                oldItem == newItem
        }
    }
}

