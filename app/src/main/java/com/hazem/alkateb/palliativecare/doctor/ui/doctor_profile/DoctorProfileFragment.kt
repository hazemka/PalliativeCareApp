package com.hazem.alkateb.palliativecare.doctor.ui.doctor_profile

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.MainActivity
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentDoctorProfileBinding
import com.squareup.picasso.Picasso

class DoctorProfileFragment : Fragment() {

    private lateinit var binding: FragmentDoctorProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val doctorProfileViewModel = ViewModelProvider(this)[DoctorProfileViewModel::class.java]
        binding = FragmentDoctorProfileBinding.inflate(inflater, container, false)

        if (isAdded){
            doctorProfileViewModel.getUserData(requireActivity())
            doctorProfileViewModel.getImage(requireActivity())
            binding.dialog2.show()
        }

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.dialog2.show()
            binding.userPhoto.setImageURI(uri)
            doctorProfileViewModel.uploadImage((binding.userPhoto.drawable as BitmapDrawable).bitmap
            ,requireActivity())
        }

        doctorProfileViewModel.data.observe(viewLifecycleOwner){user ->
            binding.txtUsername.text = "${user.firstName} ${user.secondName} ${user.familyName}"
            binding.txtUserEmail.text = user.email
            binding.txtUserDate.text = user.dob
            binding.txtUserLocation.text = user.location
            binding.txtUserPhone.text = user.phone
            binding.dialog2.hide()
        }

        doctorProfileViewModel.isLogout.observe(viewLifecycleOwner){
            if (it && isAdded){
                requireActivity().startActivity(Intent(requireContext(),MainActivity::class.java))
                requireActivity().finish()
            }
        }

        doctorProfileViewModel.isUserPhotoUploaded.observe(viewLifecycleOwner){
            binding.dialog2.hide()
        }

        doctorProfileViewModel.imageUrl.observe(viewLifecycleOwner){
            if (it != "")
                Picasso.get().load(it).placeholder(R.drawable.user_image).into(binding.userPhoto)
        }

        binding.btnLogout.setOnClickListener {
            if (isAdded)
                doctorProfileViewModel.logout(requireActivity())
        }

        binding.userPhoto.setOnClickListener {
            getImage.launch("image/*")
        }

        return binding.root
    }
}