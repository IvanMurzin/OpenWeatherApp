package com.pac.speedweather.objects.hourly

data class Hourly(
    val city: City?,
    val cnt: Int?,
    val cod: String?,
    val list: List<What>?,
    val message: Int?
)