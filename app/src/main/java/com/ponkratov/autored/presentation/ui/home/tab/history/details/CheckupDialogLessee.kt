package com.ponkratov.autored.presentation.ui.home.tab.history.details

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.ponkratov.autored.databinding.FragmentDialogCheckupBinding
import com.ponkratov.autored.presentation.extensions.addHorisontalSpace
import com.ponkratov.autored.presentation.ui.home.tab.account.addadvertisement.ImageUriAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CheckupDialogLessee(private val rideId: String) : DialogFragment() {
    private var _binding: FragmentDialogCheckupBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<RideDetailsLesseeViewModel>(owner = {requireParentFragment()})

    private val imageAdapter by lazy {
        ImageUriAdapter(
            context = requireContext()
        )
    }

    private val takeCheckupImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestCheckupPhotoUri?.let { uri ->
                    imagesListUri.add(uri)
                    imageAdapter.submitList(imagesListUri)
                    imageAdapter.notifyItemInserted(imagesListUri.size - 1)
                }
            }
        }
    private var latestCheckupPhotoUri: Uri? = null
    private var imagesListUri: MutableList<Uri> = mutableListOf()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogCheckupBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        with(binding) {
            photoRecyclerView.adapter = imageAdapter
            photoRecyclerView.addHorisontalSpace()

            buttonAddPhotosCheckup.setOnClickListener {
                if (hasPermission(Manifest.permission.CAMERA)) {
                    getTmpFileUri().let { uri ->
                        latestCheckupPhotoUri = uri
                        takeCheckupImageResult.launch(uri)
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            buttonSign.setOnClickListener {
                if (imageAdapter.currentList.isEmpty()) {
                    return@setOnClickListener
                }
                val filesDir = requireContext().applicationContext.filesDir

                viewModel.signActBefore(rideId, imagesListUri.map {
                    val tempFile = File(filesDir, "checkup-${UUID.randomUUID()}.png")
                    val tempInputStream = requireContext().contentResolver.openInputStream(it)
                    val tempOutputStream = FileOutputStream(tempFile)
                    tempInputStream?.copyTo(tempOutputStream)
                    tempInputStream?.close()
                    tempFile
                })
            }

            buttonCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            tmpFile
        )
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}