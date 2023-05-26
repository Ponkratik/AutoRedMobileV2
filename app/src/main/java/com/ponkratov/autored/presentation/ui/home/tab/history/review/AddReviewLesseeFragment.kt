package com.ponkratov.autored.presentation.ui.home.tab.history.review

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ponkratov.autored.databinding.FragmentAddReviewLesseeBinding
import com.ponkratov.autored.domain.model.Lce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddReviewLesseeFragment : Fragment() {

    private var _binding: FragmentAddReviewLesseeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<AddReviewLesseeViewModel>()

    private val args by navArgs<AddReviewLesseeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAddReviewLesseeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            progressCircular.isVisible = false
            layoutReview.isVisible = true
            checkbox5Car.isChecked = true
            checkbox5Lessor.isChecked = true

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonSend.setOnClickListener {
                val markLessor = when (radioGroupLessor.checkedRadioButtonId) {
                    checkbox1Lessor.id -> 1
                    checkbox2Lessor.id -> 2
                    checkbox3Lessor.id -> 3
                    checkbox4Lessor.id -> 4
                    checkbox5Lessor.id -> 5
                    else -> 1
                }

                val markCar = when (radioGroupCar.checkedRadioButtonId) {
                    checkbox1Car.id -> 1
                    checkbox2Car.id -> 2
                    checkbox3Car.id -> 3
                    checkbox4Car.id -> 4
                    checkbox5Car.id -> 5
                    else -> 1
                }

                viewModel.onSendButtonClicked(
                    markCar = markCar,
                    commentCar = editTextReviewCar.text.toString(),
                    carTo = args.carId,
                    markUser = markLessor,
                    commentUser = editTextReviewLessor.text.toString(),
                    userTo = args.lessorId
                )
            }

            viewModel
                .lceFlow
                .onEach {
                    when (it) {
                        is Lce.Loading -> {
                            progressCircular.isVisible = true
                            layoutReview.isVisible = false
                        }
                        is Lce.Content -> {
                            progressCircular.isVisible = false
                            layoutReview.isVisible = true
                            editTextReviewCar.text?.clear()
                            editTextReviewLessor.text?.clear()
                            AlertDialog
                                .Builder(requireContext())
                                .setTitle("Отзыв")
                                .setMessage(it.data)
                                .setPositiveButton(android.R.string.ok, null)
                                .setOnDismissListener {
                                    findNavController().navigateUp()
                                }
                                .show()
                        }
                        is Lce.Error -> {
                            progressCircular.isVisible = false
                            layoutReview.isVisible = true
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}