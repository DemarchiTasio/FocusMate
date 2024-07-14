package com.focusmate.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyCJJ_2iBv9yXjnMKpVHL2bQGotdQd6uD80"
    )

    fun sendMessage(content: String) {
        val userMessage = Message(content, isUser = true)
        _messages.value = _messages.value + userMessage

        viewModelScope.launch {
            val prompt = """
                Eres un psicologo profesional, integrado en una aplicacion. Tienes el fin de ayudar a
                los usuarios con sus estados emocionales, indentificando por medio de preguntas (de manera amigable)
                su situacion emocional actual. Una vez que tengas la nocion de su estado podes darle recomendaciones de actividades (meditacion/guias de respiracion
                actividades fisicas o de oscio) segun su caso.
                
                Debes llevar una charla sutil y amigable, como si fueran dos amigos charlando.
                En el caso de dar recomendaciones de meditacion o guias de respiracion aclarar que puede buscar algunas dentro de esta aplicacion.
                Saluda unicamente si el usuario saluda.

                Mensaje del usuario: "$content"
                                     
                Respuesta:
            """.trimIndent()

            val response = model.generateContent(prompt)
            response?.text?.let {
                val botMessage = Message(it.trim(), isUser = false)
                _messages.value = _messages.value + botMessage
            }
        }
    }
}

data class Message(
    val content: String,
    val isUser: Boolean
)