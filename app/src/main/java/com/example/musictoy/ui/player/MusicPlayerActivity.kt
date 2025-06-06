package com.example.musictoy.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Track 객체 받기
        val track = intent.getParcelableExtra<Track>("track")

        track?.let {
            binding.titleTextView.text = it.title
            binding.artistTextView.text = it.artist

            Glide.with(this)
                .load(it.imageUrl)
                .into(binding.albumImageView)
        }
    }
}
