package com.sdmadsen.steve.lawntracker

enum class Direction(val text: String) {
    HORIZONTAL("Horizontal"),
    VERTICAL("Vertical"),
    DIAGONAL_LEFT("Diagonal Left"),
    DIAGONAL_RIGHT("Diagonal Right");

    companion object {
        fun getDirectionByText(text: String) = values().first { it.text == text }
    }

    override fun toString(): String {
        return text
    }
}