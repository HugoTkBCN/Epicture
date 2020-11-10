package com.epitech.epicture

import com.epitech.epicture.R.layout.activity_my_login_page
import kotlinx.android.synthetic.main.activity_my_login_page.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MyLoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_my_login_page)

        loginImgur.setOnClickListener {
            startActivity(Intent(this, MyWebActivity::class.java))
        }
    }
}
