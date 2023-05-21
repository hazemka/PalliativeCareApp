package com.hazem.alkateb.palliativecare.doctor.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.databinding.SmallDoctorTopicItemBinding
import com.hazem.alkateb.palliativecare.model.Topic
import com.squareup.picasso.Picasso

class MyTopicsAdapter(var context: Context, var data: ArrayList<Topic>) :
    RecyclerView.Adapter<MyTopicsAdapter.MyTopicsViewHolder>() {

    class MyTopicsViewHolder(var binding: SmallDoctorTopicItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopicsViewHolder {
        val binding = SmallDoctorTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyTopicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyTopicsViewHolder, position: Int) {
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imageView5)
        holder.binding.textView14.text = data[position].name
    }

    override fun getItemCount(): Int {
        return data.size
    }
}