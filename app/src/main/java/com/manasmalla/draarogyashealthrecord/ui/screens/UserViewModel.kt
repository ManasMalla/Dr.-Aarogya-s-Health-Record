package com.manasmalla.draarogyashealthrecord.ui.screens


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    /**
     * A companion object to keep track of the [Factory] for the [UserViewModel]
     * @property STOP_TIMEOUT_MILLISECONDS A reference to the duration of time for the viewModel to wait to cancel listening to flow emmissions
     * @property Factory A [ViewModelProvider.Factory] that initializes the [UserViewModel] with a reference to the [UserRepository]
     */
    companion object {

        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserViewModel(userRepository = recordApplication().appContainer.userRepository)
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
        ), false
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
            userRepository.updateUser(
                userRepository.getCurrentUser().first().copy(isCurrentUser = false)
            )
            userRepository.registerUser(uiState.toUser())
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
        //TODO: Check why not deleting
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

}
