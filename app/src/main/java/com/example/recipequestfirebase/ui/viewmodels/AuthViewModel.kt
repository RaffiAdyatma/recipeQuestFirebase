package com.example.recipequestfirebase.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.recipequestfirebase.data.FirebaseRepository

class AuthViewModel(private val repo: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _currentUserId = MutableStateFlow<String?>(repo.getCurrentUserId())
    val currentUserId: StateFlow<String?> = _currentUserId

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun register(name: String, email: String, password: String, onSuccess: (String)->Unit = {}, onError: (String)->Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val uid = repo.registerEmailPassword(name, email, password)
                _currentUserId.value = uid
                onSuccess(uid)
            } catch (t: Throwable) {
                _error.value = t.message
                onError(t.message ?: "Error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: (String)->Unit = {}, onError: (String)->Unit = {}) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val uid = repo.loginEmailPassword(email, password)
                if (uid != null) {
                    _currentUserId.value = uid
                    onSuccess(uid)
                } else {
                    onError("Login failed")
                }
            } catch (t: Throwable) {
                onError(t.message ?: "Error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        // In production call Firebase.auth.signOut()
        _currentUserId.value = null
    }
}
