package com.example.data.favorites

import androidx.room.TypeConverter

class FavoriteConverters {

    @TypeConverter
    fun fromCourseIdsList(courseIds: List<String>): String {
        return courseIds.joinToString(",")
    }

    @TypeConverter
    fun toCourseIdsList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}
