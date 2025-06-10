package com.example.musictoy.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val audioUrl: String,
    var isLiked: Boolean = false
) : Parcelable
