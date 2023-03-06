package studio.stilip.geek.app.authorization

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import studio.stilip.geek.R
import studio.stilip.geek.domain.usecase.authorization.SignInUserUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUser: SignInUserUseCase,
) : ViewModel() {

    val userSignedIn = MutableStateFlow<Boolean?>(null)
    val editEmailHelper = MutableStateFlow(R.string.empty_text)
    val editPasswordHelper = MutableStateFlow(R.string.empty_text)

    suspend fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty())
            try {
                signInUser(email, password)
                userSignedIn.value = true
            } catch (e: FirebaseAuthInvalidUserException) {
                userSignedIn.value = false
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                userSignedIn.value = false
            }
        else {
            editEmailHelper.value = R.string.error_input_empty
            editPasswordHelper.value = R.string.error_input_empty
            userSignedIn.value = false
        }
    }
}