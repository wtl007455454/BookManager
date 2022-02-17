package com.bignerdranch.android.bookmanager.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookmanager.R
import com.bignerdranch.android.bookmanager.logic.QueryPreferences

class AnnounceFragment: Fragment() {

    private lateinit var announceText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_announce,container,false)

        announceText = view.findViewById(R.id.announceText)

        return view
    }

    override fun onStart() {
        super.onStart()
        announceText.setText(QueryPreferences.getStoredQuery(requireActivity()))
    }

    override fun onStop() {
        super.onStop()
        QueryPreferences.setStoredQuery(requireActivity(),announceText.text.toString())
    }

    companion object{
        fun newInstance(): Fragment{
            return AnnounceFragment()
        }
    }
}