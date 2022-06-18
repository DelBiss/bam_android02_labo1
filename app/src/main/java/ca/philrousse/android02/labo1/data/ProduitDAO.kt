package ca.philrousse.android02.labo1.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProduitDAO {

    @Query("SELECT * FROM Produits")
    fun obtenirListeProduits():Flow<List<Produit>>

    @Query("SELECT * FROM Produits WHERE categ = :categ")
    fun obtenirListeProduitsParCategorie(categ:String):Flow<List<Produit>>

    @Query("SELECT SUM(qte * prix) AS total, 0 as isCategory FROM Produits")
    fun obtenirValeurInventaire():Flow<TotalInventaire>

    @Query("SELECT SUM(qte * prix) AS total, 1 as isCategory FROM Produits WHERE categ = :categ")
    fun obtenirValeurInventaireParCategorie(categ:String):Flow<TotalInventaire>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(produit: Produit)

    @Insert
    fun insertAll(vararg users: Produit)

    @Delete
    fun delete(produit: Produit)

    @Query("DELETE FROM Produits")
    fun deleteAll()
}