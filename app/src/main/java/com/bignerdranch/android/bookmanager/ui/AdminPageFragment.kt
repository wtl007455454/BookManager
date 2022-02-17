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

class AdminPageFragment: Fragment() {

    interface CallBacks{
        fun onStartOtherAdminActivity(loadPageFlage: Int)
        fun openAnnounce()
    }
    private var callBacks: CallBacks? = null

    private var name: String? = null
    private lateinit var welcomeTextView: TextView
    private lateinit var managerBookBtn: Button
    private lateinit var dealLendBtn: Button
    private lateinit var dealLendBackBtn: Button
    private lateinit var announceBtn: Button

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        name = arguments?.getString(ARG_USERNAME) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_page,container,false)

        welcomeTextView = view.findViewById(R.id.welcomeTextView)
        managerBookBtn = view.findViewById(R.id.managerBookBtn)
        dealLendBtn = view.findViewById(R.id.dealLendBtn)
        dealLendBackBtn = view.findViewById(R.id.dealLendBackBtn)
        announceBtn = view.findViewById(R.id.announceBtn)

        return view
    }

    override fun onStart() {
        super.onStart()

        welcomeTextView.text = "您好！ $name"

        managerBookBtn.setOnClickListener {
            callBacks?.onStartOtherAdminActivity(0)
        }

        dealLendBtn.setOnClickListener {
            callBacks?.onStartOtherAdminActivity(1)
        }

        dealLendBackBtn.setOnClickListener {
            callBacks?.onStartOtherAdminActivity(2)
        }

        announceBtn.setOnClickListener {
            callBacks?.openAnnounce()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    companion object{
        fun newInstance(name: String): Fragment{
            return AdminPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME,name)
                }
            }
        }
    }
}
