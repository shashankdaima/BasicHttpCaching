package com.finals.foodrunner.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData

operator  fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values:List<T>){
    val value=this.value?: arrayListOf()
    value.addAll(values)
    this.value=value
}
val <T> T.exhaustive: T
    get() = this

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}