package com.hazem.alkateb.palliativecare.doctor.ui.add_post

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentAddPostBinding
import com.hazem.alkateb.palliativecare.model.Post

class AddPostFragment : Fragment() {
    private lateinit var binding: FragmentAddPostBinding
    private lateinit var viewModel: AddPostViewModel
    private lateinit var dialog: LottieProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[AddPostViewModel::class.java]

        val topicId = requireArguments().getString("topicId","")
        val topicName = requireArguments().getString("topicName","")

        Log.e("hzm", "onCreateView: $topicId / $topicName")

        dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار تحميل الوسائط ...",View.VISIBLE)

        val dialog2 = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار النشر...",View.VISIBLE)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (isAdded){
                dialog.show()
                viewModel.uploadMedia(uri!!,requireActivity(),"postsImages")
            }
        }

        val getPDF = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (isAdded){
                dialog.show()
                viewModel.uploadMedia(uri!!,requireActivity(),"pdfFiles")
            }
        }

        val getVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (isAdded){
                dialog.show()
                viewModel.uploadMedia(uri!!,requireActivity(),"postsVideos")
            }
        }

        binding.btnImageType.root.setOnClickListener {
            selectUploadImage()
            getImage.launch("image/*")
        }

        binding.btnPdfType.root.setOnClickListener {
            selectUploadPDF()
            getPDF.launch("application/pdf")
        }

        binding.btnVideoType.root.setOnClickListener {
            selectUploadVideo()
            getVideo.launch("video/*")
        }

        viewModel.isMediaUploaded.observe(viewLifecycleOwner){
            if (dialog.isShowing) dialog.dismiss()
            if (it){
                binding.mediaUploaded1.visibility = View.VISIBLE
                binding.mediaUploaded2.visibility = View.VISIBLE
            }else{
                Snackbar.make(binding.root,"حدث خطأ أثناء رفع الوسائط",Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnDoctorAddPost.setOnClickListener {
            if (checkFields() && isAdded){
                dialog2.show()
                Log.e("hzm", "btnDoctorAddPost: $topicId / $topicName")
                viewModel.addNewPost(topicId!!,topicName!! ,Post("","",binding.txtPostDecs.editText!!.text.toString()
                ,0,0,""),requireActivity())
            }
        }

        viewModel.isPostUploaded.observe(viewLifecycleOwner){
            if (dialog2.isShowing) dialog2.dismiss()
            if (it){
                Snackbar.make(binding.root,"تم النشر",Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Snackbar.make(binding.root,"حدث خطأ أثناء النشر",Snackbar.LENGTH_SHORT).show()
            }
        }

        removeFocusWhenStartWriting()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.btnVideoType.imageView4.setImageResource(R.drawable.video_type)
        binding.btnPdfType.imageView4.setImageResource(R.drawable.pdf_type)
        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }
    }

    private fun selectUploadImage(){
        binding.btnImageType.backDesign.setBackgroundResource(R.drawable.back_media_selected)
        binding.btnPdfType.backDesign.setBackgroundResource(R.color.white)
        binding.btnVideoType.backDesign.setBackgroundResource(R.color.white)
    }

    private fun selectUploadVideo(){
        binding.btnImageType.backDesign.setBackgroundResource(R.color.white)
        binding.btnPdfType.backDesign.setBackgroundResource(R.color.white)
        binding.btnVideoType.backDesign.setBackgroundResource(R.drawable.back_media_selected)
    }

    private fun selectUploadPDF(){
        binding.btnPdfType.backDesign.setBackgroundResource(R.drawable.back_media_selected)
        binding.btnVideoType.backDesign.setBackgroundResource(R.color.white)
        binding.btnImageType.backDesign.setBackgroundResource(R.color.white)
    }

    private fun checkFields():Boolean{
        if (binding.txtPostDecs.editText!!.text.isNullOrEmpty()){
            binding.txtPostDecs.error = "مطلوب"
            return false
        }
        return true
    }

    private fun removeFocusWhenStartWriting(){
        binding.txtPostDecs.editText!!.doAfterTextChanged {
            binding.txtPostDecs.isErrorEnabled = binding.txtPostDecs.editText!!.text.isEmpty()
        }
    }

}