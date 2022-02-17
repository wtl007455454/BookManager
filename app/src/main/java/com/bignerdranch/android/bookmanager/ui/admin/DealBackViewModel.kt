package com.bignerdranch.android.bookmanager.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository

class DealBackViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()

    val backBooksLiveData: LiveData<List<Book>> = bookRepository.getBooksByRequestBack(true)

    fun updateBook(book: Book){
        bookRepository.updateBook(book)
    }
}