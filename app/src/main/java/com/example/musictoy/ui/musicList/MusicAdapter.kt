package com.example.musictoy.ui.musicList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musictoy.R
import com.example.musictoy.data.local.Track
import com.example.musictoy.databinding.ItemTrackBinding

class MusicAdapter(
    private var trackList: List<Track>,
    private val onTrackClick: (Track, Int) -> Unit,
    private val onLikeClick: (Track, Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    fun updateList(newTracks: List<Track>) {
        this.trackList = newTracks
        notifyDataSetChanged()
    }

    inner class MusicViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track, position: Int) {
            binding.tvTitle.text = track.title
            binding.tvArtist.text = track.artist
            Glide.with(binding.root.context).load(track.imageUrl).into(binding.ivAlbumArt)

            binding.root.setOnClickListener {
                onTrackClick(track, position)
            }

            val iconRes = if (track.isLiked) {
                R.drawable.ic_baseline_favorite_24
            } else {
                R.drawable.ic_baseline_favorite_border_24
            }
            binding.btnLike.setImageResource(iconRes)

            binding.btnLike.setOnClickListener {
                onLikeClick(track, position)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(trackList[position], position)
    }

    override fun getItemCount(): Int = trackList.size
}
