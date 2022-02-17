package com.bignerdranch.android.bookmanager.ui.admin

import android.os.Bundle
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

class DealBackFragment: Fragment() {

    private val dealBackViewModel: DealBackViewModel by lazy {
        ViewModelProviders.of(this).get(DealBackViewModel::class.java)
    }

    private lateinit var dealBackRecyclerView: RecyclerView
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deal_back,container,false)

        dealBackRecyclerView = view.findViewById(R.id.dealBack_recyclerView)
        dealBackRecyclerView.layoutManager = LinearLayoutManager(context)
        dealBackRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dealBackViewModel.backBooksLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
            }
        })
    }

    private fun updateUI(books: List<Book>){
        adapter = BookAdapter(books)
        dealBackRecyclerView.adapter = adapter
    }

    private inner class BookHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        private lateinit var book: Book
        val dealBackBtn: Button = itemView.findViewById(R.id.dealBackBtn)
        val bookNameTextView: TextView = itemView.findViewById(R.id.bookName)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val requestPersonTextView: TextView = itemView.findViewById(R.id.requestPerson)

        init {
            dealBackBtn.setOnClickListener {
                AlertDialog.Builder(this@DealBackFragment.requireContext()).apply {
                    setTitle("提示")
                    setMessage("用户${book.lendPerson}申请还书，请操作！")
                    setCancelable(false)
                    setPositiveButton("同意") { dialog, which ->
                        if(book.isLend){
                            book.requestBack = false
                            book.isLend = false
                            dealBackViewModel.updateBook(book)
                        }else{
                            Toast.makeText(
                                this@DealBackFragment.requireContext(),
                                "错误，书未借出",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    setNegativeButton("拒绝") { dialog, which ->
                        book.requestBack = false
                        book.isLend = true
                        dealBackViewModel.updateBook(book)
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
            val view = layoutInflater.inflate(R.layout.list_item_deal_back,parent,false)
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
        fun newInstance(): Fragment{
            return DealBackFragment()
        }
    }
}