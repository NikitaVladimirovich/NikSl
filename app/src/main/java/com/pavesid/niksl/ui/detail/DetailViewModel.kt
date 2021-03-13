package com.pavesid.niksl.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pavesid.niksl.data.model.Achievement
import com.pavesid.niksl.data.model.Message
import com.pavesid.niksl.ui.detail.DetailFragment.Companion.ACHIEVEMENT_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val database = Firebase.database.reference.child("messages")

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    val list = mutableListOf<Message>()

    val achievement: Achievement = savedStateHandle.get(ACHIEVEMENT_ARG)!!

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = snapshot.getValue(Message::class.java)
            list.add(message!!)
            _messages.postValue(list)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    init {
        database.addChildEventListener(childEventListener)
        database.get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        database.removeEventListener(childEventListener)
    }

    fun writeMessage(message: String) {
        val mes = Message(UUID.randomUUID().toString(), message, achievement.id)
        database.child(mes.id).setValue(mes)
    }
}