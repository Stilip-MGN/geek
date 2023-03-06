package studio.stilip.geek.app.authorization

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import studio.stilip.geek.R
import studio.stilip.geek.domain.usecase.authorization.SignUpUserUseCase
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUser: SignUpUserUseCase,
) : ViewModel() {

    val userSignedUp = MutableStateFlow<Boolean?>(null)
    val editEmailHelper = MutableStateFlow(R.string.empty_text)
    val editPasswordHelper = MutableStateFlow(R.string.empty_text)

    suspend fun signUp(email: String, password: String) {
        val isValEmail = validateEmail(email)
        val isValPassword = validatePassword(password)
        if (isValEmail && isValPassword) {
            try {
                signUpUser(email, password)
                userSignedUp.value = true
            } catch (ex: FirebaseAuthWeakPasswordException) {
                userSignedUp.value = false
            } catch (ex: FirebaseAuthInvalidCredentialsException) {
                editEmailHelper.value = R.string.invalid_email
                userSignedUp.value = false
            } catch (ex: FirebaseAuthUserCollisionException) {
                userSignedUp.value = false
                editEmailHelper.value = R.string.error_email_already_exists
            }
        } else userSignedUp.value = false
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            editEmailHelper.value = R.string.error_input_empty
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailHelper.value = R.string.invalid_email
            false
        } else {
            editEmailHelper.value = R.string.empty_text
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.length < 6) {
            editPasswordHelper.value = R.string.invalid_password
            false
        } else {
            editPasswordHelper.value = R.string.empty_text
            true
        }
    }
}