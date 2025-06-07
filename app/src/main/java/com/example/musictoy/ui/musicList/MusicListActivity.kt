package com.example.musictoy.ui.musicList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ActivityMusicListBinding
import com.example.musictoy.ui.player.MusicPlayerActivity

class MusicListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMusicListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sampleTracks = listOf(
            Track("1", "Aeroplane", "Red Hot Chili Peppers", "https://m.media-amazon.com/images/I/71st5w-CYfL._UF1000,1000_QL80_.jpg", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
            Track("2", "Cut the Bridge", "Linkin Park", "https://i1.sndcdn.com/artworks-kILMyOpdKXlP-0-t500x500.jpg", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"),
            Track("3", "MAMMAMIA", "Måneskin", "https://upload.wikimedia.org/wikipedia/en/e/e5/Måneskin_Mammamia.jpg", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3")
        )

        val adapter = MusicAdapter(sampleTracks) { track, position ->
            Log.d("MusicList", "Clicked track: ${track.title} by ${track.artist}")

            val intent = Intent(this@MusicListActivity, MusicPlayerActivity::class.java).apply {
                putParcelableArrayListExtra("track_list", ArrayList(sampleTracks))
                putExtra("track_index", position)
            }
            startActivity(intent)
        }

        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = adapter
    }
}
