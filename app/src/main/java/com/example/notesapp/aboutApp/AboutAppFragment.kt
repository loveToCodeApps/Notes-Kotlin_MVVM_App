package com.example.notesapp.aboutApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentAboutAppBinding = DataBindingUtil.inflate(inflater,
    R.layout.fragment_about_app,container,false)

        //animations
        val introAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.right_to_center)
        binding.introHeading.animation = introAnim

        return binding.root

    }


}