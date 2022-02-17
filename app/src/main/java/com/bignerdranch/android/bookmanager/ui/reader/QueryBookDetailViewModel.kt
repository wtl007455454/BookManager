package com.bignerdranch.android.bookmanager.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository
import java.util.*

class QueryBookDetailViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()
    private val bookIdLiveData = MutableLiveData<UUID>()
    var userName: String = ""

    var bookLiveData: LiveData<Book?> = Transformations.switchMap(bookIdLiveData){ bookId ->
        bookRepository.getBook(bookId)
    }

    fun loadBook(bookId: UUID){
        bookIdLiveData.value = bookId
    }

    fun updateBook(book: Book){
        bookRepository.updateBook(book)
    }
}