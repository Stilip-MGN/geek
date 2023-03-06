package studio.stilip.geek.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Singleton

@Singleton
class HostViewModel : ViewModel() {

    private val _bottomBarVisible = MutableSharedFlow<Boolean>(0, 1)
    private val _toolbarTitle = MutableSharedFlow<String>(0, 1)
    private val _toolbarBackBtn = MutableSharedFlow<Boolean>(0, 1)

    val bottomBarVisible: SharedFlow<Boolean> = _bottomBarVisible
    val toolbarTitle: SharedFlow<String> = _toolbarTitle
    val toolbarBackBtn: SharedFlow<Boolean> = _toolbarBackBtn

    fun setBottomBarVisible(isVisible: Boolean) {
        _bottomBarVisible.tryEmit(isVisible)
    }

    fun setToolbarTitle(title: String) {
        _toolbarTitle.tryEmit(title)
    }

    fun setToolbarBackBtnVisible(isVisible: Boolean) {
        _toolbarBackBtn.tryEmit(isVisible)
    }
}