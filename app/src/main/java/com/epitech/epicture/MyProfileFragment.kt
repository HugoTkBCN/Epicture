package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyProfileFragment : BaseActivity() {

    companion object {
        fun createInstance(accessToken: String, refreshToken: String, username: String): MyProfileFragment {
            val args = Bundle()
            val fragment = MyProfileFragment()
            args.putString("access_token", accessToken)
            args.putString("refresh_token", refreshToken)
            args.putString("username", username)
            fragment.arguments = args
            return (fragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val returnView = inflater.inflate(R.layout.activity_my_profile_fragment, container, false)
        try {
            getInfosFromGalery(returnView, "https://api.imgur.com/3/account/me/" .plus(arguments?.getString("username")), 4)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return (returnView)
    }
}
