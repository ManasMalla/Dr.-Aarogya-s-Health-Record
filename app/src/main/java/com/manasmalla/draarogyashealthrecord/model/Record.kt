package com.manasmalla.draarogyashealthrecord.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "record", foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("uId"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record: Map<Metrics, Double>,
    val date: Date,
    @ColumnInfo(index = true) val userId: Int
)
