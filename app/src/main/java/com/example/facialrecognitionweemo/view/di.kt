package com.example.facialrecognitionweemo.view

import com.example.facialrecognitionweemo.services.FacialApi
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule{

    @Provides
    fun mainViewModelProvider(facialApi: FacialApi) = MainViewModel(facialApi)


}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}