package studio.stilip.geek.app.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import studio.stilip.geek.R
import studio.stilip.geek.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignInBinding.bind(view)

        with(binding) {
            btnToSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_sign_in_to_sign_up)
            }
        }
    }
}