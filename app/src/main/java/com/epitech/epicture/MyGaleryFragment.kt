package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyGaleryFragment : BaseActivity() {

    companion object {
        fun createInstance(accessToken: String): MyGaleryFragment {
            val arg = Bundle()
            val fragment = MyGaleryFragment()
            arg.putString("access_token", accessToken)
            fragment.arguments = arg
            return (fragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val returnView = inflater.inflate(R.layout.activity_my_galery_fragment, container, false)
        try {
            getInfosFromGalery(returnView, "https://api.imgur.com/3/account/me/images", 1)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return (returnView)
    }
}
