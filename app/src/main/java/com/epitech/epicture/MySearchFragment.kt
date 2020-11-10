package com.epitech.epicture

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_my_search_fragment.*


class MySearchFragment : BaseActivity() {

    companion object {
        fun createInstance(accessToken: String): MySearchFragment {
            val args = Bundle()
            val fragment = MySearchFragment()
            args.putString("access_token", accessToken)
            fragment.arguments = args
            return (fragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val returnView = inflater.inflate(R.layout.activity_my_search_fragment, container, false)
        try {
            val searchButton = returnView.findViewById(R.id.searchButton) as Button
            val inputMethod = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            searchButton.setOnClickListener {
                inputMethod.hideSoftInputFromWindow(returnView.windowToken, 0)
                getInfosFromGalery(returnView, "https://api.imgur.com/3/gallery/search/q_all/all/0/?q=" .plus(searchText.text), 3)
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        return (returnView)
    }
}
