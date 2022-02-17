package com.bignerdranch.android.bookmanager.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository

class DealLendViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()

    val lendBooksLiveData: LiveData<List<Book>> = bookRepository.getBooksByRequestLend(true)

    fun updateBook(book: Book){
        bookRepository.updateBook(book)
    }
}
