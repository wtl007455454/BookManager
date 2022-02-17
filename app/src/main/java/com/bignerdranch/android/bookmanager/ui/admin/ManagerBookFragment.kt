package com.bignerdranch.android.bookmanager.ui.admin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.Book
import java.io.Serializable
import java.util.*

private const val TAG = "ManagerBookFragment"

class ManagerBookFragment: Fragment() {

    interface CallBacks{
        fun onSelectBook(bookId: UUID)
    }
    private var callBacks: CallBacks? = null

    private lateinit var manageBookRecyclerView: RecyclerView
    private var adapter: BookAdapter? = BookAdapter(emptyList())

    private val managerBookViewModel: ManagerBookViewModel by lazy {
        ViewModelProviders.of(this).get(ManagerBookViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_book,container,false)

        manageBookRecyclerView = view.findViewById(R.id.manageBook_recyclerView)
        manageBookRecyclerView.layoutManager = LinearLayoutManager(context)
        manageBookRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        managerBookViewModel.booksLiveData.observe(viewLifecycleOwner, Observer { books ->
            Log.d(TAG,"books集合数据改变")
            updateUI(books)
        })
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    private fun updateUI(books: List<Book>){
        adapter = BookAdapter(books)
        manageBookRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_book_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_book -> {
                val book = Book()
                managerBookViewModel.addBook(book)
                callBacks?.onSelectBook(book.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class BookHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener {

        private lateinit var book: Book
        val undercarriageBtn: Button = itemView.findViewById(R.id.undercarriageBtn)
        val bookNameTextView: TextView = itemView.findViewById(R.id.bookName)
        val authorTextView: TextView = itemView.findViewById(R.id.author)

        init {
            itemView.setOnClickListener(this)
            undercarriageBtn.setOnClickListener {
                Log.d(TAG,"下架: ${book.bookName}")
                AlertDialog.Builder(this@ManagerBookFragment.requireContext()).apply {
                    setTitle("提示")
                    setMessage("您确定要下架此书吗?")
                    setCancelable(false)
                    setPositiveButton("确定") { dialog, which ->
                        managerBookViewModel.deleteBook(book)
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
        }

        override fun onClick(v: View?) {
            callBacks?.onSelectBook(book?.id!!)
        }
    }

    private inner class BookAdapter(var books: List<Book>): RecyclerView.Adapter<BookHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
            val view = layoutInflater.inflate(R.layout.list_item_book,parent,false)
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
            return ManagerBookFragment()
        }
    }
}
