package com.pac.speedweather.objects.current

data class CurrentWeather(
    val current: Current?,
    val lat: Double?,
    val lon: Double?,
    val minutely: List<Minutely>?,
    val timezone: String?,
    val timezone_offset: Int?
)