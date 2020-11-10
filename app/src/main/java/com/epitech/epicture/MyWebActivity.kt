package com.epitech.epicture

import com.epitech.epicture.R.layout.activity_my_web
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.os.Bundle
import android.content.Intent
import android.net.Uri

class MyWebActivity : AppCompatActivity() {

    private fun getUrl(url: Uri) {
        val myIntent = Intent(this, MainActivity::class.java)
        myIntent.putExtra("access_token", url.getQueryParameter("access_token"))
        myIntent.putExtra("refresh_token", url.getQueryParameter("refresh_token"))
        myIntent.putExtra("account_username", url.getQueryParameter("account_username"))
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_my_web)

        val weView = findViewById<WebView>(R.id.webView)
        weView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url!!.contains("access_token="))
                    getUrl(Uri.parse(url.toString().replace('#', '?')))
                return (true)
            }
        }
        weView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=6e93f5e0d0377fb&response_type=token")
    }
}
