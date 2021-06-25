package com.finals.foodrunner.util

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import com.finals.foodrunner.R

const val FILE_NAME="com.finals.foodrunner"

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
enum class SORT_SCHEME {
    SORT_BY_RATING, SORT_BY_INC_COST, SORT_BY_DES_COST
}

const val PREF_NAME= R.string.preference_file_name.toString()
const val USER_ID_KEY="user_id"
const val USER_NAME_KEY="name"
const val USER_MOBILE_NUMBER_KEY="mobile_number"
const val USER_ADDRESS_KEY="address"
const val USER_EMAIL_KEY="email"
const val USER_LOGGEDIN_KEY="isLogged"


