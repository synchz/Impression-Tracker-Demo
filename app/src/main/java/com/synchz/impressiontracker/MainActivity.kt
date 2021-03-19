package com.synchz.impressiontracker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.synchz.impressiontracker.adapter.ImpressionAdapter
import com.synchz.impressiontracker.tracker.ImpressionTracker
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val tracker = ImpressionTracker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        populateData()
        initListeners()
    }

    private fun initListeners() {
        button.setOnClickListener {
            when (holder.visibility) {
                View.VISIBLE -> hideHolder()
                else -> showHolder()
            }
        }
    }

    private fun populateData() {
        val list = ArrayList<String>().apply {
            add("https://miro.medium.com/max/5000/1*1BUIofZgqVuR6nj8LbrRtQ.jpeg")
            add("https://www.freecodecamp.org/news/content/images/2020/11/Untitled-design--1-.png")
            add("https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=2000&fit=max&ixid=eyJhcHBfaWQiOjExNzczfQ")
            add("https://images.immediate.co.uk/production/volatile/sites/4/2009/07/GettyImages-767981211-e6967b1.jpg?webp=true&quality=45&resize=1880%2C799")
            add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2Gf1mwLixrzUsBHF7DSMbNu_0cpvfFnPDHA&usqp=CAU")
            add("https://inteng-storage.s3.amazonaws.com/images/JULY/sizes/cyber-security-3374252_640_resize_md.jpg")
        }

        recyclerView.adapter = ImpressionAdapter(list)
    }

    private fun showHolder(){
        holder.visibility = View.VISIBLE
        button.text = getString(R.string.hide_impressions)
        tracker.stopTracking()
        var sb = StringBuilder()
        tracker.getSavedImpressions().forEach {
            Log.e("PK", it.toString())
            sb.append(it.toString())
            sb.append("\n\n")
        }
        textView.text = sb.toString()
    }

    private fun hideHolder(){
        button.text = getString(R.string.show_impressions)
        holder.visibility = View.GONE
        tracker.startTracking(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        tracker.startTracking(recyclerView)
    }

    override fun onPause() {
        super.onPause()

        tracker.stopTracking()
        tracker.getSavedImpressions().forEach {
            Log.e("PK", it.toString())
        }
    }
}