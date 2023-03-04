package studio.stilip.geek.app.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import studio.stilip.geek.R
import studio.stilip.geek.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignUpBinding.bind(view)

        with(binding) {
            btnToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_sign_up_to_sign_in)
            }
        }
    }
}