package com.bignerdranch.android.bookmanager.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookmanager.R

private const val ARG_USERNAME = "arg_userName"
private const val ARG_FLAG_JURIS = "arg_flagJuris"

class LoginAccountFragment: Fragment() {

    interface CallBacks{
        fun onQuitLogin()
    }
    private var callBacks: CallBacks? = null

    private lateinit var userName: TextView
    private lateinit var userJurisdiction: TextView
    private lateinit var quitButton: Button
    private var name: String? = null
    private var juris: Int? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        name = arguments?.getString(ARG_USERNAME) as String
        juris = arguments?.getInt(ARG_FLAG_JURIS) as Int
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_account,container,false)

        userName = view.findViewById(R.id.user_name)
        userJurisdiction = view.findViewById(R.id.user_jurisdiction)
        quitButton = view.findViewById(R.id.quitButton)

        return view
    }

    override fun onStart() {
        super.onStart()

        userName.text = name
        if(juris == 0){
            userJurisdiction.text = "身份权限: 读者"
        }
        if(juris == 1){
            userJurisdiction.text = "身份权限: 管理者"
        }

        quitButton.setOnClickListener {
            callBacks?.onQuitLogin()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    companion object{
        fun newInstance(userName: String,flagJurisdiction: Int): Fragment{
            return LoginAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME,userName)
                    putInt(ARG_FLAG_JURIS,flagJurisdiction)
                }
            }
        }
    }
}
