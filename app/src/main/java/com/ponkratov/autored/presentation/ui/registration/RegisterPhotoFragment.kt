package com.ponkratov.autored.presentation.ui.registration

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ponkratov.autored.databinding.FragmentRegisterPhotoBinding
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.presentation.utils.hasPermission
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class RegisterPhotoFragment : Fragment() {
    private var _binding: FragmentRegisterPhotoBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<RegisterPhotoViewModel>()

    private val args by navArgs<RegisterPhotoFragmentArgs>()

    private val takePassportImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestPassportPhotoUri?.let { uri ->
                    binding.imagePassport.setImageURI(uri)
                }
            }
        }
    private var latestPassportPhotoUri: Uri? = null

    private val takeDriverLicenseImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestDriverLicensePhotoUri?.let { uri ->
                    binding.imageDriverLicense.setImageURI(uri)
                }
            }
        }
    private var latestDriverLicensePhotoUri: Uri? = null

    private val selectAvatarFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.imageAvatar.setImageURI(result.data?.data)
                latestAvatarPhotoUri = result.data?.data
            }
        }

    private var latestAvatarPhotoUri: Uri? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRegisterPhotoBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionLauncher.launch(Manifest.permission.CAMERA)
        with(binding) {
            progressCircular.isVisible = false

            buttonPhotoPassport.setOnClickListener {
                if (hasPermission(requireContext(), Manifest.permission.CAMERA)) {
                    getTmpFileUri().let { uri ->
                        latestPassportPhotoUri = uri
                        takePassportImageResult.launch(uri)
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            buttonPhotoDriverLicense.setOnClickListener {
                if (hasPermission(requireContext(), Manifest.permission.CAMERA)) {
                    getTmpFileUri().let { uri ->
                        latestDriverLicensePhotoUri = uri
                        takeDriverLicenseImageResult.launch(uri)
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }

            buttonPhotoAvatar.setOnClickListener {
                openGalleryForImage()
            }

            buttonRegister.setOnClickListener {
                if (latestAvatarPhotoUri == null
                    || latestPassportPhotoUri == null
                    || latestDriverLicensePhotoUri == null
                ) {
                    AlertDialog
                        .Builder(requireContext())
                        .setTitle("Регистрация")
                        .setMessage("Добавьте все требуемые фотографии!")
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                    return@setOnClickListener
                }

                val filesDir = requireContext().applicationContext.filesDir
                val avatarFile = File(filesDir, "avatar.png")
                val avatarInputStream = requireContext().contentResolver.openInputStream(
                    requireNotNull(latestAvatarPhotoUri)
                )
                val avatarOutputStream = FileOutputStream(avatarFile)
                avatarInputStream?.copyTo(avatarOutputStream)
                avatarInputStream?.close()

                val passportFile = File(filesDir, "passport.png")
                val passportInputStream = requireContext().contentResolver.openInputStream(
                    requireNotNull(latestPassportPhotoUri)
                )
                val passportOutputStream = FileOutputStream(passportFile)
                passportInputStream?.copyTo(passportOutputStream)
                passportInputStream?.close()

                val driverLicenseFile = File(filesDir, "driverLicense.png")
                val driverLicenseInputStream = requireContext().contentResolver.openInputStream(
                    requireNotNull(latestDriverLicensePhotoUri)
                )
                val driverLicenseOutputStream = FileOutputStream(driverLicenseFile)
                driverLicenseInputStream?.copyTo(driverLicenseOutputStream)
                driverLicenseInputStream?.close()

                viewModel.onRegisterButtonClicked(
                    fio = args.fio,
                    email = args.email,
                    rawPassword = args.rawPassword,
                    phone = args.phone,
                    birthdate = args.birthdate,
                    passportNumber = args.driverLicenseNumber,
                    driverLicenseNumber = args.driverLicenseNumber,
                    profileDescription = args.profileDescription,
                    profilePhoto = avatarFile,
                    passportPhoto = passportFile,
                    driverLicensePhoto = driverLicenseFile
                )
            }

            viewModel
                .lceFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            progressCircular.isVisible = true
                            layoutRegisterPhotos.isVisible = false
                        }
                        is Lce.Content -> {
                            progressCircular.isVisible = false
                            findNavController().navigate(RegisterPhotoFragmentDirections.actionRegisterPhotoToRegisterWait())
                        }
                        is Lce.Error -> {
                            progressCircular.isVisible = false
                            layoutRegisterPhotos.isVisible = true
                            Snackbar.make(
                                requireView(),
                                it.message.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    private fun openGalleryForImage() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        selectAvatarFromGalleryResult.launch(pickPhoto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}