package com.dicoding.mystoryapp.view.upload

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivityUploadStoryBinding
import com.dicoding.mystoryapp.getImageUri
import com.dicoding.mystoryapp.reduceFileImage
import com.dicoding.mystoryapp.uriToFile
import com.dicoding.mystoryapp.view.ViewModelFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private val viewModel by viewModels<UploadStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private var currentImageUri: Uri? = null
    private var isSendLocation = false
    private var lat: Double? = null
    private var lon: Double? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMyLocation()
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCamera() }
        binding.addLocation.setOnCheckedChangeListener { _, isChecked ->
            isSendLocation = isChecked
        }
        binding.buttonUpload.setOnClickListener {
            showLoading(true)
            uploadImage()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
        if (isGranted){
            getMyLocation()
            Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherCamera.launch(currentImageUri)
    }

    private fun uploadImage(){
        showLoading(true)
        currentImageUri?.let {uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edtDescription.text.toString()
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImage = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImage
            )
            if (!isSendLocation) {
                lat = null
                lon = null
            }
            viewModel.uploadImage(multipartBody, desc, lat, lon).observe(this@UploadStoryActivity) {
                if (it.error){
                    Toast.makeText(this@UploadStoryActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@UploadStoryActivity, "Upload berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } ?: Toast.makeText(this, getString(R.string.empty_image_warning), Toast.LENGTH_SHORT).show()

        showLoading(false)
    }

    private fun showImage(){
        binding.ivPreview.setImageURI(currentImageUri)
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                lat = it.latitude
                lon = it.longitude
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressIndicator.visibility = View.VISIBLE
            binding.dimBackgroundView.visibility = View.VISIBLE
            binding.loadingProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.progressIndicator.visibility = View.GONE
            binding.dimBackgroundView.visibility = View.GONE
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null){
            currentImageUri = it
            showImage()
        }
    }

    private val launcherCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            showImage()
        }
    }
    companion object {
        const val TAG = "UploadStoryActivity"
    }
}