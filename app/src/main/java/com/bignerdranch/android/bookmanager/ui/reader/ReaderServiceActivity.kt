package com.bignerdranch.android.bookmanager.ui.reader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.ui.admin.ManagerBookFragment
import java.util.*

private const val TAG = "ReaderServiceActivity"
private const val EXTRA_USERNAME = "extra_userName"
private const val EXTRA_LOADPAGEFLAG = "extra_loadPageFlag"

class ReaderServiceActivity : AppCompatActivity(),QueryBookFragment.CallBacks {

    private lateinit var toolBar: Toolbar
    private lateinit var labelTextView: TextView
    private lateinit var fm: FragmentManager
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.colorPrimary)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_reader_service)

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        labelTextView = findViewById(R.id.label_text_view)
        labelTextView.text = "读者页面"

        userName = intent.getStringExtra(EXTRA_USERNAME) as String

        fm = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)
        val loadPageFlag: Int = intent.getIntExtra(EXTRA_LOADPAGEFLAG,0)
        val name: String = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        if(currentFragment == null){
            when(loadPageFlag){
                0 -> addFragment(QueryBookFragment.newInstance())
                1 -> addFragment(RequestBackFragment.newInstance(name))
                else -> addFragment(QueryBookFragment.newInstance())
            }
        }

        setCallBackResult()
    }

    private fun setCallBackResult(){
        setResult(Activity.RESULT_OK)
    }

    override fun onSelectBook(bookId: UUID) {
        Log.d(TAG,"id: $bookId")
        replaceFragment(QueryBookDetailFragment.newInstance(bookId,userName ?: ""))
    }

    private fun addFragment(target: Fragment){
        fm.beginTransaction()
            .add(R.id.fragment_container,target)
            .commit()
    }

    private fun replaceFragment(target: Fragment){
        fm.beginTransaction()
            .replace(R.id.fragment_container,target)
            .addToBackStack(null)
            .commit()
    }

    companion object{
        fun newIntent(pachageContext: Context,userName: String,loadPageFlag: Int): Intent {
            return Intent(pachageContext, ReaderServiceActivity::class.java).apply {
                putExtra(EXTRA_USERNAME,userName)
                putExtra(EXTRA_LOADPAGEFLAG,loadPageFlag)
            }
        }
    }
}
