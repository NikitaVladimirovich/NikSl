package com.pavesid.niksl.ui.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.niksl.data.DataRepository
import com.pavesid.niksl.data.model.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoneViewModel @Inject constructor(
    private val repository: DataRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _notViewedAchievements = MutableLiveData<List<Achievement>>()
    val notViewedAchievements: LiveData<List<Achievement>> = _notViewedAchievements

    private val _doneAchievements = MutableLiveData<List<Achievement>>()
    val doneAchievements: LiveData<List<Achievement>> = _doneAchievements

    private val _notYetAchievements = MutableLiveData<List<Achievement>>()
    val notYetAchievements: LiveData<List<Achievement>> = _notYetAchievements

    fun loadDoneAchievements() {
        viewModelScope.launch(dispatcher) {
            _doneAchievements.postValue(repository.getDoneAchievements())
        }
    }

    fun loadNotYetAchievements() {
        viewModelScope.launch(dispatcher) {
            _notYetAchievements.postValue(repository.getNotYetAchievements())
        }
    }

    fun loadNotViewedAchievements() {
        viewModelScope.launch(dispatcher) {
            _notViewedAchievements.postValue(repository.getNotViewedAchievements())
        }
    }
}
