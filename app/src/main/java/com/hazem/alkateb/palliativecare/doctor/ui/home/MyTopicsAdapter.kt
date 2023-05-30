package com.hazem.alkateb.palliativecare.doctor.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.SmallDoctorTopicItemBinding
import com.hazem.alkateb.palliativecare.model.Topic
import com.hazem.alkateb.palliativecare.model.UserTopic
import com.squareup.picasso.Picasso

class MyTopicsAdapter(var context: Context, var data: ArrayList<Topic>,var type:String) :
    RecyclerView.Adapter<MyTopicsAdapter.MyTopicsViewHolder>() {

    companion object{
        val selectedTopic = MutableLiveData<Topic>()
    }

    private var selectedPosition = 0

    class MyTopicsViewHolder(var binding: SmallDoctorTopicItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopicsViewHolder {
        val binding = SmallDoctorTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyTopicsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyTopicsViewHolder, position: Int) {
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imageView5)
        holder.binding.textView14.text = data[position].name

        if(type =="doctor"){
            if (selectedPosition == holder.adapterPosition) {
                holder.binding.doctorTopicContainer.setBackgroundResource(R.drawable.round_button)
            } else {
                holder.binding.doctorTopicContainer.setBackgroundColor(context.resources.getColor(R.color.white))
            }
            holder.binding.root.setOnClickListener {
                selectedPosition = holder.adapterPosition
                notifyDataSetChanged()
                selectedTopic.value = data[position]
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}