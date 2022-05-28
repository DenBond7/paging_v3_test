package com.denbond7.myapplication.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.denbond7.myapplication.model.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainViewModel(application: Application) : AndroidViewModel(application) {
    @ExperimentalCoroutinesApi
    val pagerFlow = UserRepository.getUsersPager(application).flow.cachedIn(viewModelScope)
}
