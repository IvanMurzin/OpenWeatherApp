package com.pac.speedweather.tools

import com.google.gson.Gson
import com.pac.speedweather.objects.current.CurrentWeather
import com.pac.speedweather.objects.hourly.Hourly

object Requester {
    private const val API_KEY = "d722644d54ea5bb25754c13fbd59f541"
    fun requestCurrentWeather(lat:Double?,lon:Double?): CurrentWeather? {
        val response =
            khttp.get("https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&exclude=hourly,daily&appid=$API_KEY")
        val jsonString = response.jsonObject.toString()
        return Gson().fromJson(jsonString, CurrentWeather::class.java)
    }

}