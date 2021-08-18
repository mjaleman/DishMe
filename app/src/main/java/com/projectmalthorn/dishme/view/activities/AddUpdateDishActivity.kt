package com.projectmalthorn.dishme.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
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

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateDishBinding

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
                        mBinding.ivDishImage.setImageURI(selectedImage)

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
                            mBinding.ivDishImage.setImageBitmap(bitmap)

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
//
//    companion object{
//        private const val CAMERA = 1
//        private const val GALLERY = 2
//    }

}