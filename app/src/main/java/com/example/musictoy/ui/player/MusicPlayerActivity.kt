package com.example.musictoy.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.musictoy.R
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding
    private var player: ExoPlayer? = null
    private lateinit var trackList: List<Track>
    private var currentIndex: Int = 0
    private lateinit var track: Track
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackList = intent.getParcelableArrayListExtra("track_list") ?: emptyList()
        currentIndex = intent.getIntExtra("track_index", 0)
        track = trackList[currentIndex]

        binding.tvTitle.text = track.title
        binding.tvArtist.text = track.artist
        Glide.with(this).load(track.imageUrl).into(binding.ivAlbumArt)

        initializePlayer()
        initPlayPauseButton()
        initSeekBar()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            val mediaItem = MediaItem.fromUri(track.audioUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                    } else {
                        binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                }
            })

            exoPlayer.playWhenReady = true
        }
        startSeekBarUpdate()
    }

    private fun initPlayPauseButton() {
        binding.btnPlay.setOnClickListener {
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                } else {
                    it.play()
                    binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }
        }
    }

    private fun initSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startSeekBarUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                player?.let {
                    if (it.duration > 0) {
                        binding.seekBar.max = it.duration.toInt()
                        binding.seekBar.progress = it.currentPosition.toInt()
                    }
                }
                handler.postDelayed(this, 500)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        handler.removeCallbacksAndMessages(null)
        player?.release()
        player = null
    }
}