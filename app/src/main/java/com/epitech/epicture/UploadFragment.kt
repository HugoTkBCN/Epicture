package com.epitech.epicture

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_upload_fragment.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS", "DEPRECATION")
class UploadFragment : Fragment() {
    private val REQUEST_CODE = 101
    private var image : Intent? = null

    companion object {
        fun createInstance(accessToken: String): UploadFragment {
            val args = Bundle()
            val fragment = UploadFragment()
            args.putString("access_token", accessToken)
            fragment.arguments = args
            return (fragment)
        }
    }

    private fun restartButton() {
        uploadButton.setImageResource(R.drawable.ic_baseline_image_24)
        uploadButton.setBackgroundColor(resources.getColor(R.color.blackLight))
        title_upload_image.setText("")
        description_upload_image.setText("")


    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
    }

    private fun encodeImage(imageBitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        return (Base64.getEncoder().encodeToString(byteArray))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val returnView = inflater.inflate(R.layout.activity_upload_fragment, container, false)
        val mButton = returnView.findViewById(R.id.uploadButton) as android.support.v7.widget.AppCompatImageButton
        try {
            mButton.setOnClickListener {
                askForPermissions()
                openGalleryForImage()
                uploadImgur.setOnClickListener {
                    val imageBase64 = image?.data?.let { it1 -> uriToBitmap(it1) }?.let { it1 -> encodeImage(it1) }
                    val requestBody =  MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", title_upload_image.text.toString())
                        .addFormDataPart("description", description_upload_image.text.toString())
                        .addFormDataPart("image", imageBase64)
                        .build()
                    val myRequest = Request.Builder()
                        .header("Authorization", "Bearer ".plus(arguments?.getString("access_token")))
                        .url("https://api.imgur.com/3/image/")
                        .post(requestBody)
                        .build()
                    OkHttpClient().newCall(myRequest).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            println("Uploaded")
                        }
                        override fun onFailure(call: Call?, e: IOException?) {
                            println("Fail")
                        }
                    })
                    restartButton()
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return (returnView)
    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(context as Context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, Manifest.permission.READ_EXTERNAL_STORAGE))
                showPermissionDeniedDialog()
            else
                ActivityCompat.requestPermissions(activity as Activity,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
            return (false)
        }
        return (true)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                     askForPermissions()
                }
                return
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings"
            ) { _, _ ->
                // send to app settings if permission is denied permanently
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts(
                    "package",
                    PACKAGE_NAME, null
                )
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel",null)
            .show()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            uploadButton.setImageURI(data?.data) // handle chosen image
            image = data
        }
    }


}
