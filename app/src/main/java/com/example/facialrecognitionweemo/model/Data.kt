package com.example.facialrecognitionweemo.model

data class Data<out T>(val dataState: DataState,
                       val data: T? = null,
                       val exception: Throwable? = null)