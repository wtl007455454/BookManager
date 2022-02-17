package com.bignerdranch.android.bookmanager.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.bookmanager.logic.Book

@Database(entities = [Book::class],version = 1)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao
}