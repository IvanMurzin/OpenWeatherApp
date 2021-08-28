package com.pac.speedweather

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birjuvachhani.locus.Locus
import com.pac.speedweather.tools.Requester
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_current.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
    }

    private fun setupUi() {
        val pos = (0..4).random()
        recycler.layoutManager = LinearLayoutManager(this)
        startApplicationWeather(pos)
    }

    @SuppressLint("SetTextI18n")
    private fun startApplicationWeather(pos: Int) {
        Locus.startLocationUpdates(this) { result ->
            result.location?.let {
                doAsync {
                    val currentWeather =
                        Requester.requestCurrentWeather(it.latitude, it.longitude)
                    uiThread {
                        val mills = currentWeather?.current?.dt?.toLong()
                            ?: System.currentTimeMillis() / 1000
                        val date = Date(mills * 1000)
                        val dateFormat = SimpleDateFormat("hh:mm");
                        val time = dateFormat.format(date)
                        val icon =
                            getIcon(currentWeather?.current?.weather?.get(0)?.main.toString())
                        recycler.adapter = MyCurrentAdapter(icon, time, pos)
                        var f = true
                        var city1 = ""
                        var country1 = ""
                        for (i in currentWeather!!.timezone!!) {
                            if (i == '/' || i == '\\') f = false
                            else if (f) city1 += i
                            else country1 += i
                        }
                        this@MainActivity.city.text = city1
                        this@MainActivity.country.text = country1
                        this@MainActivity.temperature.text =
                            (currentWeather.current?.temp!! - 273).toInt().toString() + "°"
                    }
                }
            }
            result.error?.let {
                recycler.adapter = MyCurrentAdapter(getIcon(""), "???", pos)
                startApplicationWeather(pos)
            }
        }
    }

    class MyCurrentAdapter(
        private val icon: Drawable?,
        private val time: String,
        private val pos: Int
    ) :
        RecyclerView.Adapter<MyCurrentAdapter.MyHolder>() {


        class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.icon
            val time: TextView = view.time
            val today: TextView = view.today
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_current, parent, false)
            return MyHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            if (position == pos) {
                holder.icon.setImageDrawable(icon)
                holder.time.text = time
                holder.today.text = "Today"
            }
        }

        override fun getItemCount() = 4
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun getIcon(weather: String): Drawable? {
        return when (weather) {
            "Thunderstorm" -> {
                getDrawable(R.drawable.ic_thunder)
            }
            "Drizzle" -> {
                getDrawable(R.drawable.ic_clouds)
            }
            "Rain" -> {
                getDrawable(R.drawable.ic_rain)
            }
            "Snow" -> {
                getDrawable(R.drawable.ic_snow)
            }
            "Mist" -> {
                getDrawable(R.drawable.ic_clouds)

            }
            "Smoke" -> {
                getDrawable(R.drawable.ic_clouds)

            }
            "Clear" -> {
                getDrawable(R.drawable.ic_sun)

            }
            "Clouds" -> {
                getDrawable(R.drawable.ic_clouds)

            }
            else -> {
                getDrawable(R.drawable.ic_moon)
            }
        }
    }
}