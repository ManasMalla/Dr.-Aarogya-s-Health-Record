package com.manasmalla.draarogyashealthrecord.ui.screens


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.model.enum
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.ProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.math.ceil


class UserViewModel(private val userRepository: UserRepository, context: Context) : ViewModel() {

    /**
     * A companion object to keep track of the [Factory] for the [UserViewModel]
     * @property STOP_TIMEOUT_MILLISECONDS A reference to the duration of time for the viewModel to wait to cancel listening to flow emmissions
     * @property Factory A [ViewModelProvider.Factory] that initializes the [UserViewModel] with a reference to the [UserRepository]
     */
    companion object {

        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserViewModel(
                    userRepository = recordApplication().appContainer.userRepository,
                    recordApplication().applicationContext
                )
            }
        }

    }

    /**
     * A boolean reference whether any [users] exists in the application which is fetched using [SharedFlow] from the [userRepository]
     * @see UserRepository.getUsersCount
     */
    var isFirstRuntime: StateFlow<Boolean> = userRepository.getUsersCount().map { it <= 0 }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(
            STOP_TIMEOUT_MILLISECONDS
        ), true
    )

    /**
     * A reference to the all [users] of the application which is fetched using [SharedFlow] from the [userRepository]
     * @see UserRepository.getAllUsers
     * @sample User
     */
    val users: StateFlow<List<User>> = userRepository.getAllUsers().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(
            STOP_TIMEOUT_MILLISECONDS
        ), listOf()
    )

    var usersProfileUiState: MutableMap<User, ProfileUiState> by mutableStateOf(
        mutableMapOf()
    )

    init {
        viewModelScope.launch {
            userRepository.getAllUsers().filter { it.isNotEmpty() }.collectLatest {
                it.map { user ->
                    usersProfileUiState[user] = ProfileUiState.Loading
                    withContext(Dispatchers.IO) {
                        when (user.image != null) {
                            true -> {
                                val fileDir = File(context.getExternalFilesDir(null), user.image)
                                usersProfileUiState[user] = if (fileDir.exists()) {
                                    val bitmapData = fileDir.inputStream().readBytes()
                                    val imageBitmap = BitmapFactory.decodeByteArray(
                                        fileDir.inputStream().readBytes(), 0, bitmapData.size
                                    ).asImageBitmap()
                                    ProfileUiState.Storage(imageBitmap)
                                } else {
                                    ProfileUiState.Default(gender = user.gender.enum)
                                }
                            }

                            false -> {
                                usersProfileUiState[user] =
                                    ProfileUiState.Default(gender = user.gender.enum)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * The UI State of the Profile Screen
     * @see updateUiState
     * @sample UserUiState
     */
    var uiState by mutableStateOf(
        UserUiState(
            name = "", age = "", gender = Gender.Male
        )
    )
        private set

    /**
     * A method to update the current uiState
     * @param updatedUiState The updated state of the UI that can be used to update the contents on the screen
     */
    fun updateUiState(updatedUiState: UserUiState) {
        uiState = updatedUiState.copy(actionsEnabled = updatedUiState.isValid)
    }

    /**
     * A method to register a user into application
     * @param onRegisterUser A callback function which is called once the user has been registered
     */
    fun registerUser(onRegisterUser: () -> Unit) {
        viewModelScope.launch {
            userRepository.registerUser(uiState.toUser())
            onRegisterUser()
        }
    }

    /**
     * A method to register/add another user to application
     * @param onRegisterUser A callback function which is called once the user has been registered
     */
    fun registerAnotherUser(onRegisterUser: () -> Unit) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser().first().copy(isCurrentUser = false)

            launch {
                userRepository.registerUser(uiState.toUser())
            }
            launch {
                userRepository.updateUser(currentUser)
            }
            onRegisterUser()
        }
    }

    /**
     * A method to update a particular user
     * @param onUpdateUser A callback function which is called once the user has been updated successfully
     */
    fun updateUser(onUpdateUser: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(
                user = uiState.toUser()
                    .copy(uId = userRepository.getCurrentUser().map { it.uId }.first())
            )
            onUpdateUser()
            uiState = UserUiState(name = "", age = "", gender = Gender.Male)
        }
    }

    /**
     * A method to set a particular [user] as the [currentUser]
     * @param user The user to be set as the [currentUser]
     */
    fun setAsCurrentUser(user: User) {
        viewModelScope.launch {
            userRepository.setCurrentUser(user)
        }
    }

    /**
     * A method to delete a particular user's account from the application
     * @param onDeleteUser A callback function which is called once the user has been successfully deleted
     */
    fun deleteCurrentUser(onDeleteUser: () -> Unit) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser().first()
            val newCurrentUser = userRepository.getAllUsers()
                .map { users -> users.first { user -> !user.isCurrentUser } }.first()
            userRepository.setCurrentUser(newCurrentUser)
            userRepository.deleteUser(currentUser)
            onDeleteUser()
        }
    }

    /**
     *A method to update the value of [uiState] to match with the latest [currentUser] data
     * @see UserUiState
     */
    fun updateUiStateToCurrentUser() {
        viewModelScope.launch {
            uiState = userRepository.getCurrentUser().map { it.toUiState() }.first()
        }
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun saveImage(context: Context, uri: Uri?, density: Density) {
        viewModelScope.launch {
            if (uri != null) {
                uiState = uiState.copy(image = "profile_images/${uiState.name}.png")
                withContext(Dispatchers.IO) {
                    val fileDir = File(context.getExternalFilesDir(null), "profile_images")
                    if (!fileDir.exists()) {
                        fileDir.mkdir()
                    }
                    val file = File(fileDir, "${uiState.name}.png")
                    val stream = FileOutputStream(file)
                    try {
                        val input = context.contentResolver.openInputStream(uri)
                        if (input != null) {
                            val bitmap = BitmapFactory.decodeStream(input)
                            val resizedBitmap = getResizedBitmap(
                                bitmap,
                                with(density) { ceil(180.dp.toPx()).toInt() })
                            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            stream.flush()
                            stream.close()
                        }
                    } catch (_: FileNotFoundException) {
                    }
                }
            }
        }
    }

}
