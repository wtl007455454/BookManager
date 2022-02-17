package com.bignerdranch.android.bookmanager.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository

class ManagerBookViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()

    val booksLiveData: LiveData<List<Book>> = bookRepository.getBooks()

    fun addBook(book: Book){
        bookRepository.addBook(book)
    }

    fun deleteBook(book: Book){
        bookRepository.deleteBook(book)
    }
}