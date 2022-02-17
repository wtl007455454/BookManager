package com.bignerdranch.android.bookmanager.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.Book
import java.util.*

private const val ARG_BOOKID = "arg_bookId"

class BookDetailFragment: Fragment() {

    private val bookDetailViewModel: BookDetailViewModel by lazy {
        ViewModelProviders.of(this).get(BookDetailViewModel::class.java)
    }

    private lateinit var book: Book
    private lateinit var bookName: EditText
    private lateinit var author: EditText
    private lateinit var publisher: EditText
    private lateinit var isbnFlag: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
        val bookId: UUID = arguments?.getSerializable(ARG_BOOKID) as UUID
        bookDetailViewModel.loadBook(bookId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail,container,false)

        bookName = view.findViewById(R.id.bookName)
        author = view.findViewById(R.id.author)
        publisher = view.findViewById(R.id.publisher)
        isbnFlag = view.findViewById(R.id.isbnFlag)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookDetailViewModel.bookLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                this.book = it
                updateUI()
            }
        })
    }

    override fun onStop() {
        super.onStop()

        book.bookName = bookName.text.toString()
        book.author = author.text.toString()
        book.publisher = publisher.text.toString()
        book.isbn = isbnFlag.text.toString()
        
        bookDetailViewModel.saveBook(book)
        Log.d("BookDetailFragment","保存书本信息")
    }

    private fun updateUI(){
        bookName.setText(book.bookName)
        author.setText(book.author)
        publisher.setText(book.publisher)
        isbnFlag.setText(book.isbn)
    }

    companion object{
        fun newInstance(bookId: UUID): Fragment{
            return BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_BOOKID,bookId)
                }
            }
        }
    }
}
