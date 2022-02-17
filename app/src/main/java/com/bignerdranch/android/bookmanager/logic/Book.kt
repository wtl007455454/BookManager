package com.bignerdranch.android.bookmanager.logic

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Book(@PrimaryKey val id: UUID = UUID.randomUUID(),
                            var bookName: String = "",
                            var author: String = "",
                            var publisher: String = "",
                            var isbn: String = "",
                            var isLend: Boolean = false,
                            var requestLended: Boolean = false,
                            var requestBack: Boolean = false,
                            var lendPerson: String = "")