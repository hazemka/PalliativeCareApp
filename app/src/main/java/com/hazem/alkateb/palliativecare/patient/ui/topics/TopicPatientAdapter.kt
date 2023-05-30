package com.hazem.alkateb.palliativecare.patient.ui.topics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.SplashScreenActivity
import com.hazem.alkateb.palliativecare.databinding.PatientTopicItemBinding
import com.hazem.alkateb.palliativecare.model.SubscribeTopic
import com.hazem.alkateb.palliativecare.model.Topic
import com.hazem.alkateb.palliativecare.model.UserTopic
import com.hazem.alkateb.palliativecare.notification.NotificationCenter
import com.squareup.picasso.Picasso

class TopicPatientAdapter(var context: Context, var data: ArrayList<UserTopic>,
var lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<TopicPatientAdapter.TopicPatientViewHolder>() {

    private val initialData = data

    class TopicPatientViewHolder(var binding: PatientTopicItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicPatientViewHolder {
        val binding = PatientTopicItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return TopicPatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicPatientViewHolder, position: Int) {
        Picasso.get().load(data[position].imageUrl).into(holder.binding.imgDoctorTopic)
        holder.binding.txtDoctorTopicTitle.text = data[position].name
        holder.binding.txtDoctorTopicDesc.text = data[position].description

        holder.binding.btnSubscribeTopic.setOnClickListener {
            subscribeTopic(position)
        }

        holder.binding.imgDoctorTopic.setOnClickListener {
            val b = Bundle()
            b.putSerializable("topic",data[position])
            holder.binding.root.findNavController().navigate(R.id.action_nav_topics_patient_to_topicDetailsFragment,b)
        }
        holder.binding.txtDoctorTopicTitle.setOnClickListener {
            val b = Bundle()
            b.putSerializable("topic",data[position])
            holder.binding.root.findNavController().navigate(R.id.action_nav_topics_patient_to_topicDetailsFragment,b)
        }
        holder.binding.txtDoctorTopicDesc.setOnClickListener {
            val b = Bundle()
            b.putSerializable("topic",data[position])
            holder.binding.root.findNavController().navigate(R.id.action_nav_topics_patient_to_topicDetailsFragment,b)
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

    private fun subscribeTopic(position: Int){
        val dialog = LottieProgressDialog(context, false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل..", View.VISIBLE)
        dialog.show()
        TopicsPatientViewModel.subscribeTopic(context, SubscribeTopic("",data[position].doctorId
            ,data[position].id))
        TopicsPatientViewModel.isTopicSubscribed.observe(lifecycleOwner){
            if(dialog.isShowing) dialog.dismiss()
            if (it){
                val notification = NotificationCenter.createNotification(context,
                    Intent(context, SplashScreenActivity::class.java)
                    ,context.resources.getString(R.string.NotificationChannelId),"تم متابعة موضوع ${data[position].name}","ستصلك إشعارات متعلقة بهذا الموضوع ، ويمكنك مشاهدته من خلال صفحة المتابعات",R.drawable.ic_notification)
                NotificationCenter.notifyNotification(context,notification)
                data.removeAt(position)
                notifyItemRemoved(position)
            }else{
                Toast.makeText(context, "حدث خطأ ما!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}