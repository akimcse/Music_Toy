package com.example.musictoy.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.musictoy.R
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ActivityMusicPlayerBinding
import com.example.musictoy.ui.TrackViewModel

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicPlayerBinding
    private val viewModel: TrackViewModel by viewModels()
    private var player: ExoPlayer? = null
    private lateinit var track: Track
    private var currentIndex: Int = 0
    private var playMode: PlayMode = PlayMode.REPEAT_ALL
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentIndex = intent.getIntExtra("track_index", 0)

        viewModel.trackList.observe(this) { list ->
            if (list.isNotEmpty()) {
                track = list[currentIndex]
                initializePlayer()
                updateTrackUI()
            }
        }

        binding.btnLiked.setOnClickListener {
            viewModel.toggleLike(track)
            updateLikeIcon()
        }

        binding.btnNext.setOnClickListener {
            val listSize = viewModel.trackList.value?.size ?: return@setOnClickListener
            val nextIndex = if (playMode == PlayMode.SHUFFLE) {
                getShuffledIndex(listSize)
            } else {
                (currentIndex + 1) % listSize
            }
            player?.seekTo(nextIndex, 0L)
        }

        binding.btnPrev.setOnClickListener {
            val listSize = viewModel.trackList.value?.size ?: return@setOnClickListener
            val prevIndex = if (playMode == PlayMode.SHUFFLE) {
                getShuffledIndex(listSize)
            } else {
                if (currentIndex - 1 < 0) listSize - 1 else currentIndex - 1
            }
            player?.seekTo(prevIndex, 0L)
        }

        binding.btnPlayMode.setOnClickListener {
            playMode = playMode.next()
            updatePlayModeIcon()
            applyPlayModeToPlayer()
        }

        initPlayPauseButton()
        initSeekBar()
        updatePlayModeIcon()
    }

    private fun initializePlayer() {
        val mediaItems = viewModel.trackList.value?.map { MediaItem.fromUri(it.audioUrl) } ?: return

        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer
            exoPlayer.setMediaItems(mediaItems, currentIndex, 0L)
            exoPlayer.prepare()
            applyPlayModeToPlayer()
            exoPlayer.play()

            exoPlayer.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    binding.btnPlay.setImageResource(
                        if (isPlaying) R.drawable.ic_baseline_pause_24
                        else R.drawable.ic_baseline_play_arrow_24
                    )
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentIndex = exoPlayer.currentMediaItemIndex
                    updateTrackUI()
                }
            })
        }

        startSeekBarUpdate()
    }

    private fun updateTrackUI() {
        track = viewModel.trackList.value?.getOrNull(currentIndex) ?: return
        binding.tvTitle.text = track.title
        binding.tvArtist.text = track.artist
        Glide.with(this).load(track.imageUrl).into(binding.ivAlbumArt)
        updateLikeIcon()
    }

    private fun updateLikeIcon() {
        val iconResId = if (viewModel.isLiked(track)) {
            R.drawable.ic_baseline_favorite_24
        } else {
            R.drawable.ic_baseline_favorite_border_24
        }
        binding.btnLiked.setImageResource(iconResId)
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

    private fun updatePlayModeIcon() {
        val resId = when (playMode) {
            PlayMode.REPEAT_ALL -> R.drawable.ic_baseline_repeat_24
            PlayMode.REPEAT_ONE -> R.drawable.ic_baseline_repeat_one_24
            PlayMode.SHUFFLE -> R.drawable.ic_baseline_shuffle_24
        }
        binding.btnPlayMode.setImageResource(resId)
    }

    private fun applyPlayModeToPlayer() {
        player?.let {
            when (playMode) {
                PlayMode.REPEAT_ALL -> {
                    it.repeatMode = Player.REPEAT_MODE_ALL
                    it.shuffleModeEnabled = false
                }
                PlayMode.REPEAT_ONE -> {
                    it.repeatMode = Player.REPEAT_MODE_ONE
                    it.shuffleModeEnabled = false
                }
                PlayMode.SHUFFLE -> {
                    it.repeatMode = Player.REPEAT_MODE_ALL
                    it.shuffleModeEnabled = true
                }
            }
        }
    }

    private fun getShuffledIndex(size: Int): Int {
        val indices = (0 until size).toMutableList().apply { remove(currentIndex) }
        return indices.random()
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

    enum class PlayMode {
        REPEAT_ALL,
        REPEAT_ONE,
        SHUFFLE;

        fun next(): PlayMode {
            return when (this) {
                REPEAT_ALL -> REPEAT_ONE
                REPEAT_ONE -> SHUFFLE
                SHUFFLE -> REPEAT_ALL
            }
        }
    }
}