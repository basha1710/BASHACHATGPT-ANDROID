@file:OptIn(ExperimentalMaterial3Api::class)

package com.basha.chatgpt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.basha.chatgpt.ui.theme.ColorModelMessage
import com.basha.chatgpt.ui.theme.ColorUserMessage
import com.basha.chatgpt.ui.theme.Purple80
import com.basha.chatgpt.R

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Fixed Header with reduced height and rounded corners at the top
        AppHeader()

        // Spacer to separate header from message list
        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable message list between the header and footer
        Box(modifier = Modifier.weight(1f)) {
            MessageList(
                modifier = Modifier.fillMaxSize(),
                messageList = viewModel.messageList
            )
        }

        // Fixed Footer with the message input box
        MessageInput(
            onMessageSend = {
                viewModel.sendMessage(it)
            }
        )

        // Delete Chat History Button below the footer (after the message input box)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center // Center the button horizontally
        ) {
            Button(
                onClick = { viewModel.deleteChatHistory() },
                modifier = Modifier
                    .height(40.dp) // Set the height of the button
                    .widthIn(min = 100.dp) // Ensure the button has a minimum width
                    .wrapContentSize(Alignment.Center) // Center the content (icon/text) inside the button
                    .clip(RoundedCornerShape(20.dp)) // Rounded corners for the button
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color.Yellow, Color.Red)
                        )
                    ) // Yellow to Red gradient
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete, // Delete icon
                    contentDescription = "Delete Chat History",
                    modifier = Modifier.size(30.dp) // Size of the icon inside the button
                )
                Spacer(modifier = Modifier.width(10.dp)) // Add space between the icon and the text
                Text(
                    text = "Delete Chat History",
                    fontSize = 16.sp, // Adjust font size if needed
                    modifier = Modifier.align(Alignment.CenterVertically) // Ensure text is vertically centered
                )
            }
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if (messageList.isEmpty()) {
        // Show a placeholder when no messages
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Icon",
                tint = Purple80,
            )
            Text(text = "Ask me anything", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        // Scrollable list of messages
        LazyColumn(
            modifier = modifier,
            reverseLayout = true // Makes the messages appear from bottom to top
        ) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 16.dp else 60.dp,
                        end = if (isModel) 60.dp else 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clip(RoundedCornerShape(16.dp)) // Rounded corners for message box
                    .background(if (isModel) ColorModelMessage else ColorUserMessage)
                    .padding(16.dp)
            ) {
                when (messageModel.type) {
                    "image" -> {
                        Image(
                            painter = rememberAsyncImagePainter(model = messageModel.message),
                            contentDescription = "Image",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    "video" -> {
                        Text(text = "Video Message - Placeholder")
                    }
                    else -> {
                        SelectionContainer {
                            Text(
                                text = messageModel.message,
                                fontWeight = FontWeight.W500,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit) {
    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it
            },
            placeholder = { Text("Type your message here...", fontSize = 14.sp) },
            textStyle = TextStyle(fontSize = 16.sp),
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF8A2BE2),
                unfocusedBorderColor = Color.Gray
            )
        )
        IconButton(onClick = {
            if (message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Reduced header height
            .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)) // Rounded corners for header
            .background(Color(0xFF8A2BE2)) // Violet background color (hex: #8A2BE2)
            .padding(horizontal = 12.dp) // Padding for better layout
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Basha ChatGPT",
            color = Color.White,
            fontSize = 24.sp, // Reduced font size for the header
            fontWeight = FontWeight.Bold
        )
    }
}
