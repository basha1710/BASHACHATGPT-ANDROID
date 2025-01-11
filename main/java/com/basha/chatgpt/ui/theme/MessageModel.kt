package com.basha.chatgpt

data class MessageModel(
    val message: String,
    val role: String, // Should be either "user" or "model"
    val type: String = "text" // Default to "text" if type is not provided
)
