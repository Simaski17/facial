package com.example.facialrecognitionweemo.services

import com.example.facialrecognitionweemo.model.DataResponse
import com.example.facialrecognitionweemo.model.RequestDataUsers
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PUT

interface FacialApi {

    @PUT("Usuarios/validaUsuarioEnrolado")
    fun getValidUsers(@HeaderMap headers: Map<String, String>, @Body requestDataUsers: RequestDataUsers): Call<DataResponse>

}