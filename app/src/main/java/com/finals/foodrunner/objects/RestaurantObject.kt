package com.finals.foodrunner.objects

data class RestaurantObject (
    val id:Int,
    val name:String,
    val cost:Int,
    val rating:Number,
    val isFavourite:Boolean,
    val photo:String?=null

)