package com.example.facialrecognitionweemo.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facialrecognitionweemo.model.Data
import com.example.facialrecognitionweemo.model.DataResponse
import com.example.facialrecognitionweemo.model.RequestDataUsers
import com.example.facialrecognitionweemo.services.FacialApi
import com.example.facialrecognitionweemo.utils.postException
import com.example.facialrecognitionweemo.utils.postLoading
import com.example.facialrecognitionweemo.utils.postValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val facialApi: FacialApi) : ViewModel() {

    val model = MutableLiveData<Data<DataResponse>>()

    fun getValidUsers(requestDataUsers: RequestDataUsers){
        CoroutineScope(Dispatchers.Main).launch {

            model.postLoading()

            runCatching {

                withContext(Dispatchers.IO) {

                    var headers: HashMap<String, String> = hashMapOf()
                    headers.put("X-API-KEY", "e5bf2a80d74753d1c5b1c8c0f1078ec0")

                        facialApi.getValidUsers(headers, requestDataUsers).execute().body()

                }

            }.onSuccess { response ->

                if (response!!.code == 200 ) {
                    Log.e("SUCCESS", "SUCCESS ")
                    model.postValue(response)
                } else {
                    Log.e("ERROR", "ERROR ")
                    model.postException(Exception("${"Error"}: ${response!!.err.toString()}"))
                }


            }.onFailure { throwable ->
                model.postException(throwable)
            }

        }
    }



}