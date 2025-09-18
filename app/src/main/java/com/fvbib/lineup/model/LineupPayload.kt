package com.fvbib.lineup.model

data class LineupPayload(
    val v: Int = 1,
    val teamCode: String,
    val set: Int,
    val side: String,
    val players: List<String>
)
