package dev.anshshukla.splitty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageViewModel: ViewModel() {
    private val mutableSelectedPageId = MutableLiveData<Int>()
    val selectedPageId: LiveData<Int> get() = mutableSelectedPageId

    fun setPageId(pageId: Int) {
        mutableSelectedPageId.value = pageId
    }
}