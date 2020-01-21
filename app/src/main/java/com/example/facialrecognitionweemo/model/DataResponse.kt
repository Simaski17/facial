package com.example.facialrecognitionweemo.model

data class DataResponse (val err: Boolean = false, val code: Int, val message: String, val data: List<Any>)