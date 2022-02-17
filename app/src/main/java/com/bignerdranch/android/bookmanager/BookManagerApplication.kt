package com.bignerdranch.android.bookmanager

import android.app.Application
import com.bignerdranch.android.bookmanager.logic.BookRepository

class BookManagerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        BookRepository.initialize(this)
    }
}
