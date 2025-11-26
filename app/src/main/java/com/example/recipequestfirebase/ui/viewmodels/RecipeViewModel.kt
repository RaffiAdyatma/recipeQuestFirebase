package com.example.recipequestfirebase.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.example.recipequestfirebase.data.FirebaseRepository
import com.example.recipequestfirebase.data.RecipeModel

class RecipeViewModel(private val repo: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _recipes = MutableStateFlow<List<RecipeModel>>(emptyList())
    val recipes: StateFlow<List<RecipeModel>> = _recipes

    fun observeUserRecipes(userId: String) {
        repo.observeRecipesForUser(userId)
            .onEach { _recipes.value = it }
            .launchIn(viewModelScope)
    }

    fun observeShared() {
        repo.observeSharedRecipes()
            .onEach { _recipes.value = it }
            .launchIn(viewModelScope)
    }

    fun addRecipe(recipe: RecipeModel, onDone: (String)->Unit = {}) {
        viewModelScope.launch {
            val id = repo.addRecipe(recipe)
            onDone(id)
        }
    }

    fun updateRecipe(id: String, recipe: RecipeModel, onDone: ()->Unit = {}) {
        viewModelScope.launch {
            repo.updateRecipe(id, recipe)
            onDone()
        }
    }

    fun deleteRecipe(id: String, onDone: ()->Unit = {}) {
        viewModelScope.launch {
            repo.deleteRecipe(id)
            onDone()
        }
    }

    fun setShared(id: String, isShared: Boolean, sharedById: String?) {
        viewModelScope.launch {
            repo.setRecipeShared(id, isShared, sharedById)
        }
    }

    suspend fun getRecipeById(id: String): RecipeModel? {
        return repo.getRecipeById(id)
    }

    suspend fun getUserById(uid: String) = repo.getUserById(uid)

    fun copyShared(shared: RecipeModel, targetUserId: String, onDone: (String)->Unit = {}) {
        viewModelScope.launch {
            val id = repo.copySharedToUser(shared, targetUserId)
            onDone(id)
        }
    }
}
