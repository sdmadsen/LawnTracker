package com.sdmadsen.steve.lawntracker

enum class Status(val text: String) {
    PENDING("Not Yet Started"),
    STARTED("Started"),
    PAUSED("Paused"),
    COMPLETED("Completed"),
    START("Start"),
    STOP("Stop");

    companion object {
        fun getStatusByText(text: String) = values().first { it.text == text }

        fun getMowStatusValues() = values().sliceArray(0..3)
    }

    override fun toString(): String {
        return text
    }
}