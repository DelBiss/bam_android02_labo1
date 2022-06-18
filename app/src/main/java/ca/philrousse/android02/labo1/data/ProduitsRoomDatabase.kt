package ca.philrousse.android02.labo1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Produit::class], version = 1, exportSchema = false)
abstract class ProduitsRoomDatabase:RoomDatabase() {
    abstract fun produitDao(): ProduitDAO

    companion object {
        @Volatile
        private var INSTANCE: ProduitsRoomDatabase? = null

        fun getDatabase(
            context: Context ,
            scope: CoroutineScope
        ): ProduitsRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProduitsRoomDatabase::class.java,
                    "word_database"
                ).addCallback(ProduitsDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class ProduitsDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.produitDao())
                    }
                }
            }

            fun populateDatabase(produitDao: ProduitDAO) {
                produitDao.deleteAll()

                produitDao.insert(Produit(1,"Chai","Boissons", 90.0, 39))
                produitDao.insert(Produit(2,"Chang","Merci", 95.0, 17))
                produitDao.insert(Produit(3,"Aniseed Syrup","Condiments", 50.0, 13))
                produitDao.insert(Produit(4,"Chef Anton's Cajun Seasoning","Condiments", 110.0, 53))
                produitDao.insert(Produit(5,"Chef Anton's Gumbo Mix","Condiments", 106.75, 39))
                produitDao.insert(Produit(6,"Grandma's Boysenberry Spread","Condiments", 125.0, 120))
                produitDao.insert(Produit(7,"Uncle Bob's Organic Dried Pears","Produits secs", 150.0, 15))

            }
        }
    }
}