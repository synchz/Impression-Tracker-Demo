package com.synchz.impressiontracker.tracker

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ImpressionTracker {

    private val minimumVisibleHeightThreshold = 50.0

    private val requiredDuration = 2

    private var startTime: Long = 0

    private var endTime: Long = 0

    private var hasTrackingStarted = false

    private val viewsBeingTracked: ArrayList<Pair<String, Double>> = ArrayList()

    private val impressions: ArrayList<ImpressionModel> = ArrayList()

    private var recyclerView: RecyclerView? = null

    fun getSavedImpressions(): ArrayList<ImpressionModel> {
        return impressions
    }

    fun startTracking(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.viewTreeObserver
            .addOnGlobalLayoutListener {
                if (!hasTrackingStarted) {
                    startTime = System.currentTimeMillis()
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager)
                            .findFirstVisibleItemPosition()
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    analyzeAndAddViewData(
                        firstVisibleItemPosition,
                        lastVisibleItemPosition
                    )
                    hasTrackingStarted = true
                }
            }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    endTime = System.currentTimeMillis()
                    for (trackedViewsCount in 0 until viewsBeingTracked.size) {
                        var duration = (endTime - startTime) / 1000
                        if (duration >= requiredDuration)
                            addImpressionToList(duration, trackedViewsCount)
                    }
                    viewsBeingTracked.clear()
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    startTime = System.currentTimeMillis()
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)?.let {
                            it.findFirstVisibleItemPosition()
                        }
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)?.let {
                            it.findLastVisibleItemPosition()
                        }
                    analyzeAndAddViewData(
                        firstVisibleItemPosition ?: 0,
                        lastVisibleItemPosition ?: 0
                    )
                }
            }
        })
    }

    fun stopTracking() {
        endTime = System.currentTimeMillis()
        val firstVisibleItemPosition =
            (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItemPosition =
            (recyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        analyzeAndAddViewData(
            firstVisibleItemPosition,
            lastVisibleItemPosition
        )
        for (trackedViewsCount in 0 until viewsBeingTracked.size) {
            var duration = (endTime - startTime) / 1000
            if (duration >= requiredDuration)
                addImpressionToList(duration, trackedViewsCount)
        }
        viewsBeingTracked.clear()
    }

    private fun addImpressionToList(duration: Long, trackedViewsCount: Int) {
        var model = ImpressionModel(
            duration,
            viewsBeingTracked[trackedViewsCount].first,
            viewsBeingTracked[trackedViewsCount].second
        )
        if(!impressions.contains(model))
        impressions.add(model)
    }

    private fun analyzeAndAddViewData(
        firstVisibleItemPosition: Int,
        lastVisibleItemPosition: Int
    ) {
        for (viewPosition in firstVisibleItemPosition..lastVisibleItemPosition) {
            recyclerView?.layoutManager?.let {
                addViewToTrackedIfVisible(it.findViewByPosition(viewPosition), viewPosition)
            }


        }
    }

    private fun addViewToTrackedIfVisible(itemView: View?, viewPosition: Int) {
        itemView?.let {
            val viewVisiblePercentage = getViewsVisibility(itemView)
            if (viewVisiblePercentage >= minimumVisibleHeightThreshold) {
                viewsBeingTracked.add(Pair(it.tag.toString(), viewVisiblePercentage))
            }
        }
    }

    private fun getViewsVisibility(view: View): Double {
        val itemRect = Rect()
        view.getLocalVisibleRect(itemRect)
        val visibleHeight: Double = itemRect.height().toDouble()
        val height: Double = view.measuredHeight.toDouble()
        return visibleHeight / height * 100.0
    }
}