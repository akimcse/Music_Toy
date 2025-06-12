package com.example.musictoy.data.repository

import com.example.musictoy.data.local.Track

object TrackRepository {
    private val _trackList = mutableListOf<Track>()

    fun getTrackList(): List<Track> = _trackList

    fun addAll(tracks: List<Track>) {
        _trackList.clear()
        _trackList.addAll(tracks)
    }

    fun toggleLike(track: Track) {
        val index = _trackList.indexOfFirst { it.id == track.id }
        if (index != -1) {
            val updated = _trackList[index].copy(isLiked = !_trackList[index].isLiked)
            _trackList[index] = updated
        }
    }

    fun isLiked(track: Track): Boolean {
        return _trackList.find { it.id == track.id }?.isLiked ?: false
    }

    fun getLikedTracks(): List<Track> {
        return _trackList.filter { it.isLiked }
    }

    fun getTrackAt(index: Int): Track? {
        return _trackList.getOrNull(index)
    }
}
