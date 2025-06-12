package com.example.musictoy.ui.musicList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musictoy.R
import com.example.musictoy.data.local.Track
import com.example.musictoy.data.repository.TrackRepository
import com.example.musictoy.databinding.ActivityMusicListBinding
import com.example.musictoy.ui.TrackViewModel
import com.example.musictoy.ui.player.MusicPlayerActivity

class MusicListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusicListBinding
    private lateinit var adapter: MusicAdapter
    private val viewModel: TrackViewModel by viewModels()
    private var isFilteringLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MusicAdapter(
            emptyList(),
            onTrackClick = { _, position ->
                val intent = Intent(this, MusicPlayerActivity::class.java).apply {
                    putExtra("track_index", position)
                }
                startActivity(intent)
            },
            onLikeClick = { track, _ ->
                viewModel.toggleLike(track)
            }
        )

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = adapter

        viewModel.trackList.observe(this) { list ->
            val displayList = if (isFilteringLiked) {
                list.filter { it.isLiked }
            } else {
                list
            }
            adapter.updateList(displayList)
        }

        binding.btnLiked.setOnClickListener {
            isFilteringLiked = !isFilteringLiked
            binding.btnLiked.setImageResource(
                if (isFilteringLiked) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            )
            viewModel.trackList.value?.let { trackList ->
                val displayList = if (isFilteringLiked) {
                    trackList.filter { it.isLiked }
                } else {
                    trackList
                }
                adapter.updateList(displayList)
            }
        }

        viewModel.addTracks(
            listOf(
                Track(
                    "1",
                    "Aeroplane",
                    "Red Hot Chili Peppers",
                    "https://m.media-amazon.com/images/I/71st5w-CYfL._UF1000,1000_QL80_.jpg",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                ),
                Track(
                    "2",
                    "Cut the Bridge",
                    "Linkin Park",
                    "https://i1.sndcdn.com/artworks-kILMyOpdKXlP-0-t500x500.jpg",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
                ),
                Track(
                    "3",
                    "MAMMAMIA",
                    "Måneskin",
                    "https://upload.wikimedia.org/wikipedia/en/e/e5/Måneskin_Mammamia.jpg",
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
                )
            )

        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.trackList.value?.let { adapter.updateList(it) }
    }
}
