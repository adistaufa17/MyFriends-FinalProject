package com.adista.finalproject.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var idDb: Int = 0,

    @SerializedName("id")
    val idUser: Int,

    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("profile")
    val profile: String?
)

