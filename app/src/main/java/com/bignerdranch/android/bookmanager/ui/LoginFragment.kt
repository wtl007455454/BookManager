package com.bignerdranch.android.bookmanager.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookmanager.R

class LoginFragment: Fragment() {

    interface CallBacks{
        fun onLoginSuccess(name: String,juris: Int)
    }

    private var callBacks: CallBacks? = null
    private lateinit var userAccount: TextView
    private lateinit var userPassword: TextView
    private lateinit var loginInButton: Button

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login,container,false)

        userAccount = view.findViewById(R.id.user_account)
        userPassword = view.findViewById(R.id.user_password)
        loginInButton = view.findViewById(R.id.login_in_button)

        return view
    }

    override fun onStart() {
        super.onStart()

        loginInButton.setOnClickListener {
            val isCorrect = checkIsCorrect(userAccount.text.toString(),userPassword.text.toString())
            if(isCorrect == 0){
                Toast.makeText(requireContext(),"登录成功",Toast.LENGTH_SHORT).show()
                callBacks?.onLoginSuccess(userAccount.text.toString(),0)
            }else if(isCorrect == 1){
                Toast.makeText(requireContext(),"登录成功",Toast.LENGTH_SHORT).show()
                callBacks?.onLoginSuccess(userAccount.text.toString(),1)
            }else{
                Toast.makeText(requireContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    private fun checkIsCorrect(userAccount: String,userPassword: String): Int{
        if(userAccount == "reader" && userPassword == "123"){
            return 0
        }else if(userAccount == "admin" && userPassword == "123"){
            return 1
        }else{
            return -1
        }
    }

    companion object{
        fun newInstance(): Fragment{
            return LoginFragment()
        }
    }
}