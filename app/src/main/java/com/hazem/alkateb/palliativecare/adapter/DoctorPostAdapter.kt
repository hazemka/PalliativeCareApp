package com.hazem.alkateb.palliativecare.adapter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.PostItemBinding
import com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details.ShowTopicDetailsFragment
import com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details.ShowTopicDetailsViewModel
import com.hazem.alkateb.palliativecare.model.Post
import com.rajat.pdfviewer.PdfViewerActivity
import com.squareup.picasso.Picasso

class DoctorPostAdapter(var context: Context, var data: ArrayList<Post>
,private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<DoctorPostAdapter.DoctorPostViewHolder>() {

    val loadingDialog = LottieProgressDialog(context, false, 150, 200, null,
        null, LottieProgressDialog.SAMPLE_6, "جار الحذف..", View.VISIBLE)

    class DoctorPostViewHolder(var binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorPostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return DoctorPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorPostViewHolder, position: Int) {
        holder.binding.txtNoComments.text = data[position].numOfComments.toString()
        holder.binding.txtNoLikes.text = data[position].numOfLikes.toString()
        holder.binding.txtPostDescription.text = data[position].description
        when(data[position].postType){
            "image" -> {
                holder.binding.postImage.visibility = View.VISIBLE
                Picasso.get().load(data[position].mediaUrl).placeholder(R.drawable.placeholder_image).into(holder.binding.postImage)
            }
            "video" -> {
                val player:ExoPlayer = ExoPlayer.Builder(context).build()
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
                    context.startActivity(PdfViewerActivity.launchPdfFromUrl(context,
                            data[position].mediaUrl, "معاينة", "",
                            enableDownload = false))
                }
            }
        }

        holder.binding.btnShowMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, holder.binding.btnShowMenu)
            popupMenu.inflate(R.menu.post_menu)
            popupMenu.setOnMenuItemClickListener {menuItem ->
                when(menuItem.itemId){
                    R.id.deletePost -> {
                        try {
                            deletePost(position)
                        }catch (e: Exception) {
                            if (loadingDialog.isShowing) loadingDialog.dismiss()
                            Toast.makeText(context, "حدث خطأ ما", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

        holder.binding.btnComments1.setOnClickListener {
            val sharedPreferences = context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
            val b = Bundle()
            b.putString("topicId",ShowTopicDetailsFragment.topicId)
            b.putString("postId",data[position].id)
            b.putString("userId",sharedPreferences.getString("userId",""))
            holder.binding.root.findNavController().navigate(R.id.action_showTopicDetailsFragment_to_showCommentsFragment,b)
        }

        holder.binding.btnComments2.setOnClickListener {
            val sharedPreferences = context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
            val b = Bundle()
            b.putString("topicId",ShowTopicDetailsFragment.topicId)
            b.putString("postId",data[position].id)
            b.putString("userId",sharedPreferences.getString("userId",""))
            holder.binding.root.findNavController().navigate(R.id.action_showTopicDetailsFragment_to_showCommentsFragment,b)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun deletePost(position: Int){
        loadingDialog.show()
        if (ShowTopicDetailsFragment.topicId != "")
            ShowTopicDetailsViewModel.deletePost(ShowTopicDetailsFragment.topicId,data[position].id,
                data[position].mediaUrl,context as Activity)
        ShowTopicDetailsViewModel.isPostDeleted.observe(lifecycleOwner){
            if (it){
                if (loadingDialog.isShowing) loadingDialog.dismiss()
                if (position < data.size){
                    data.removeAt(position)
                    notifyItemRemoved(position)
                }
            }else{
                Toast.makeText(context, "حدث خطأ ما", Toast.LENGTH_SHORT).show()
            }
        }
    }
}