package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyFavorisFragment : BaseActivity() {

    companion object {
        fun createInstance(accessToken: String, refreshToken: String, username: String): MyFavorisFragment {
            val args = Bundle()
            val fragment = MyFavorisFragment()
            args.putString("access_token", accessToken)
            args.putString("refresh_token", refreshToken)
            args.putString("username", username)
            fragment.arguments = args
            return (fragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val returnView = inflater.inflate(R.layout.activity_my_favoris_fragment, container, false)
        try {
            val url = "https://api.imgur.com/3/account/"  .plus(arguments?.getString("username")) .plus("/favorites")
            getInfosFromGalery(returnView, url, 2)
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return (returnView)
    }
}
