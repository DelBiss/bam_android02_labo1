package ca.philrousse.android02.labo1.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProduitDAO {

    @Query("SELECT * FROM Produits")
    fun obtenirListeProduits():Flow<List<Produit>>

    @Query("SELECT * FROM Produits WHERE cat = :categ")
    fun obtenirListeProduitsParCategorie(categ:String):Flow<List<Produit>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(produit: Produit)

    @Insert
    fun insertAll(vararg users: Produit)

    @Delete
    fun delete(produit: Produit)

    @Query("DELETE FROM Produits")
    fun deleteAll()
}