package studio.stilip.geek.app.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import studio.stilip.geek.data.UserCacheManager
import studio.stilip.geek.domain.entities.Game
import studio.stilip.geek.domain.entities.User
import studio.stilip.geek.domain.usecase.game.GetGameByIdUseCase
import studio.stilip.geek.domain.usecase.user.GetUserByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserCollectionByIdUseCase
import studio.stilip.geek.domain.usecase.game.GetUserWishlistByIdUseCase
import studio.stilip.geek.domain.usecase.user.UpdateUserProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val updateProfile: UpdateUserProfileUseCase
) : ViewModel() {

    val userId = UserCacheManager.getUserId()
    private val _nickname = MutableStateFlow("")
    private val _status = MutableStateFlow("")
    val profileUpdated = MutableStateFlow<Boolean?>(null)

    val user = getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, User())

    fun onNicknameChanged(nickname: String) {
        if (nickname == _nickname.value) return
        viewModelScope.launch {
            _nickname.value = nickname
        }
    }

    fun onStatusChanged(status: String) {
        if (status == _status.value) return
        viewModelScope.launch {
            _status.value = status
        }
    }

    fun onCompleteClicked(data: ByteArray) {
        viewModelScope.launch {
            val user = user.value.copy(name = _nickname.value, status = _status.value)
            updateProfile(user, data)
            profileUpdated.value = true
        }
    }
}