package com.bignerdranch.android.bookmanager.ui.reader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookmanager.logic.Book
import com.bignerdranch.android.bookmanager.logic.BookRepository

class RequestBackViewModel: ViewModel() {

    private val bookRepository = BookRepository.get()

    val userNameLiveData = MutableLiveData<String>()

    var backBooksLiveData: LiveData<List<Book>> = Transformations.switchMap(userNameLiveData){
        bookRepository.getBooksByRequestBack(true,it)
    }

    fun putuserName(name: String){
        userNameLiveData.value = name
    }

    fun updateBook(book: Book){
        bookRepository.updateBook(book)
    }
}