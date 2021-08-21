package com.projectmalthorn.dishme.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.projectmalthorn.dishme.R
import com.projectmalthorn.dishme.databinding.ActivityAddUpdateDishBinding
import com.projectmalthorn.dishme.databinding.DialogCustomImageSelectionBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.ivCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object:MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()){
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            startForResultToLoadImage.launch(intent)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()
            dialog.dismiss()
        }

        binding.ivGallery.setOnClickListener{
            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,

            )
                .withListener(object:PermissionListener{
                    override fun onPermissionGranted(report: PermissionGrantedResponse?) {
                        report?.let{
                            val intent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            startForResultToLoadImage.launch(intent)
                        }
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        showRationalDialogForPermissions()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                }).onSameThread().check()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage(
            "It looks like you have disabled permissions required for this feature. It can be enable" +
                    "under Application Settings."
        )
            .setPositiveButton("GO TO SETTINGS")
            {_,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }

            }
            .setNegativeButton("Cancel"){dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private val startForResultToLoadImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val selectedImage: Uri? = result.data?.data
                    if (selectedImage != null){
//                        mBinding.ivDishImage.setImageURI(selectedImage)

                        Glide.with(this)
                            .load(selectedImage)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object: RequestListener<Drawable>{
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    Log.e("TAG", "ERROR LOADING IMAGE")
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    resource?.let{
                                        val bitmap: Bitmap = resource.toBitmap()
                                        mImagePath = saveImageToInternalStorage(bitmap)
                                        Log.i("Image Path", mImagePath)
                                    }
                                    return false
                                }

                            })
                            .into(mBinding.ivDishImage)

                        mBinding.ivAddDishImage.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.ic_vector_edit
                            )
                        )

                    }else{
                        // Get the bitmap directly from camera
                        result.data?.extras?.let {
                            val bitmap: Bitmap = result.data!!.extras!!.get("data") as Bitmap
//                            mBinding.ivDishImage.setImageBitmap(bitmap)

                            Glide.with(this)
                                .load(bitmap)
                                .centerCrop()
                                .into(mBinding.ivDishImage)

                            mImagePath = saveImageToInternalStorage(bitmap)
                            Log.i("ImagePath", mImagePath)

                            mBinding.ivAddDishImage.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_vector_edit
                                )
                            )
                        }
                    }
                } catch (error: Exception) {
                    Log.d("log==>>", "Error : ${error.localizedMessage}")
                }
            }
        }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String{
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    companion object{
        private const val IMAGE_DIRECTORY = "DishMeImages"
    }

}