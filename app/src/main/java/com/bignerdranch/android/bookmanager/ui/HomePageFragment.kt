package com.bignerdranch.android.bookmanager.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.QueryPreferences

private const val ARG_ISLOGIN = "arg_isLogin"
private const val ARG_USERNAME = "arg_userName"

class HomePageFragment: Fragment() {

    interface CallBacks{
        fun onHomePageLogin()
        fun onStartOtherReaderActivity(loadPageFlag: Int)
    }
    private var callBacks: CallBacks? = null

    private lateinit var loginTipTextView: TextView
    private lateinit var homeLoginBtn: Button
    private var isLogin: Boolean? = null
    private var name: String? = null
    private lateinit var welcomeTextView: TextView
    private lateinit var queryBookBtn: Button
    private lateinit var requestBackBtn: Button
    private lateinit var watchAnnounce: Button

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLogin = arguments?.getBoolean(ARG_ISLOGIN) as Boolean
        name = arguments?.getString(ARG_USERNAME) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page,container,false)

        loginTipTextView = view.findViewById(R.id.loginTip)
        homeLoginBtn = view.findViewById(R.id.home_login_Btn)
        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        queryBookBtn = view.findViewById(R.id.queryBookBtn)
        requestBackBtn = view.findViewById(R.id.requestBackBtn)
        watchAnnounce = view.findViewById(R.id.watchAnnounce)

        return view
    }

    override fun onStart() {
        super.onStart()

        if(isLogin ?: false){
            loginTipTextView.visibility = View.GONE
            homeLoginBtn.visibility = View.GONE
            welcomeTextView.visibility = View.VISIBLE
            welcomeTextView.text = "您好！ $name"
        }else{
            loginTipTextView.visibility = View.VISIBLE
            homeLoginBtn.visibility = View.VISIBLE
            homeLoginBtn.setOnClickListener {
                callBacks?.onHomePageLogin()
            }
            welcomeTextView.visibility = View.GONE
        }

        queryBookBtn.setOnClickListener {
            if(isLogin ?: false){
                callBacks?.onStartOtherReaderActivity(0)
            }else{
                Toast.makeText(this.requireContext(),"请先登录",Toast.LENGTH_SHORT).show()
            }
        }

        requestBackBtn.setOnClickListener {
            if(isLogin ?: false){
                callBacks?.onStartOtherReaderActivity(1)
            }else{
                Toast.makeText(this.requireContext(),"请先登录",Toast.LENGTH_SHORT).show()
            }
        }

        watchAnnounce.setOnClickListener {
            AlertDialog.Builder(this.requireContext()).apply {
                setTitle("公告")
                setMessage(QueryPreferences.getStoredQuery(requireActivity()))
                setCancelable(false)
                setPositiveButton("知道了") { dialog, which ->
                }
                show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    companion object{
        fun newInstance(isLogin: Boolean,name: String): Fragment{
            return HomePageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_ISLOGIN,isLogin)
                    putString(ARG_USERNAME,name)
                }
            }
        }
    }
}
