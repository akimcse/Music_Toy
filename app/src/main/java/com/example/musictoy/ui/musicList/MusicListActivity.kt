package com.example.musictoy.ui.musicList

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ActivityMusicListBinding

class MusicListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sampleTracks = listOf(
            Track("1", "Aeroplane", "Red Hot Chili Peppers", "https://example.com/image1.jpg", "https://example.com/audio1.mp3"),
            Track("2", "Cut the Bridge", "Linkin Park", "https://example.com/image2.jpg", "https://example.com/audio2.mp3"),
            Track("3", "MAMMAMIA", "Måneskin", "https://example.com/image3.jpg", "https://example.com/audio3.mp3")
        )

        val adapter = MusicAdapter(sampleTracks) { track ->
            Log.d("MusicList", "Clicked track: ${track.title} by ${track.artist}")
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = adapter
    }
}
