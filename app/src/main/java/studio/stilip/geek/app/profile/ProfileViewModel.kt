package studio.stilip.geek.app.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.authorization.GetCurrentUserRefUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUser: GetCurrentUserRefUseCase,
    private val getUserById: GetUserByIdUseCase,
) : ViewModel() {

    private val _user = MutableStateFlow(User())

    private val userRef = currentUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val user: StateFlow<User> = _user


    init {
        viewModelScope.launch {
            userRef.collect {
                it?.let {
                    getUserById(it.uid).collect { u ->
                        _user.value = u
                    }
                }
            }
        }
    }
}