package com.bignerdranch.android.bookmanager.ui.admin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookmanager.R
import java.util.*

private const val TAG = "AdminServiceActivity"
private const val EXTRA_LOADPAGEFLAG = "extra_loadPageFlag"

class AdminServiceActivity : AppCompatActivity(),ManagerBookFragment.CallBacks {

    private lateinit var toolBar: Toolbar
    private lateinit var labelTextView: TextView
    private lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.colorPrimary)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_admin_service)

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        labelTextView = findViewById(R.id.label_text_view)
        labelTextView.text = "管理员页面"

        fm = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)
        val loadPageFlag: Int = intent.getIntExtra(EXTRA_LOADPAGEFLAG,0)
        if(currentFragment == null){
            when(loadPageFlag){
                0 -> addFragment(ManagerBookFragment.newInstance())
                1 -> addFragment(DealLendFragment.newInstance())
                2 -> addFragment(DealBackFragment.newInstance())
                else -> addFragment(ManagerBookFragment.newInstance())
            }
        }

        setCallBackResult()
    }

    override fun onSelectBook(bookId: UUID) {
        replaceFragment(BookDetailFragment.newInstance(bookId))
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

    private fun setCallBackResult(){
        setResult(Activity.RESULT_OK)
    }

    companion object{
        fun newIntent(pachageContext: Context,loadPageFlag: Int): Intent{
            return Intent(pachageContext,AdminServiceActivity::class.java).apply {
                putExtra(EXTRA_LOADPAGEFLAG,loadPageFlag)
            }
        }
    }
}
