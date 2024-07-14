package com.focusmate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focusmate.ui.viewModel.ChatViewModel
import com.focusmate.ui.viewModel.Message

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        TopAppBar(
            title = {
                Text(
                    text = "SerenityBot",
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            backgroundColor = Color(0xFF6200EE),
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            reverseLayout = true
        ) {
            items(messages.size) { index ->
                val message = messages[messages.size - 1 - index]
                MessageItem(message)
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text(text = "Escribe un mensaje...") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color(0xFF6200EE),
                    unfocusedBorderColor = Color.LightGray
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 16.sp)
            )
            Button(
                onClick = {
                    if (textState.text.isNotEmpty()) {
                        viewModel.sendMessage(textState.text)
                        textState = TextFieldValue("")
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE)),
                modifier = Modifier.height(54.dp)
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message.content,
            modifier = Modifier
                .background(
                    if (message.isUser) Color(0xFF6200EE) else Color(0xFF333333),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp),
            color = Color.White,
            style = LocalTextStyle.current.copy(fontSize = 16.sp)
        )
    }
}