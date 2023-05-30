package com.hazem.alkateb.palliativecare.patient.ui.topic_details

import android.content.Intent
import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.ChatActivity
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.SplashScreenActivity
import com.hazem.alkateb.palliativecare.databinding.FragmentTopicDetailsBinding
import com.hazem.alkateb.palliativecare.model.SubscribeTopic
import com.hazem.alkateb.palliativecare.model.UserTopic
import com.hazem.alkateb.palliativecare.notification.NotificationCenter
import com.squareup.picasso.Picasso

class TopicDetailsFragment : Fragment() {
    private lateinit var dialog: LottieProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTopicDetailsBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(this)[TopicDetailsViewModel::class.java]
        var followingState = false

        dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل..",View.VISIBLE)

        val topic = requireArguments().getSerializable("topic") as UserTopic

        binding.txtTopicDescription.text = topic.description
        binding.txtTopicNameDetails.text = topic.name

        if (isAdded){
            viewModel.getDoctorDetails(topic.doctorId)
            viewModel.getSubscriptionsIds(requireContext())
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.visibility = View.GONE
        }

        viewModel.doctorDetails.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                if (it[1].isNotEmpty())
                    Picasso.get().load(it[1]).placeholder(R.drawable.user_image).into(binding.doctorDetails.userImage)
                binding.doctorDetails.userName.text = it[0]
            }
        }

        viewModel.myTopicsArray.observe(viewLifecycleOwner){
            if (it.contains(topic.id)){
                binding.btnFollowing.text = "إلغاء المتابعة"
                followingState = true
                binding.btnFollowing.setTextColor(resources.getColor(R.color.Primary_color))
                binding.btnFollowing.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white));
            }
        }

        binding.btnChat.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("receiverId",topic.doctorId)
            intent.putExtra("receiverName",binding.doctorDetails.userName.text.toString())
            startActivity(intent)
        }

        binding.btnGoToPosts.setOnClickListener {
            val b = Bundle()
            b.putSerializable("topic",topic)
            findNavController().navigate(R.id.action_topicDetailsFragment_to_showPostsFragment,b)
        }

        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }

        binding.btnFollowing.setOnClickListener {
            if (followingState){
                dialog.show()
                viewModel.unSubscribeTopic(requireContext(), SubscribeTopic("",topic.doctorId,topic.id))
                viewModel.isTopicUnSubscribed.observe(viewLifecycleOwner){
                    if (it){
                        if (dialog.isShowing) dialog.dismiss()
                        binding.btnFollowing.text = "متابعة"
                        followingState = false
                        createNotificationUnSubscribe()
                        binding.btnFollowing.setTextColor(resources.getColor(R.color.white))
                        binding.btnFollowing.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.Primary_color));

                    }else{
                        if (dialog.isShowing) dialog.dismiss()
                        Snackbar.make(binding.root,"حدث خطأ ما !!",Snackbar.LENGTH_SHORT).show()
                    }
                }
            }else{
                dialog.show()
                viewModel.subscribeTopic(requireContext(),
                    SubscribeTopic("",topic.doctorId,topic.id))
                viewModel.isTopicSubscribed.observe(viewLifecycleOwner) {
                    if (it){
                        if (dialog.isShowing) dialog.dismiss()
                        binding.btnFollowing.text = "إلغاء المتابعة"
                        followingState = true
                        createNotificationSubscribe()
                        binding.btnFollowing.setTextColor(resources.getColor(R.color.white))
                        binding.btnFollowing.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.Primary_color));
                    }else{
                        if (dialog.isShowing) dialog.dismiss()
                        Snackbar.make(binding.root,"حدث خطأ ما !!",Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    private fun createNotificationSubscribe(){
        val notification = NotificationCenter.createNotification(requireContext(),
            Intent(requireContext(), SplashScreenActivity::class.java)
            ,requireActivity().resources.getString(R.string.NotificationChannelId),"تم متابعة الموضوع","ستصلك إشعارات متعلقة بهذا الموضوع ، ويمكنك مشاهدته من خلال صفحة المتابعات",R.drawable.ic_notification)
        NotificationCenter.notifyNotification(requireContext(),notification)
    }

    private fun createNotificationUnSubscribe(){
        val notification = NotificationCenter.createNotification(requireContext(),
            Intent(requireContext(), SplashScreenActivity::class.java)
            ,requireActivity().resources.getString(R.string.NotificationChannelId),"تم إلغاء متابعة الموضوع","يمكنك متابعته مرة أخرى من صفحة المواضيع",R.drawable.ic_notification)
        NotificationCenter.notifyNotification(requireContext(),notification)
    }

}