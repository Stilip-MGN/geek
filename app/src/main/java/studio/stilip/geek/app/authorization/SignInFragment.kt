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
import studio.stilip.geek.databinding.FragmentSignInBinding

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignInBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getText(R.string.authorization).toString())
        hostViewModel.setToolbarBackBtnVisible(false)

        with(binding) {
            btnToSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_sign_in_to_sign_up)
            }

            btnSignIn.setOnClickListener {
                val email = editEmail.text.toString().trim()
                val password = editPassword.text.toString().trim()

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.signIn(email, password)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userSignedIn
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect { isSuccess ->
                        when (isSuccess) {
                            true ->
                                findNavController().navigate(R.id.action_navigation_sign_in_to_games)
                            false -> Toast.makeText(
                                this@SignInFragment.context,
                                getText(R.string.error_sign_in),
                                Toast.LENGTH_LONG
                            ).show()
                            else -> {}
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
        }
    }
}