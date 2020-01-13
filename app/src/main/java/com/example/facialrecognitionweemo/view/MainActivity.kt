package com.example.facialrecognitionweemo.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.facialrecognitionweemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btAccept.setOnClickListener {

            val intent = Intent(this,
                FaceImageActivity::class.java)
            startActivity(intent)

        }

        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            CameraPermissionHelper.requestCameraPermission(this)
            return
        }

        if (!CameraPermissionHelper.hasWriteExternalStorage(this)) {
            CameraPermissionHelper.requestWriteExternalPermission(this)
            return
        }

        if (!CameraPermissionHelper.hasReadExternalStorage(this)) {
            CameraPermissionHelper.requestReadExternallPermission(this)
            return
        }


    }

    /** Helper to ask camera permission.  */
    object CameraPermissionHelper {
        private const val CAMERA_PERMISSION_CODE = 0
        private const val WRITE_EXTERNAL_STORAGE_CODE = 1
        private const val READ_EXTERNAL_STORAGE_CODE = 2
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE

        /** Check to see we have the necessary permissions for this app.  */
        fun hasCameraPermission(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED
        }

        fun hasWriteExternalStorage(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
        }

        fun hasReadExternalStorage(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
        }

        /** Check to see we have the necessary permissions for this app, and ask for them if we don't.  */
        fun requestCameraPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(CAMERA_PERMISSION), CAMERA_PERMISSION_CODE)
        }

        fun requestWriteExternalPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION), WRITE_EXTERNAL_STORAGE_CODE)
        }

        fun requestReadExternallPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION), READ_EXTERNAL_STORAGE_CODE)
        }

        /** Check to see if we need to show the rationale for this permission.  */
        fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION)
        }

        fun shouldShowRequestPermissionRationaleWrite(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, WRITE_EXTERNAL_STORAGE_PERMISSION)
        }

        fun shouldShowRequestPermissionRationaleRead(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_EXTERNAL_STORAGE_PERMISSION)
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

            if (!CameraPermissionHelper.shouldShowRequestPermissionRationaleWrite(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }

            if (!CameraPermissionHelper.shouldShowRequestPermissionRationaleRead(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }

            finish()
        }

        recreate()
    }


}
