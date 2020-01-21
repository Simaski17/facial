package com.example.facialrecognitionweemo

import android.app.Application
import com.example.facialrecognitionweemo.di.DaggerFacialComponent
import com.example.facialrecognitionweemo.di.FacialComponent

class FacialApp: Application() {

    lateinit var component: FacialComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerFacialComponent.factory().create(this)
    }

}