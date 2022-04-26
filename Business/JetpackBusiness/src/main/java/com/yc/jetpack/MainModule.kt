package com.yc.jetpack

import com.yc.jetpack.study.model.*
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import org.koin.android.viewmodel.dsl.viewModel


@OptIn(KoinApiExtension::class)
val mainModule = module {
    //single单例模式  注入方式 。single方法定义的对象在该scope中会缓存起来，多个地方依赖同一个对象
    single { SavedDataRepository() }

    //添加viewModel
    viewModel { HasParamsViewModel(get()) }
    //添加无参数viewModel
    viewModel { NoParamsViewModel() }
    //添加多个参数viewModel
    viewModel { parameters ->
        SavedDataViewModel2(correction = parameters.get() , repository = get())
    }
}