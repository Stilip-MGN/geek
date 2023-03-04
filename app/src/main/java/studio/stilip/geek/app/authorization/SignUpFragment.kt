package studio.stilip.geek.app.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
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

            btnSignUp.setOnClickListener {
                val email: String = editEmail.text.toString()
                val password = editPassword.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                findNavController().navigate(R.id.action_navigation_sign_up_to_games)
                            } else {
                                Toast.makeText(
                                    this@SignUpFragment.context,
                                    "${task.exception}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@SignUpFragment.context,
                        "Поля не должны быть пустыми",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}