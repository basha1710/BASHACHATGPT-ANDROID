package com.basha.chatgpt

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel : ViewModel() {

    // Mutable state list for maintaining the chat messages
    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    // Initialize the generative model with the API key and model name
    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )

    init {
        // Check for holidays or festivals on app launch
        checkForHolidayOrFestival()
    }

    /**
     * Sends a message to the AI model and handles the response.
     *
     * @param question The user's question or input message.
     */
    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                Log.d("ChatViewModel", "Attempting to send message: $question")

                // Build chat history from previous messages
                val chatHistory = messageList.map {
                    Log.d("ChatViewModel", "History - Role: ${it.role}, Message: ${it.message}")
                    content(it.role) { text(it.message) }
                }.toList()

                // Initialize the chat session with the generative model
                val chat = generativeModel.startChat(history = chatHistory)

                // Add the user's message to the list
                messageList.add(MessageModel(question, "user"))
                Log.d("ChatViewModel", "User message added: $question")

                // Add a placeholder for the typing indicator
                messageList.add(MessageModel("Typing...", "model"))
                Log.d("ChatViewModel", "Typing placeholder added.")

                // Send the user's message to the generative model
                val response = chat.sendMessage(question)

                // Remove the typing indicator
                messageList.removeLastOrNull()
                Log.d("ChatViewModel", "Typing placeholder removed.")

                // Add the model's response to the message list
                val responseText = response.text?.toString() ?: "No response received"
                messageList.add(MessageModel(responseText, "model"))
                Log.d("ChatViewModel", "Model response added: $responseText")

            } catch (e: Exception) {
                // Handle exceptions by removing the placeholder and adding an error message
                messageList.removeLastOrNull()
                val errorMessage = "Error: ${e.localizedMessage ?: "Unknown error occurred"}"
                messageList.add(MessageModel(errorMessage, "model"))
                Log.e("ChatViewModel", errorMessage, e)
            }
        }
    }
    // Method to delete all chat history
    fun deleteChatHistory() {
        viewModelScope.launch {
            messageList.clear() // Clear the chat messages
            Log.d("ChatViewModel", "Chat history deleted.")
        }
    }
    /**
     * Checks if today is a holiday or festival and adds a message accordingly.
     */
    private fun checkForHolidayOrFestival() {
        val holidays = mapOf(
            "01-01" to "Today is New Year's Day, celebrate!",
            "12-25" to "Today is Christmas, celebrate!",
            "11-04" to "Today is Diwali, celebrate!",
            "08-15" to "Today is Independence Day, celebrate!"
        )

        val today = SimpleDateFormat("MM-dd", Locale.getDefault()).format(Date())
        holidays[today]?.let { holidayMessage ->
            messageList.add(MessageModel(holidayMessage, "model"))
            Log.d("ChatViewModel", "Holiday message added: $holidayMessage")
        }
    }
}
