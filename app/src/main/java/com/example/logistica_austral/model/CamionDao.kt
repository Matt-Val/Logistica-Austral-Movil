package com.example.logistica_austral.model
import androidx.room.*

@Dao
interface CamionDao {
    @Query("SELECT * FROM camiones ORDER BY patente DESC")
    suspend fun obtenerUsuarios(): List<Camion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(camion : Camion)

    @Delete
    suspend fun eliminar(camion : Camion)

}