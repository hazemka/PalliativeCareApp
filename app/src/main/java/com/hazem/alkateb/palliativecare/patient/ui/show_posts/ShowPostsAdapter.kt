package com.hazem.alkateb.palliativecare.patient.ui.show_posts

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.PostItemBinding
import com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details.ShowTopicDetailsFragment
import com.hazem.alkateb.palliativecare.model.Post
import com.rajat.pdfviewer.PdfViewerActivity
import com.squareup.picasso.Picasso

class ShowPostsAdapter(var context: Context, var data: ArrayList<Post>) :
    RecyclerView.Adapter<ShowPostsAdapter.ShowPostsViewHolder>() {

    class ShowPostsViewHolder(var binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowPostsViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ShowPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowPostsViewHolder, position: Int) {
        holder.binding.txtNoComments.text = data[position].numOfComments.toString()
        holder.binding.txtNoLikes.text = data[position].numOfLikes.toString()
        holder.binding.txtPostDescription.text = data[position].description

        when(data[position].postType){
            "image" -> {
                holder.binding.postImage.visibility = View.VISIBLE
                Picasso.get().load(data[position].mediaUrl).placeholder(R.drawable.placeholder_image).into(holder.binding.postImage)
            }
            "video" -> {
                val player: ExoPlayer = ExoPlayer.Builder(context).build()
                val dataSource = DefaultDataSourceFactory(context,"exoplayer-codelap")
                holder.binding.playerVideo.visibility = View.VISIBLE
                holder.binding.playerVideo.player = player
                val uri = Uri.parse(data[position].mediaUrl)
                val media = ProgressiveMediaSource.Factory(dataSource).createMediaSource(MediaItem.fromUri(uri))
                player.playWhenReady = false
                player.prepare(media,false,false)
            }
            "pdf" ->{
                holder.binding.pdfImage.visibility = View.VISIBLE
                holder.binding.pdfImage.setOnClickListener {
                    context.startActivity(
                        PdfViewerActivity.launchPdfFromUrl(context,
                        data[position].mediaUrl, "معاينة", "",
                        enableDownload = false))
                }
            }
        }

        holder.binding.btnLike1.setOnClickListener {
            ShowPostsViewModel.likePost(context,ShowPostsFragment.topic.doctorId
            ,ShowPostsFragment.topic.id,data[position].id)
            holder.binding.btnLike1.setImageResource(R.drawable.like_done)
            holder.binding.txtNoLikes.text = (holder.binding.txtNoLikes.text.toString().toInt()+1).toString()
        }

        holder.binding.btnLike2.setOnClickListener {
            ShowPostsViewModel.likePost(context,ShowPostsFragment.topic.doctorId
                ,ShowPostsFragment.topic.id,data[position].id)
            holder.binding.btnLike1.setImageResource(R.drawable.like_done)
            holder.binding.txtNoLikes.text = (holder.binding.txtNoLikes.text.toString().toInt()+1).toString()
        }

        holder.binding.btnComments1.setOnClickListener {
            val b = Bundle()
            b.putString("topicId", ShowPostsFragment.topic.id)
            b.putString("postId",data[position].id)
            b.putString("userId",ShowPostsFragment.topic.doctorId)
            holder.binding.root.findNavController().navigate(R.id.action_showPostsFragment_to_showCommentsFragment2,b)
        }

        holder.binding.btnComments2.setOnClickListener {
            val b = Bundle()
            b.putString("topicId", ShowPostsFragment.topic.id)
            b.putString("postId",data[position].id)
            b.putString("userId",ShowPostsFragment.topic.doctorId)
            holder.binding.root.findNavController().navigate(R.id.action_showPostsFragment_to_showCommentsFragment2,b)
        }

        holder.binding.btnShowMenu.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return data.size
    }
}