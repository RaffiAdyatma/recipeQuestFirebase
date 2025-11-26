package com.example.recipequestfirebase.data

data class UserModel(
    val id: String = "",
    val name: String = "",
    val email: String = ""
)

data class RecipeModel(
    val id: String = "",
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val tools: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val description: String = "",
    val ownerUserId: String = "",
    val isShared: Boolean = false,
    val sharedByUserId: String? = null,
    val imageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
