package com.example.musictoy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musictoy.data.local.Track
import com.example.musictoy.data.repository.TrackRepository

class TrackViewModel : ViewModel() {
    private val _trackList = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>> get() = _trackList

    init {
        _trackList.value = TrackRepository.getTrackList()
    }

    fun toggleLike(track: Track) {
        TrackRepository.toggleLike(track)
        _trackList.value = TrackRepository.getTrackList()
    }

    fun addTracks(tracks: List<Track>) {
        TrackRepository.addAll(tracks)
        _trackList.value = TrackRepository.getTrackList()
    }

    fun getTrack(index: Int): Track? {
        return _trackList.value?.getOrNull(index)
    }

    fun getLikedTracks(): List<Track> {
        return TrackRepository.getLikedTracks()
    }

    fun isLiked(track: Track): Boolean {
        return TrackRepository.isLiked(track)
    }
}
