package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.manasmalla.draarogyashealthrecord.data.RecordRepository
import com.manasmalla.draarogyashealthrecord.data.UserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.enum
import com.manasmalla.draarogyashealthrecord.model.toUiState
import com.manasmalla.draarogyashealthrecord.recordApplication
import com.manasmalla.draarogyashealthrecord.ui.ProfileUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.record.isValid
import com.manasmalla.draarogyashealthrecord.ui.screens.record.toRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

class HomeViewModel(
    val userRepository: UserRepository,
    private val recordRepository: RecordRepository,
    val context: Context
) :
    ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLISECONDS = 5_000L


        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    userRepository = recordApplication().appContainer.userRepository,
                    recordApplication().appContainer.recordRepository,
                    recordApplication().applicationContext
                )
            }
        }

    }

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var userUiState: StateFlow<UserUiState> =
        userRepository.getCurrentUser().filterNotNull().map { it.toUiState() }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(
                STOP_TIMEOUT_MILLISECONDS
            ), UserUiState(name = "User", age = "", gender = Gender.Other)
        )
        private set

    var recordUiState: RecordUiState.Success by mutableStateOf(
        RecordUiState.Success()
    )

    var profileUiState: ProfileUiState by mutableStateOf(
        ProfileUiState.Loading
    )

    init {
        viewModelScope.launch {
//            userRepository.getUsersCount().distinctUntilChanged().collectLatest {
//                if (it > 0) {
            launch {
                userUiState.filterNotNull().collectLatest {
                    recordUiState = RecordUiState.Success(
                        measurements = List(it.metric.size) { "" },
                        measurableMetrics = it.metric,
                    )
                }
            }
            launch {
                userRepository.getCurrentUser().filterNotNull().collectLatest {
                    uiState = HomeUiState.Loading
                    getRecords()
                }
                    }
                    launch {

                        userRepository.getCurrentUser().filterNotNull().collectLatest { user ->
                            profileUiState = ProfileUiState.Loading
                            withContext(Dispatchers.IO) {
                                when (user.image != null) {
                                    true -> {
                                        val fileDir =
                                            File(context.getExternalFilesDir(null), user.image)
                                        profileUiState = if (fileDir.exists()) {
                                            val bitmapData = fileDir.inputStream().readBytes()
                                            val imageBitmap = BitmapFactory.decodeByteArray(
                                                fileDir.inputStream().readBytes(),
                                                0,
                                                bitmapData.size
                                            ).asImageBitmap()
                                            ProfileUiState.Storage(imageBitmap)
                                        } else {
                                            ProfileUiState.Default(gender = user.gender.enum)
                                        }
                                    }

                                    false -> {
                                        profileUiState =
                                            ProfileUiState.Default(gender = user.gender.enum)
                                    }
                                }
                            }
                        }


//                    }
//                }
            }
        }
    }


    var dialogUiState by mutableStateOf(
        false
    )

    private suspend fun getRecords() {
        viewModelScope.launch {
            recordRepository.getUserRecords(userRepository.getCurrentUser().map { it.uId }.first())
                .collectLatest {
                    uiState = if (it.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(it)
                    }
                }
        }
    }

    fun updateRecordUiState(updatedRecordUiState: RecordUiState.Success) {
        recordUiState = updatedRecordUiState.copy(actionsEnabled = updatedRecordUiState.isValid)
    }

    fun addRecord(isPast: Boolean = false) {
        viewModelScope.launch {
            if (!isPast) {
                recordUiState = recordUiState.copy(date = Calendar.getInstance().time)
            }
            recordRepository.addRecord(
                recordUiState.toRecord(
                    userId = userRepository.getCurrentUser().map { it.uId }.first()
                )
            )
            recordUiState = recordUiState.copy(
                measurements = recordUiState.measurements.map { "" },
                actionsEnabled = false
            )
        }
    }

    fun showAccountDialog() {
        dialogUiState = true
    }

    fun dismissAccountDialog() {
        dialogUiState = false
    }

    fun addPastRecord(viewContext: Context) {
        val date = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            viewContext,
            { _, year, month, day ->
                val dateInstance = Calendar.getInstance()
                dateInstance.set(year, month, day)
                recordUiState = recordUiState.copy(date = dateInstance.time)
                addRecord(isPast = true)
            }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()

    }

}