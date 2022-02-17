package com.bignerdranch.android.bookmanager

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookmanager.ui.*
import com.bignerdranch.android.bookmanager.ui.admin.AdminServiceActivity
import com.bignerdranch.android.bookmanager.ui.admin.AnnounceFragment
import com.bignerdranch.android.bookmanager.ui.reader.ReaderServiceActivity

private const val REQUEST_CODE_MANAGERBOOK = 0
private const val REQUEST_CODE_QUERYBOOK = 1

class BookManagerActivity : AppCompatActivity(),
    LoginFragment.CallBacks,
    LoginAccountFragment.CallBacks,
    HomePageFragment.CallBacks,
    AdminPageFragment.CallBacks{

    private lateinit var toolBar: Toolbar
    private lateinit var labelTextView: TextView
    private lateinit var homePageBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var fm: FragmentManager

    private val bookManagerViewModel: BookManagerViewModel by lazy {
        ViewModelProviders.of(this).get(BookManagerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.colorPrimary)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_book_manager)

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        labelTextView = findViewById(R.id.label_text_view)
        labelTextView.text = "天立图书管理系统"

        homePageBtn = findViewById(R.id.homeBtn)
        loginBtn = findViewById(R.id.loginBtn)

        fm = supportFragmentManager
        val currentFragment = fm.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            val fragment = HomePageFragment.newInstance(bookManagerViewModel.isLogin,bookManagerViewModel.userName)
            fm.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        homePageBtn.setOnClickListener {
            if(supportFragmentManager.findFragmentById(R.id.fragment_container) !is HomePageFragment && bookManagerViewModel.userJuris == 0){
                replaceFragment(HomePageFragment.newInstance(bookManagerViewModel.isLogin,bookManagerViewModel.userName))
            }else if(supportFragmentManager.findFragmentById(R.id.fragment_container) !is AdminPageFragment && bookManagerViewModel.userJuris == 1){
                replaceFragment(AdminPageFragment.newInstance(bookManagerViewModel.userName))
            }
        }

        loginBtn.setOnClickListener {
            if(supportFragmentManager.findFragmentById(R.id.fragment_container) !is LoginFragment && bookManagerViewModel.isLogin == false){
                replaceFragment(LoginFragment.newInstance())
            }else if(supportFragmentManager.findFragmentById(R.id.fragment_container) !is LoginAccountFragment && bookManagerViewModel.isLogin == true){
                replaceFragment(LoginAccountFragment.newInstance(bookManagerViewModel.userName,bookManagerViewModel.userJuris))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_MANAGERBOOK){

        }
        if(requestCode == REQUEST_CODE_QUERYBOOK){

        }
    }

    override fun onLoginSuccess(name: String,juris: Int) {
        bookManagerViewModel.userName = name
        bookManagerViewModel.userJuris = juris
        bookManagerViewModel.isLogin = true
        replaceFragment(LoginAccountFragment.newInstance(name,juris))
    }

    override fun onQuitLogin() {
        bookManagerViewModel.isLogin = false
        bookManagerViewModel.userJuris = 0
        replaceFragment(LoginFragment.newInstance())
    }

    override fun onHomePageLogin() {
        replaceFragment(LoginFragment())
    }

    override fun openAnnounce() {
        fm.beginTransaction()
            .replace(R.id.fragment_container,AnnounceFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onStartOtherAdminActivity(loadPageFlag: Int) {
        val intent = AdminServiceActivity.newIntent(this,loadPageFlag)
        startActivityForResult(intent, REQUEST_CODE_MANAGERBOOK)
    }

    override fun onStartOtherReaderActivity(loadPageFlag: Int) {
        val intent = ReaderServiceActivity.newIntent(this,bookManagerViewModel.userName,loadPageFlag)
        startActivityForResult(intent, REQUEST_CODE_QUERYBOOK)
    }

    private fun replaceFragment(target: Fragment){
        fm.beginTransaction()
            .replace(R.id.fragment_container,target)
            .commit()
    }
}
