package com.example.facialrecognitionweemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_face_image.*

class FaceImageActivity : AppCompatActivity() {

    private lateinit var mCamera: Camera
    private lateinit var mPreview: CameraPreview
    private lateinit var mPicture: Camera.PictureCallback

    private var cameraFront = false
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_image)

        mCamera = Camera.open(1)
        mCamera.setDisplayOrientation(90)
        mPreview = CameraPreview(applicationContext, mCamera)
        mPicture = getPictureCallback()!!
        cameraPreview.addView(mPreview)

        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
            return
        }

        btSave.setOnClickListener {
            mCamera.takePicture(null, null, mPicture)
        }

    }


    /** Helper to ask camera permission.  */
    object CameraPermissionHelper {
        private const val CAMERA_PERMISSION_CODE = 0
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

        /** Check to see we have the necessary permissions for this app.  */
        fun hasCameraPermission(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
        }

        /** Check to see we have the necessary permissions for this app, and ask for them if we don't.  */
        fun requestCameraPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(CAMERA_PERMISSION), CAMERA_PERMISSION_CODE)
        }

        /** Check to see if we need to show the rationale for this permission.  */
        fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION)
        }

        /** Launch Application Setting to grant permission.  */
        fun launchPermissionSettings(activity: Activity) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", activity.packageName, null)
            activity.startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }

        recreate()
    }

    private fun getPictureCallback(): PictureCallback? {
        return PictureCallback { data, camera ->
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val intent = Intent(this, PictureActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()

        if (mCamera == null) {
            mCamera = Camera.open(1)
            mCamera.setDisplayOrientation(90)
            mPicture = getPictureCallback()!!
            mPreview.refreshCamera(mCamera)
            Log.d("nu", "null")
        } else {
            Log.d("nu", "no null")
        }
    }

}
