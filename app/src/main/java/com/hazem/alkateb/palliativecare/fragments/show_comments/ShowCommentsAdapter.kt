package com.hazem.alkateb.palliativecare.fragments.show_comments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.databinding.CommentItemBinding
import com.hazem.alkateb.palliativecare.model.Comment
import com.squareup.picasso.Picasso

class ShowCommentsAdapter(var context: Context, var data: ArrayList<Comment>) :
    RecyclerView.Adapter<ShowCommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(var binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        if (data[position].userImage.isNotEmpty())
            Picasso.get().load((data[position].userImage)).into(holder.binding.userImageComment)
        holder.binding.txtUserComment.text = data[position].comment
        holder.binding.userNameComment.text = data[position].userName
    }

    override fun getItemCount(): Int {
        return data.size
    }
}