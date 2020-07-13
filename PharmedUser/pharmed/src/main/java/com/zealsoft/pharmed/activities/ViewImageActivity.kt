package com.zealsoft.pharmed.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Utills
import java.util.*

class ViewImageActivity : AppCompatActivity() {

    private lateinit var close: ImageView
    private lateinit var zoomableImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private var imageUrl: String? = null
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        setViews()
    }

    private fun setViews() {

        Utills.transparentToolbar(this, true)
        Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)

        close = findViewById(R.id.close)
        zoomableImageView = findViewById(R.id.zoomable_image_view)
        zoomableImageView.setOnTouchListener(ImageMatrixTouchHandler(applicationContext))
        progressBar = findViewById(R.id.progress)
        progressBar.max = 100

        imageUrl = intent.getStringExtra("imageUrl")
        type = intent.getStringExtra("type")
        var imagePlaceHolder = R.drawable.icon_pharmacy_general
        var showLoading = false

        when (type) {
            Constants.TYPE_USER -> {
                imagePlaceHolder = R.drawable.icon_user_general
                imageUrl = Utills.getCompleteUrl(imageUrl)
            }
            Constants.TYPE_PHARMACY -> {
                imagePlaceHolder = R.drawable.icon_pharmacy_general
                imageUrl = Utills.getCompleteUrl(imageUrl)
            }
            Constants.PRESCRIPTION_IMAGE ->
                imagePlaceHolder = R.drawable.icon_prescription

            Constants.IMAGE_PATH_PRESCRIPTION -> {
                imagePlaceHolder = R.drawable.icon_prescription
                imageUrl = Utills.getCompleteUrl(imageUrl)
                showLoading = true
            }
            Constants.IMAGE_LICENSE ->
                imagePlaceHolder = R.drawable.icon_license

            Constants.IMAGE_PATH_LICENSE -> {
                imagePlaceHolder = R.drawable.icon_license
                imageUrl = Utills.getCompleteUrl(imageUrl)
                showLoading = true
            }

            Constants.IMAGE_PATH_PROMOTION -> {
                imagePlaceHolder = R.drawable.icon_license
                imageUrl = Utills.getCompleteUrl(imageUrl)
                showLoading = true
            }

//        Glide
//                .with(this)
//                .load(imageUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .placeholder(imagePlaceHolder)
//                .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
//                .error(imagePlaceHolder)
//                .priority(Priority.HIGH)
//                .into(zoomableImageView)
        }

        val options = RequestOptions()
                .placeholder(imagePlaceHolder)
                .error(imagePlaceHolder)
                .priority(Priority.HIGH)
//
//        if(showLoading)
//            GlideImageLoader(zoomableImageView, progressBar).load(Utills.getCompleteUrl(imageUrl),options)
//        else {
            Glide
                    .with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(imagePlaceHolder)
                    .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                    .error(imagePlaceHolder)
                    .priority(Priority.HIGH)
                    .into(zoomableImageView)
//        }

        close.setOnClickListener { finish() }
    }
}