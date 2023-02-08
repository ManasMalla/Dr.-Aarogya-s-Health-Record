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
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    companion object {

        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                UserViewModel(userRepository = recordApplication().appContainer.userRepository)
            }
        }

    }

    var isFirstRuntime: StateFlow<Boolean> = userRepository.getUsersCount().map { it <= 0 }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(
            STOP_TIMEOUT_MILLISECONDS
        ), false
    )

    val users: SharedFlow<List<User>> = userRepository.getAllUsers().shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(
            STOP_TIMEOUT_MILLISECONDS
        )
    )

    private val currentUser: SharedFlow<User> = userRepository.getCurrentUser().shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(
            STOP_TIMEOUT_MILLISECONDS
        )
    )


    var uiState by mutableStateOf(
        UserUiState(
            name = "", age = "", gender = Gender.Male
        )
    )
        private set

    fun updateUiState(updatedUiState: UserUiState) {
        uiState = updatedUiState.copy(actionsEnabled = updatedUiState.isValid)
    }


    fun registerUser(onRegisterUser: () -> Unit) {
        viewModelScope.launch {
            val currentUser = currentUser.first()
            launch {
                userRepository.registerUser(uiState.toUser())
            }
            launch {
                userRepository.updateUser(currentUser.copy(isCurrentUser = false))
            }
            onRegisterUser()
        }
    }

    fun updateUser(onUpdateUser: () -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(uiState.toUser())
            onUpdateUser()
            //uiState = UserUiState(name = "", age = 0, gender = Gender.Male)
        }
    }

    fun setAsCurrentUser(user: User) {
        viewModelScope.launch {
            userRepository.setCurrentUser(user)
        }
    }

    fun deleteCurrentUser(onDeleteUser: () -> Unit) {
        //TODO: Check why not deleting
        viewModelScope.launch {
            val currentUser = currentUser.first()
            val newCurrentUser = userRepository.getAllUsers()
                .map { users -> users.first { user -> !user.isCurrentUser } }.first()
            userRepository.setCurrentUser(newCurrentUser)
            userRepository.deleteUser(currentUser)
            onDeleteUser()
        }
    }

    /**
     *A method to update the value of [uiState] to match with the latest [currentUser] data
     */
    fun updateUiStateToCurrentUser() {
        viewModelScope.launch {
            uiState = currentUser.map { it.toUiState() }.first()
        }
    }

}
