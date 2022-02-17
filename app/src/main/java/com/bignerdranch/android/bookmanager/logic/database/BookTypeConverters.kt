package com.bignerdranch.android.bookmanager.logic.database

import androidx.room.TypeConverter
import java.util.*

class BookTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}
