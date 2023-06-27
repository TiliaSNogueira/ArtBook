package com.example.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.artbook.R
import com.example.artbook.databinding.FragmentArtDetailsBinding
import com.example.artbook.util.Status
import com.example.artbook.viewmodel.ArtDetailsViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_art_details) {

    private lateinit var viewModel: ArtDetailsViewModel

    private var fragmentArtDetailsBinding: FragmentArtDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArtDetailsViewModel::class.java]
        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentArtDetailsBinding = binding


        subscribeToObservers()
        binding.artImageView.setOnClickListener {
            //findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
            val action = ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
            findNavController().navigate(action)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding.saveButton.setOnClickListener {
            viewModel.makeArt(
                binding.nameText.text.toString(),
                binding.artistText.text.toString(),
                binding.yearText.text.toString()
            )
        }

        val args: ArtDetailsFragmentArgs by navArgs()
        val imageSelected = args.art
        fragmentArtDetailsBinding?.let {
            glide.load(imageSelected).into(it.artImageView)
            viewModel.selectedImage(imageSelected)
        }


    }

    private fun subscribeToObservers() {

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_artDetailsFragment_to_artsFragment)
                    viewModel.resetInsertArtMsg()
                }

                Status.LOADING -> {

                }

                Status.ERROR -> {
                    Toast.makeText(context, it.message ?: "Error", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    override fun onDestroyView() {
        fragmentArtDetailsBinding = null
        super.onDestroyView()
    }

}