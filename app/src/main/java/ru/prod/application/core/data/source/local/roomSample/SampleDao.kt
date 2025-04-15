package ru.prod.application.core.data.source.local.roomSample

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSomething(something: SampleEntity)

    @Query("SELECT * FROM sample")
    fun getSomeList(): List<SampleEntity>

    @Query("SELECT * FROM sample WHERE id = :somethingId")
    fun getSomethingById(somethingId: Int): SampleEntity
}