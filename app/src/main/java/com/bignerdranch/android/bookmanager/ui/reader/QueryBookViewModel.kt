package com.bignerdranch.android.bookmanager.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository

class QueryBookViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()

    val booksLiveData: LiveData<List<Book>> = bookRepository.getBooks()
}
