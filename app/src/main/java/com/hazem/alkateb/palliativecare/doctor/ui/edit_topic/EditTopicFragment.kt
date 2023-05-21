package com.hazem.alkateb.palliativecare.doctor.ui.edit_topic

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentEditTopicBinding
import com.hazem.alkateb.palliativecare.model.Topic
import com.squareup.picasso.Picasso

class EditTopicFragment : Fragment() {
    private lateinit var binding: FragmentEditTopicBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditTopicBinding.inflate(inflater,container,false)
        val topic = requireArguments().getSerializable("topic") as Topic

        Picasso.get().load(topic.imageUrl).into(binding.imageTopic)
        binding.txtTopicName.editText!!.setText(topic.name)
        binding.txtTopicDesc.editText!!.setText(topic.description)

        val viewModel = ViewModelProvider(this)[EditTopicViewModel::class.java]

        val dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل",View.VISIBLE)

        binding.btnEditTopicNow.setOnClickListener {
            if (checkFields() && isAdded){
                topic.name = binding.txtTopicName.editText!!.text.toString()
                topic.description = binding.txtTopicDesc.editText!!.text.toString()
                viewModel.editTopicNow(topic,(binding.imageTopic.drawable as BitmapDrawable).bitmap,requireActivity())
                dialog.show()
            }
        }

        viewModel.isTopicEdited.observe(viewLifecycleOwner){
            if (it){
                dialog.dismiss()
                Snackbar.make(requireContext(),binding.root,"تم تعديل الموضوع بنجاح", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                dialog.dismiss()
                Snackbar.make(requireContext(),binding.root,"حدث خطأ ما ! تأكد من اتصال الإنترنت",Snackbar.LENGTH_SHORT).show()
            }
        }


        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imageTopic.setImageURI(uri)
        }

        binding.imageTopic.setOnClickListener {
            getImage.launch("image/*")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        removeFocusWhenStartWriting()

        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }
    }

    private fun checkFields():Boolean{
        if (binding.txtTopicName.editText!!.text.isNullOrEmpty()){
            binding.txtTopicName.error = "مطلوب"
            return false
        }else if (binding.txtTopicDesc.editText!!.text.isNullOrEmpty()){
            binding.txtTopicDesc.error = "مطلوب"
            return false
        }
        return true
    }

    private fun removeFocusWhenStartWriting(){
        binding.txtTopicName.editText!!.doAfterTextChanged {
            binding.txtTopicName.isErrorEnabled = binding.txtTopicName.editText!!.text.isEmpty()
        }
        binding.txtTopicDesc.editText!!.doAfterTextChanged {
            binding.txtTopicDesc.isErrorEnabled = binding.txtTopicDesc.editText!!.text.isEmpty()
        }
    }

}