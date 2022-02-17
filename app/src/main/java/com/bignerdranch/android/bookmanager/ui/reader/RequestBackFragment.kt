package com.bignerdranch.android.bookmanager.ui.reader

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.Book

private const val TAG = "RequestBackFragment"
private const val ARG_USERNAME = "arg_userName"

class RequestBackFragment: Fragment() {

    private lateinit var requestBackViewModel: RequestBackViewModel

    private lateinit var requestBackRecyclerView: RecyclerView
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userName: String = arguments?.getString(ARG_USERNAME) ?: ""
        requestBackViewModel = ViewModelProviders.of(this).get(RequestBackViewModel::class.java)
        requestBackViewModel.putuserName(userName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_request_back,container,false)

        requestBackRecyclerView = view.findViewById(R.id.requestBack_recyclerView)
        requestBackRecyclerView.layoutManager = LinearLayoutManager(context)
        requestBackRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestBackViewModel.backBooksLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d(TAG,"shuju daolai:$it")
                updateUI(it)
            }
        })
    }

    private fun updateUI(books: List<Book>){
        adapter = BookAdapter(books)
        requestBackRecyclerView.adapter = adapter
    }

    private inner class BookHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private lateinit var book: Book
        val requestBackBtn: Button = itemView.findViewById(R.id.requestBackBtn)
        val bookNameTextView: TextView = itemView.findViewById(R.id.bookName)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val requestPersonTextView: TextView = itemView.findViewById(R.id.requestPerson)

        init {
            requestBackBtn.setOnClickListener {
                AlertDialog.Builder(this@RequestBackFragment.requireContext()).apply {
                    setTitle("提示")
                    setMessage("您确定现在归还此书吗？")
                    setCancelable(false)
                    setPositiveButton("确定") { dialog, which ->
                        if(book.isLend){
                            if(!book.requestBack){
                                book.requestBack = true
                                requestBackViewModel.updateBook(book)
                            }else{
                                Toast.makeText(
                                    this@RequestBackFragment.requireContext(),
                                    "已经申请归还",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{
                            Toast.makeText(
                                this@RequestBackFragment.requireContext(),
                                "出错，书未借出",
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

        fun bind(book: Book){
            this.book = book
            bookNameTextView.text = book.bookName
            authorTextView.text = book.author
            requestPersonTextView.text = book.lendPerson
        }
    }

    private inner class BookAdapter(var books: List<Book>): RecyclerView.Adapter<BookHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val view = layoutInflater.inflate(R.layout.list_item_request_back,parent,false)
            return BookHolder(view)
        }

        override fun getItemCount(): Int {
            return books.size
        }

        override fun onBindViewHolder(holder: BookHolder, position: Int) {
            val book = books[position]
            holder.bind(book)
        }
    }

    companion object{
        fun newInstance(userName: String): Fragment{
            return RequestBackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME,userName)
                }
            }
        }
    }
}