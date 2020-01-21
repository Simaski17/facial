package com.example.facialrecognitionweemo.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.facialrecognitionweemo.R
import com.example.facialrecognitionweemo.model.Data
import com.example.facialrecognitionweemo.model.DataResponse
import com.example.facialrecognitionweemo.model.DataState
import com.example.facialrecognitionweemo.model.RequestDataUsers
import com.example.facialrecognitionweemo.utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel by lazy { getViewModel { component.mainViewModel } }
    private lateinit var component: MainActivityComponent
    private val cal: Calendar = Calendar.getInstance()
    private var optionGender = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component = app.component.plus(MainActivityModule())



        viewModel.model.observe(this, Observer(::updateUi))

        btAccept.setOnClickListener(this)
        etDate.setOnClickListener(this)


//            val intent = Intent(this, FaceImageActivity::class.java)
//            startActivity(intent)


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
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION =
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val READ_EXTERNAL_STORAGE_PERMISSION =
            Manifest.permission.READ_EXTERNAL_STORAGE

        /** Check to see we have the necessary permissions for this app.  */
        fun hasCameraPermission(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(
                activity,
                CAMERA_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun hasWriteExternalStorage(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(
                activity,
                WRITE_EXTERNAL_STORAGE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun hasReadExternalStorage(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(
                activity,
                READ_EXTERNAL_STORAGE_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        }

        /** Check to see we have the necessary permissions for this app, and ask for them if we don't.  */
        fun requestCameraPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(CAMERA_PERMISSION), CAMERA_PERMISSION_CODE
            )
        }

        fun requestWriteExternalPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION), WRITE_EXTERNAL_STORAGE_CODE
            )
        }

        fun requestReadExternallPermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION), READ_EXTERNAL_STORAGE_CODE
            )
        }

        /** Check to see if we need to show the rationale for this permission.  */
        fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION)
        }

        fun shouldShowRequestPermissionRationaleWrite(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }

        fun shouldShowRequestPermissionRationaleRead(activity: Activity): Boolean {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                READ_EXTERNAL_STORAGE_PERMISSION
            )
        }

        /** Launch Application Setting to grant permission.  */
        fun launchPermissionSettings(activity: Activity) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", activity.packageName, null)
            activity.startActivity(intent)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(
                this,
                "Camera permission is needed to run this application",
                Toast.LENGTH_LONG
            )
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btAccept -> {

                if (etName.text.isNullOrEmpty()) {
                    Snackbar.make(v, "Debe ingresar un Nombre", Snackbar.LENGTH_SHORT).show()
                } else if (etRut.text.isNullOrEmpty()) {
                    Snackbar.make(v, "Debe ingresar un Rut", Snackbar.LENGTH_SHORT).show()
                } else if (etDate.text.isNullOrEmpty()) {
                    Snackbar.make(v, "Debe ingresar una Fecha de Nacimiento", Snackbar.LENGTH_SHORT)
                        .show()
                } else if (!rbFemale.isChecked && !rbMale.isChecked) {
                    Snackbar.make(v, "Debe seleccionar un genero", Snackbar.LENGTH_SHORT).show()
                } else {
                    optionGender = if (rbFemale.isChecked) {
                        "F"
                    } else {
                        "M"
                    }

                    val requestDataUsers = RequestDataUsers(
                        etName.text.toString(),
                        optionGender,
                        etDate.text.toString(),
                        etRut.text.toString()
                    )
                    viewModel.getValidUsers(requestDataUsers)
                    etDate.setText("")
                    etRut.setText("")
                    etName.setText("")
                }


            }
            R.id.etDate -> {


                val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker, year: Int, monthOfYear: Int,
                        dayOfMonth: Int
                    ) {
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateDateInView()
                    }
                }

                DatePickerDialog(
                    this@MainActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
                Log.e("VISIBLE", "VISIBLE")
            }
        }
    }

    private fun updateUi(event: Data<DataResponse>?) {

        event.with {
            when (dataState) {
                DataState.LOADING -> {

                }
                DataState.SUCCESS -> {
                    Log.e("VISIBLE", "VISIBLE")
                }
                DataState.ERROR -> {
                }
            }

            data.notNull { DataResponse -> {

            }

            }

            exception.notNull {

            }

        }

    }

    private fun updateDateInView() {
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        etDate.setText(sdf.format(cal.getTime()).toString())
    }

}
