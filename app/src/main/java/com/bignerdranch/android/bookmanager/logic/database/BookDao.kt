package com.bignerdranch.android.bookmanager.logic.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.bookmanager.logic.Book
import java.util.*

@Dao
interface BookDao {

    @Query("select * from Book")
    fun getBooks(): LiveData<List<Book>>

    @Query("select * from Book where requestLended = (:flag)")
    fun getBooksByRequestLend(flag: Boolean): LiveData<List<Book>>

    @Query("select * from Book where isLend = (:flag) and lendPerson = (:userName)")
    fun getBooksByRequestBack(flag: Boolean,userName: String): LiveData<List<Book>>

    @Query("select * from Book where requestBack = (:flag)")
    fun getBooksByRequestBack(flag: Boolean): LiveData<List<Book>>

    @Query("select * from Book where id = (:id)")
    fun getBook(id: UUID): LiveData<Book?>

    @Update
    fun updateBook(book: Book)

    @Insert
    fun addBook(book: Book)

    @Delete
    fun deleteBook(book: Book)
}
