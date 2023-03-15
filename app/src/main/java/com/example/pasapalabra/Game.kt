package com.example.pasapalabra

class Game(private val preguntas: HashMap<String, List<Question>>) {
    private val pasapalabra = preguntas.keys.toMutableSet()
    private val respuestas = mutableMapOf<String, Boolean>()

    init {
        pasapalabra.addAll(preguntas.keys)
    }

    fun getPregunta(letra: String): Question {
        return preguntas[letra]?.random() ?: throw IllegalArgumentException("No hay preguntas para la letra '$letra'")
    }

    fun responder(letra: String, respuesta: String): Boolean {
        val correcto = preguntas[letra]?.any { it.respuesta.equals(respuesta, ignoreCase = true) } ?: false
        respuestas[letra] = correcto
        if (!correcto) {
            pasapalabra.add(letra)
        } else {
            pasapalabra.remove(letra)
        }
        return correcto
    }

    fun getPasapalabra(): Set<String> = pasapalabra

    fun getAciertos(): Int = respuestas.values.count { it }

    fun getRespuestasIntentadas(): Int = respuestas.size
}
