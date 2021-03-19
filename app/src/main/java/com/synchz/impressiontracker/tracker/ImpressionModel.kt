package com.synchz.impressiontracker.tracker

data class ImpressionModel(
    var impressionDuration:Long,
    var imageUrl: String,
    var visiblePercentage: Double
){
    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other is ImpressionModel){
            return (other.imageUrl == imageUrl && impressionDuration == other.impressionDuration && visiblePercentage == visiblePercentage)
        }
        return false
    }
}