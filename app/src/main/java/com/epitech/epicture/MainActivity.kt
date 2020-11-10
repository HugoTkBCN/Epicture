package com.epitech.epicture

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private fun createFragment(fragment: Fragment)
    {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.mainFrame, fragment)
        transaction.commit()
    }

    private fun setNavigationBar(accessToken : String, refreshToken : String, username : String): MyGaleryFragment? {
        var myUploadFragment: UploadFragment? = null
        var mySearchFragment: MySearchFragment? = null
        var myProfileFragment: MyProfileFragment? = null
        var myGaleryFragment: MyGaleryFragment? = null
        var myFavorisFragment: MyFavorisFragment? = null
        val navigationBar = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_gallery -> {
                    createFragment(myGaleryFragment as Fragment)
                    return@OnNavigationItemSelectedListener (true)
                }
                R.id.nav_search -> {
                    createFragment(mySearchFragment as Fragment)
                    return@OnNavigationItemSelectedListener (true)
                }
                R.id.nav_favoris -> {
                    createFragment(myFavorisFragment as Fragment)
                    return@OnNavigationItemSelectedListener (true)
                }
                R.id.nav_account -> {
                    createFragment(myProfileFragment as Fragment)
                    return@OnNavigationItemSelectedListener (true)
                }
                R.id.nav_upload -> {
                    createFragment(myUploadFragment as Fragment)
                    return@OnNavigationItemSelectedListener (true)
                }
            }
            false
        }

        setContentView(R.layout.activity_main)
        nav_view.setOnNavigationItemSelectedListener(navigationBar)

        myUploadFragment = UploadFragment.createInstance(accessToken)
        mySearchFragment = MySearchFragment.createInstance(accessToken)
        myGaleryFragment = MyGaleryFragment.createInstance(accessToken)
        myFavorisFragment = MyFavorisFragment.createInstance(accessToken, refreshToken, username)
        myProfileFragment = MyProfileFragment.createInstance(accessToken, refreshToken, username)
        return (myGaleryFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accessToken = intent.getStringExtra("access_token")
        val refreshToken = intent.getStringExtra("refresh_token")
        val username = intent.getStringExtra("account_username")
        val myGaleryFragment = setNavigationBar(accessToken, refreshToken, username)

        createFragment(myGaleryFragment as Fragment)
    }
}
