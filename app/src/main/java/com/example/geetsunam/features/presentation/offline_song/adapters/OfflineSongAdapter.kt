package com.example.geetsunam.features.presentation.offline_song.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geetsunam.databinding.OfflineSongCardBinding
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongEvent
import com.example.geetsunam.features.presentation.offline_song.viewmodel.OfflineSongViewModel
import com.example.geetsunam.utils.CustomDialog
import com.example.geetsunam.utils.SongDiffUtil
import com.example.geetsunam.utils.models.Song
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class OfflineSongAdapter(
    private val context: Context, private val offlineSongViewModel: OfflineSongViewModel
) : RecyclerView.Adapter<OfflineSongAdapter.MyViewHolder>() {

    private var songs = emptyList<Song>()

    class MyViewHolder(private val binding: OfflineSongCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Song, context: Context, vm: OfflineSongViewModel) {
            binding.result = result
            binding.from = "offline"
            binding.executePendingBindings()
            //setting listener for cross button
            binding.ibDelete.setOnClickListener {
                CustomDialog().showSureDeleteDialog(context) {
                    // Call from the viewmodel to delete the selected song
                    vm.onEvent(OfflineSongEvent.DeleteOfflineSong(result.id!!, result.filePath!!))
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OfflineSongCardBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = songs[position]
        holder.bind(currentResult, context, offlineSongViewModel)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun setData(newData: List<Song>) {
        val songDiffUtil = SongDiffUtil(songs, newData)
        val result = DiffUtil.calculateDiff(songDiffUtil)
        songs = newData
        result.dispatchUpdatesTo(this)
    }
}