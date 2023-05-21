package com.hazem.alkateb.palliativecare.doctor.ui.add_topic

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentAddTopicBinding
import com.hazem.alkateb.palliativecare.model.Topic

class AddTopicFragment : Fragment() {
    private lateinit var binding:FragmentAddTopicBinding
    private lateinit var dialog: LottieProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddTopicBinding.inflate(inflater, container, false)

        if (isAdded){
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNav.visibility = View.GONE
        }

        dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل",View.VISIBLE)

        val viewModel = ViewModelProvider(this)[AddTopicViewModel::class.java]

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imageTopic.setImageURI(uri)
        }

        binding.btnDoctorAddTopic.setOnClickListener {
            if (checkFields() && isAdded){
                dialog.show()
                viewModel.addTopic(Topic("",binding.txtTopicName.editText!!.text.toString()
                ,binding.txtTopicDesc.editText!!.text.toString(),"",0,0,0,true)
                ,(binding.imageTopic.drawable as BitmapDrawable).bitmap,requireActivity())
            }
        }

        viewModel.isTopicAdded.observe(viewLifecycleOwner){
            doWhenTopicAdded(it)
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

    private fun doWhenTopicAdded(result:Boolean){
        when(result){
            true -> {
                if (dialog.isShowing && isAdded){
                    dialog.dismiss()
                    clearFields()
                    Snackbar.make(requireContext(),binding.root,"تم إضافة الموضوع بنجاح",Snackbar.LENGTH_SHORT).show()
                }
            }
            false -> {
                if (dialog.isShowing && isAdded){
                    dialog.dismiss()
                    Snackbar.make(requireContext(),binding.root,"حدث خطأ ما ، تأكد من اتصال الانترنت",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearFields(){
        binding.txtTopicName.editText!!.text.clear()
        binding.txtTopicDesc.editText!!.text.clear()
        binding.imageTopic.setImageResource(R.drawable.add_image)
    }

}