package com.bignerdranch.android.bookmanager.logic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.bookmanager.logic.database.BookDao
import com.bignerdranch.android.bookmanager.logic.database.BookDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class BookRepository private constructor(context: Context) {

    private val database: BookDatabase = Room.databaseBuilder(
        context.applicationContext, BookDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val bookDao: BookDao = database.bookDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getBooks(): LiveData<List<Book>> = bookDao.getBooks()

    fun getBooksByRequestLend(flag: Boolean): LiveData<List<Book>> = bookDao.getBooksByRequestLend(flag)

    fun getBooksByRequestBack(flag: Boolean,userName: String): LiveData<List<Book>> = bookDao.getBooksByRequestBack(flag,userName)

    fun getBooksByRequestBack(flag: Boolean): LiveData<List<Book>> = bookDao.getBooksByRequestBack(flag)

    fun getBook(id: UUID): LiveData<Book?> = bookDao.getBook(id)

    fun updateBook(book: Book){
        executor.execute {
            bookDao.updateBook(book)
        }
    }

    fun addBook(book: Book){
        executor.execute {
            bookDao.addBook(book)
        }
    }

    fun deleteBook(book: Book){
        executor.execute {
            bookDao.deleteBook(book)
        }
    }

    companion object {
        private var INSTANCE: BookRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = BookRepository(context)
            }
        }
        fun get(): BookRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
