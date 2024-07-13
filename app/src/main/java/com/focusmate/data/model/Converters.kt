package com.focusmate.data.model

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromListInt(value: List<Int>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toListInt(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }
}
