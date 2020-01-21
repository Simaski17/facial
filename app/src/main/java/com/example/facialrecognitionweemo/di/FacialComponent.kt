package com.example.facialrecognitionweemo.di

import android.app.Application
import com.example.facialrecognitionweemo.view.MainActivityComponent
import com.example.facialrecognitionweemo.view.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface FacialComponent {


    fun plus(module: MainActivityModule): MainActivityComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): FacialComponent
    }

}