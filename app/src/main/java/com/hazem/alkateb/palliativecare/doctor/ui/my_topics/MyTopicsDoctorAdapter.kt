package com.hazem.alkateb.palliativecare.doctor.ui.my_topics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.DoctorTopicItemBinding
import com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details.ShowTopicDetailsFragment
import com.hazem.alkateb.palliativecare.model.Topic
import com.squareup.picasso.Picasso

class MyTopicsDoctorAdapter(var context: Context, var data: ArrayList<Topic>) :
    RecyclerView.Adapter<MyTopicsDoctorAdapter.DoctorTopicViewHolder>() {

    private val initialData = data

    class DoctorTopicViewHolder(var binding: DoctorTopicItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorTopicViewHolder {
        val binding = DoctorTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return DoctorTopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorTopicViewHolder, position: Int) {
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imgDoctorTopic)
        holder.binding.txtDoctorTopicTitle.text = data[position].name
        holder.binding.txtDoctorTopicDesc.text = data[position].description
        holder.binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("topicId", data[position].id)
            bundle.putString("topicName", data[position].name)
            holder.binding.root.findNavController().navigate(R.id.action_navigation_my_topics_doctor_to_showTopicDetailsFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun search(text:String){
        val newData = initialData.filter {topic ->
            topic.name.contains(text)
        }
        data = newData as ArrayList<Topic>
        notifyDataSetChanged()
    }

    fun stopSearch(){
        data = initialData
        notifyDataSetChanged()
    }

}