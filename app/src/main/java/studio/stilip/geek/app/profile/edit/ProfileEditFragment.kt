package studio.stilip.geek.app.profile.edit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import studio.stilip.geek.R
import studio.stilip.geek.app.HostViewModel
import studio.stilip.geek.app.events.event.EventFragment
import studio.stilip.geek.databinding.FragmentProfileEditBinding
import studio.stilip.geek.domain.entities.User
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ProfileEditFragment : Fragment(R.layout.fragment_profile_edit) {
    private val hostViewModel: HostViewModel by activityViewModels()
    private val viewModel: ProfileEditViewModel by viewModels()
    private lateinit var binding: FragmentProfileEditBinding

    val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Glide.with(binding.avatar)
                .load(uri)
                .centerCrop()
                .into(binding.avatar)
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context as AppCompatActivity

        context.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.done_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_done -> {
                        val bitmap = (binding.avatar.drawable as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        viewModel.onCompleteClicked(data)
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileEditBinding.bind(view)

        hostViewModel.setBottomBarVisible(false)
        hostViewModel.setToolbarTitle(getString(R.string.edit_profile))
        hostViewModel.setToolbarBackBtnVisible(true)

        with(binding) {
            editNickname.doOnTextChanged { text, _, _, _ ->
                viewModel.onNicknameChanged(text.toString())
            }
            editStatus.doOnTextChanged { text, _, _, _ ->
                viewModel.onStatusChanged(text.toString())
            }

            avatar.setOnClickListener {
                getContent.launch("image/*")
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { user ->
                    if (user != User())
                        with(binding) {
                            editNickname.setText(user.name)
                            editStatus.setText(user.status)

                            Glide.with(avatar)
                                .load(user.avatar)
                                .centerCrop()
                                .into(avatar)

                            this@launch.cancel()
                        }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profileUpdated
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { isSuccess ->
                    when (isSuccess) {
                        true -> {
                            findNavController().navigate(
                                R.id.action_navigation_profile_edit_to_profile
                            )
                        }
                        false -> {}
                        null -> {}
                    }
                }
        }
    }
}