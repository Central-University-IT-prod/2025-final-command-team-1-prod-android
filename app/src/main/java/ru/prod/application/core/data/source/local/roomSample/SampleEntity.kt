package ru.prod.application.core.data.source.local.roomSample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample")
data class SampleEntity(
    @PrimaryKey val id: Int,
    val something: String
)
