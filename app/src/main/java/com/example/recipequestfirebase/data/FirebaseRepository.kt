package com.example.recipequestfirebase.data


import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val usersCol = firestore.collection("users")
    private val recipesCol = firestore.collection("recipes")

    // register + create user doc + default recipe
    suspend fun registerEmailPassword(name: String, email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user!!.uid
        val user = UserModel(id = uid, name = name, email = email)
        usersCol.document(uid).set(user).await()

        val default = RecipeModel(
            title = "Resep Default",
            ingredients = listOf("Bahan A","Bahan B"),
            tools = listOf("Wajan","Spatula"),
            steps = listOf("Siapkan bahan","Masak sampai matang"),
            ownerUserId = uid,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        recipesCol.add(default).await()
        return uid
    }

    suspend fun loginEmailPassword(email: String, password: String): String? {
        val res = auth.signInWithEmailAndPassword(email, password).await()
        return res.user?.uid
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun addRecipe(recipe: RecipeModel): String {
        val toInsert = recipe.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis())
        val docRef = recipesCol.add(toInsert).await()
        return docRef.id
    }

    suspend fun updateRecipe(id: String, recipe: RecipeModel) {
        val updates = mapOf(
            "title" to recipe.title,
            "ingredients" to recipe.ingredients,
            "tools" to recipe.tools,
            "steps" to recipe.steps,
            "description" to recipe.description,
            "updatedAt" to System.currentTimeMillis()
        )
        recipesCol.document(id).update(updates).await()
    }

    suspend fun deleteRecipe(id: String) {
        recipesCol.document(id).delete().await()
    }

    suspend fun setRecipeShared(id: String, isShared: Boolean, sharedByUserId: String?) {
        val updates = mapOf(
            "isShared" to isShared,
            "sharedByUserId" to sharedByUserId,
            "updatedAt" to System.currentTimeMillis()
        )
        recipesCol.document(id).update(updates).await()
    }

    // get single recipe by id
    suspend fun getRecipeById(id: String): RecipeModel? {
        val snap = recipesCol.document(id).get().await()
        return snap.toObject(RecipeModel::class.java)?.copy(id = snap.id)
    }

    // observe user's recipes
    fun observeRecipesForUser(userId: String) = callbackFlow<List<RecipeModel>> {
        val sub = recipesCol.whereEqualTo("ownerUserId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { d ->
                    d.toObject(RecipeModel::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    // observe shared recipes (public feed)
    fun observeSharedRecipes() = callbackFlow<List<RecipeModel>> {
        val sub = recipesCol.whereEqualTo("isShared", true)
            .orderBy("updatedAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { d ->
                    d.toObject(RecipeModel::class.java)?.copy(id = d.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    suspend fun getUserById(uid: String): UserModel? {
        val snap = usersCol.document(uid).get().await()
        return snap.toObject(UserModel::class.java)
    }

    suspend fun copySharedToUser(shared: RecipeModel, targetUserId: String): String {
        val copy = shared.copy(
            id = "",
            ownerUserId = targetUserId,
            isShared = false,
            sharedByUserId = null,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return addRecipe(copy)
    }
}