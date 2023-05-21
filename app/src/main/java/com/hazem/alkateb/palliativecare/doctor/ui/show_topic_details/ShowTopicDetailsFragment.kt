package com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.github.mikephil.charting.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.adapter.DoctorPostAdapter
import com.hazem.alkateb.palliativecare.databinding.FragmentShowTopicDetailsBinding
import com.hazem.alkateb.palliativecare.databinding.HideShowDialogBinding
import com.hazem.alkateb.palliativecare.model.Topic
class ShowTopicDetailsFragment : Fragment() {
    private lateinit var binding: FragmentShowTopicDetailsBinding
    private lateinit var showTopicDetailsViewModel: ShowTopicDetailsViewModel
    private lateinit var loadingDialog: LottieProgressDialog
    private lateinit var doctorPostsAdapter:DoctorPostAdapter
    private var isShowTopic = false
    companion object{
         var topicId = ""
    }
    var topicName = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowTopicDetailsBinding.inflate(inflater, container, false)

        showTopicDetailsViewModel = ViewModelProvider(this)[ShowTopicDetailsViewModel::class.java]

        if (isAdded) {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.visibility = View.GONE
            binding.shimmerLayout.startShimmer()
        }

        loadingDialog = LottieProgressDialog(
            requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل", View.VISIBLE
        )

        topicId = requireArguments().getString("topicId")!!
        topicName = requireArguments().getString("topicName")!!


        showTopicDetailsViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                if (loadingDialog.isShowing) loadingDialog.dismiss()
                Snackbar.make(binding.root, "تم حذف الموضوع بنجاح", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                if (loadingDialog.isShowing) loadingDialog.dismiss()
                Snackbar.make(binding.root, "حدث خطأ ما ! تأكد من اتصال الانترنت", Snackbar.LENGTH_SHORT).show()
            }
        }

        showTopicDetailsViewModel.isHideShowTopic.observe(viewLifecycleOwner){
            if (loadingDialog.isShowing) loadingDialog.dismiss()
            if (it){
                Snackbar.make(binding.root,"تم تعديل حالة العرض بنجاح",Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(binding.root,"حدث خطأ ما ! تأكد من اتصال الإنترنت",Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnAddPost.setOnClickListener {
            val b = Bundle()
            b.putString("topicId",topicId)
            b.putString("topicName",topicName)
            findNavController().navigate(R.id.action_showTopicDetailsFragment_to_addPostFragment,b)
        }

        showTopicDetailsViewModel.topic.observe(viewLifecycleOwner){topic ->
            if (loadingDialog.isShowing) loadingDialog.dismiss()
            binding.txtShowTopicTitle.text = topic.name
            binding.txtShowTopicDes.text = topic.description
            binding.numOfFollwers.text = topic.numOfFollowers.toString()
            binding.menuTopic.setOnClickListener {
                showMenu(topic)
            }
            isShowTopic = topic.isShow
        }

        showTopicDetailsViewModel.dataPosts.observe(viewLifecycleOwner){
            if (isAdded){
                binding.txtNumOfPosts.text = it.size.toString()
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.INVISIBLE
                binding.rvShowPosts.visibility = View.VISIBLE
                doctorPostsAdapter = DoctorPostAdapter(requireContext(),it,viewLifecycleOwner)
                binding.rvShowPosts.adapter = doctorPostsAdapter
                binding.rvShowPosts.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }
        if (isAdded){
            showTopicDetailsViewModel.getCurrentUserTopic(topicId,requireActivity())
            showTopicDetailsViewModel.getTopicPosts(topicId,requireActivity())
        }
    }

    private fun showMenu(topic: Topic) {
        val popupMenu = PopupMenu(requireActivity(), binding.menuTopic)
        popupMenu.inflate(R.menu.topic_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    val b = Bundle()
                    b.putSerializable("topic", topic)
                    findNavController().navigate(
                        R.id.action_showTopicDetailsFragment_to_editTopicFragment, b)
                }
                R.id.delete -> {
                    showDeleteDialog(topic.id, topic.imageUrl)
                }
                R.id.show_hide -> {
                    showHideDialog(topic.id)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private fun showDeleteDialog(topicId: String, imageUrl: String) {
        if (isAdded) {
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(true)
                .setTitle("حذف الموضوع !")
                .setMessage("هل أنت متأكد من حذف هذا الموضوع ؟")
                .setNeutralButton("لا") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("نعم") { dialog, _ ->
                    dialog.dismiss()
                    showTopicDetailsViewModel.deleteTopic(topicId, imageUrl, requireActivity())
                    loadingDialog.show()
                }
                .show()
        }
    }

    private fun showHideDialog(topicId:String) {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        val hideShowDialogBinding = HideShowDialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(hideShowDialogBinding.root)
        hideShowDialogBinding.switchShowHide.isChecked = isShowTopic
        when(isShowTopic){
            true -> hideShowDialogBinding.txtStatus.text = "ظاهر"
            false -> hideShowDialogBinding.txtStatus.text = "مخفي"
        }
        hideShowDialogBinding.switchShowHide.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> hideShowDialogBinding.txtStatus.text = "ظاهر"
                false -> hideShowDialogBinding.txtStatus.text = "مخفي"
            }
            isShowTopic = isChecked
        }
        hideShowDialogBinding.btnSaveShowing.setOnClickListener {
            showTopicDetailsViewModel.showHideTopic(requireActivity(),topicId,isShowTopic)
            dialog.dismiss()
            loadingDialog.show()
        }
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }
}
