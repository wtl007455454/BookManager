package com.bignerdranch.android.bookmanager.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.Book
import java.util.*

private const val ARG_BOOKID = "arg_bookId"
private const val ARG_USERNAME = "arg_userName"

class QueryBookDetailFragment: Fragment() {

    private lateinit var book: Book
    private lateinit var bookName: EditText
    private lateinit var author: EditText
    private lateinit var publisher: EditText
    private lateinit var isbnFlag: EditText
    private lateinit var requestLendBtn: Button

    private val queryBookDetailViewModel: QueryBookDetailViewModel by lazy {
        ViewModelProviders.of(this).get(QueryBookDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
        val bookId: UUID = arguments?.getSerializable(ARG_BOOKID) as UUID
        queryBookDetailViewModel.loadBook(bookId)
        val userName: String = arguments?.getString(ARG_USERNAME) as String
        queryBookDetailViewModel.userName = userName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_query_book_detail,container,false)

        bookName = view.findViewById(R.id.bookName)
        author = view.findViewById(R.id.author)
        publisher = view.findViewById(R.id.publisher)
        isbnFlag = view.findViewById(R.id.isbnFlag)
        requestLendBtn = view.findViewById(R.id.requestLendBtn)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryBookDetailViewModel.bookLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                this.book = it
                updateUI()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        requestLendBtn.setOnClickListener {
            AlertDialog.Builder(this.requireContext()).apply {
                setTitle("提示")
                setMessage("${queryBookDetailViewModel.userName}您确定要申请借此书吗?")
                setCancelable(false)
                setPositiveButton("确定") { dialog, which ->
                    if(!book.isLend){
                        if(!book.requestLended){
                            book.lendPerson = queryBookDetailViewModel.userName
                            book.requestLended = true
                            queryBookDetailViewModel.updateBook(book)
                        }else{
                            Toast.makeText(
                                this@QueryBookDetailFragment.requireContext(),
                                "有人正在申请借书",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            this@QueryBookDetailFragment.requireContext(),
                            "书已被借出",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                setNegativeButton("取消") { dialog, which ->
                }
                show()
            }
        }
    }

    private fun updateUI(){
        bookName.setText(book.bookName)
        author.setText(book.author)
        publisher.setText(book.publisher)
        isbnFlag.setText(book.isbn)
    }

    companion object{
        fun newInstance(bookId: UUID,userName: String): Fragment{
            return QueryBookDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_BOOKID,bookId)
                    putString(ARG_USERNAME,userName)
                }
            }
        }
    }
}
