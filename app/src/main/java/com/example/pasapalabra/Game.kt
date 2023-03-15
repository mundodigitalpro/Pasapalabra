package com.example.pasapalabra

class Game(private val preguntas: HashMap<String, List<Question>>) {
    private val pasapalabra = preguntas.keys.toMutableSet()
    private val respuestas = mutableMapOf<String, Boolean>()
    private var respuestasIntentadas = 0


    init {
        pasapalabra.addAll(preguntas.keys)
    }

  /*  fun getPregunta(letra: String): Question? {
        if (letra.length != 1 || !letra[0].isLetter()) {
            throw IllegalArgumentException("El caracter '$letra' no es una letra válida")
        }
        return preguntas[letra]?.random()
    }*/

    fun getPregunta(letra: String): Question? {
        return preguntas[letra]?.random()
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

    fun reset() {
        pasapalabra.clear()
        pasapalabra.addAll(preguntas.keys)
        respuestas.clear()
    }

    fun incrementarRespuestasIntentadas() {

        respuestasIntentadas++
    }
}
