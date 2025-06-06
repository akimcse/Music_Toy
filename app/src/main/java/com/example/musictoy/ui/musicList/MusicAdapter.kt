package com.example.musictoy.ui.musiclist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ItemTrackBinding

class MusicAdapter(
    private val trackList: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.tvTitle.text = track.title
            binding.tvArtist.text = track.artist

            binding.root.setOnClickListener {
                onItemClick(track)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun getItemCount(): Int = trackList.size
}
