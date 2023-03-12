package studio.stilip.geek.app.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.databinding.FragmentSignUpBinding

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignUpBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.authorization).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        with(binding) {
            btnToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_sign_up_to_sign_in)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userSignedUp
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { isSuccess ->
                        when (isSuccess) {
                            true ->
                                findNavController().navigate(R.id.action_navigation_sign_up_to_games)
                            false -> Toast.makeText(
                                this@SignUpFragment.context,
                                getText(R.string.try_again),
                                Toast.LENGTH_LONG
                            ).show()
                            null -> {}
                        }
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.editEmailHelper
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { res ->
                        editEmailLayout.helperText = getText(res)
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.editPasswordHelper
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { res ->
                        editPasswordLayout.helperText = getText(res)
                    }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.editNicknameHelper
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { res ->
                        editNicknameLayout.helperText = getText(res)
                    }
            }

            btnSignUp.setOnClickListener {
                val email = editEmail.text.toString().trim()
                val password = editPassword.text.toString().trim()
                val nickname = editNickname.text.toString().trim()

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.signUp(email, password, nickname)
                }
            }
        }
    }
}