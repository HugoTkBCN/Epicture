package com.epitech.epicture

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_my_profile_fragment.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


abstract class BaseActivity : Fragment() {

    private fun printGalery(info: JSONArray, galery: ArrayList<DataImage>, i: Int) {
        if (info.getJSONObject(i).getString("title") == "null" && info.getJSONObject(i).getString("description") == "null"
        )
            galery.add(DataImage(info.getJSONObject(i).getString("link"), "", ""))
        else if (info.getJSONObject(i).getString("description") == "null")
            galery.add(
                DataImage(
                    info.getJSONObject(i).getString("link"),
                    info.getJSONObject(i).getString("title"),
                    ""
                )
            )
        else if (info.getJSONObject(i).getString("title") == "null")
            galery.add(
                DataImage(
                    info.getJSONObject(i).getString("link"),
                    "",
                    info.getJSONObject(i).getString("description")
                )
            )
        else
            galery.add(
                DataImage(
                    info.getJSONObject(i).getString("link"),
                    info.getJSONObject(i).getString("title"),
                    info.getJSONObject(i).getString("description")
                )
            )
    }

    private fun printFavories(info: JSONArray, galery: ArrayList<DataImage>, i: Int) {
        val cover: String = info.getJSONObject(i).getString("cover")
        var link: String = "https://i.imgur.com/".plus(cover).plus(".png")
        if (info.getJSONObject(i).getString("type") == "video/mp4")
            link = "https://i.imgur.com/".plus(cover).plus(".mp4")

        if (info.getJSONObject(i).getString("title") == "null" && info.getJSONObject(i).getString("description") == "null")
            galery.add(
                DataImage(
                    link,
                    "",
                    ""
                )
            )
        else if (info.getJSONObject(i).getString("title") == "null")
            galery.add(
                DataImage(
                    link,
                    "",
                    info.getJSONObject(i).getString("description")
                )
            )
        else if (info.getJSONObject(i).getString("description") == "null")
            galery.add(
                DataImage(
                    link,
                    info.getJSONObject(i).getString("title"),
                    ""
                )
            )
        else
            galery.add(
                DataImage(
                    link,
                    info.getJSONObject(i).getString("title"),
                    info.getJSONObject(i).getString("description")
                )
            )
    }

    private fun printSearch(info: JSONArray, galery: ArrayList<DataImage>, i: Int) {
        val link: String = "https://i.imgur.com/".plus(info.getJSONObject(i).getString("cover")).plus(".png")
        if (info.getJSONObject(i).getString("title") == "null")
            galery.add(
                DataImage(
                    link,
                    "",
                    info.getJSONObject(i).getString("description")
                )
            )
        if (info.getJSONObject(i).getString("description") == "null")
            galery.add(
                DataImage(
                    link,
                    info.getJSONObject(i).getString("title"),
                    ""
                )
            )
    }

    private fun printAccountInfos(body: String?) {
        val info = JSONObject(body).getJSONObject("data")

        Thread(Runnable {
            activity!!.runOnUiThread(java.lang.Runnable {
                text_name.text = info.getString("url")
                text_reputation_points.text = info.getString("reputation").plus(" points")
                Picasso.with(context).load(info.getString("avatar")).into(profil_picture)
                text_reputation.text = info.getString("reputation_name")
                if (info.getString("bio") == "null")
                    text_description.text = ""
                else
                    text_description.text = info.getString("bio")
            })
        }).start()
    }

    private fun runStartThread(imageView: RecyclerView): RecyclerView {
        Thread {
            activity!!.runOnUiThread {
                val layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                imageView.layoutManager = layoutManager
            }
        }.start()
        return (imageView)
    }

    private fun printImage(body: String?, returnView: View, type: Int) {
        val info: JSONArray = JSONObject(body).getJSONArray("data")
        val size = info.length()
        val galery = ArrayList<DataImage>()
        var i = 0
        val imageView: RecyclerView

        if (type == 2)
            imageView = runStartThread(returnView.findViewById(R.id.images1) as RecyclerView)
        else if (type == 3)
            imageView = runStartThread(returnView.findViewById(R.id.images2) as RecyclerView)
        else
            imageView = runStartThread(returnView.findViewById(R.id.images) as RecyclerView)
        while (i < size) {
            if (type == 2)
                printFavories(info, galery, i)
            else if (type == 3 && info.getJSONObject(i).has("cover")) {
                printSearch(info, galery, i)
            } else
                printGalery(info, galery, i)
            i += 1
        }

        Thread {
            activity!!.runOnUiThread {
                imageView.adapter = MyAdaptater(context!!, galery)
            }
        }.start()
    }

    fun getInfosFromGalery(returnView: View, url: String, type: Int) {
        val myRequest = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer ".plus(arguments?.getString("access_token")))
            .build()
        OkHttpClient().newCall(myRequest).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                if (type != 4)
                    printImage(response?.body()?.string(), returnView, type)
                else
                    printAccountInfos(response?.body()?.string())
            }
            override fun onFailure(call: Call?, e: IOException?) {
                println("Fail")
            }
        })
    }
}