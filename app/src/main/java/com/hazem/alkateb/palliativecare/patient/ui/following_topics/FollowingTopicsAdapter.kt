package com.hazem.alkateb.palliativecare.patient.ui.following_topics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.DoctorTopicItemBinding
import com.hazem.alkateb.palliativecare.model.UserTopic
import com.squareup.picasso.Picasso

class FollowingTopicsAdapter(var context: Context, var data: ArrayList<UserTopic>) :
    RecyclerView.Adapter<FollowingTopicsAdapter.FollowingTopicsViewHolder>() {

    private val initialData = data

    class FollowingTopicsViewHolder(var binding: DoctorTopicItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingTopicsViewHolder {
        val binding = DoctorTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return FollowingTopicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingTopicsViewHolder, position: Int) {
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imgDoctorTopic)
        holder.binding.txtDoctorTopicTitle.text = data[position].name
        holder.binding.txtDoctorTopicDesc.text = data[position].description

        holder.binding.root.setOnClickListener {
            val b = Bundle()
            b.putSerializable("topic",data[position])
            holder.binding.root.findNavController().navigate(R.id.action_nav_following_topics_to_topicDetailsFragment,b)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun search(text:String){
        val newData = initialData.filter {topic ->
            topic.name.contains(text)
        }
        data = newData as ArrayList<UserTopic>
        notifyDataSetChanged()
    }

    fun stopSearch(){
        data = initialData
        notifyDataSetChanged()
    }
}