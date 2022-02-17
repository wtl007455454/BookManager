package com.bignerdranch.android.bookmanager.ui.reader

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.Book
import java.util.*

class QueryBookFragment: Fragment() {

    interface CallBacks{
        fun onSelectBook(bookId: UUID)
    }
    private var callBacks: CallBacks? = null

    private lateinit var queryBookRecyclerView: RecyclerView
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    private val queryBookViewModel: QueryBookViewModel by lazy {
        ViewModelProviders.of(this).get(QueryBookViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_query_book,container,false)

        queryBookRecyclerView = view.findViewById(R.id.queryBook_recyclerView)
        queryBookRecyclerView.layoutManager = LinearLayoutManager(context)
        queryBookRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryBookViewModel.booksLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            updateUI(it)
        })
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    private fun updateUI(books: List<Book>){
        adapter = BookAdapter(books)
        queryBookRecyclerView.adapter = adapter
    }

    private inner class BookHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var book: Book
        val bookNameTextView: TextView = itemView.findViewById(R.id.bookName)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val stockTextView: TextView = itemView.findViewById(R.id.stockTextView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: Book){
            this.book = book
            bookNameTextView.text = book.bookName
            authorTextView.text = book.author
            if(book.isLend){
                stockTextView.setBackgroundResource(R.drawable.undercarriagebtnshape)
                stockTextView.text = "已借完"
            }else{
                stockTextView.setBackgroundResource(R.drawable.stockshape)
                stockTextView.text = "有库存"
            }
        }

        override fun onClick(v: View?) {
            callBacks?.onSelectBook(book.id)
        }
    }

    private inner class BookAdapter(var books: List<Book>): RecyclerView.Adapter<BookHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val view = layoutInflater.inflate(R.layout.list_item_querybook,parent,false)
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
            return QueryBookFragment()
        }
    }
}