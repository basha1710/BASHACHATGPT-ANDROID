package com.basha.chatgpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.basha.chatgpt.ui.theme.BashagptTheme
import com.basha.chatgpt.ChatPage as ChatPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtain the ViewModel
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            BashagptTheme {
                // Use Scaffold to handle padding
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pass the ViewModel to ChatPage
                    ChatPage(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = chatViewModel
                    )
                }
            }
        }
    }
}
