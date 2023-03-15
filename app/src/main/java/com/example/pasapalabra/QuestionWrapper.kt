package com.example.pasapalabra

import com.google.gson.annotations.SerializedName

data class QuestionsWrapper(
    @SerializedName("preguntas")
    val preguntas: HashMap<String, List<Question>>
)

