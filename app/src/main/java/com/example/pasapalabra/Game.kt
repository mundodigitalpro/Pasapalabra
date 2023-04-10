package com.example.pasapalabra

// Clase Game que recibe un HashMap con las preguntas
class Game(private val preguntas: HashMap<String, List<Question>>) {

    // Variables privadas para almacenar el estado del juego
    private val pasapalabra = preguntas.keys.toMutableSet()
    private val respuestas = mutableMapOf<String, Boolean>()

    // Inicializar el conjunto de letras para Pasapalabra
    init {
        pasapalabra.addAll(preguntas.keys)
    }

    // Función para obtener una pregunta aleatoria de una letra dada
    fun getPregunta(letra: String): Question? {
        return preguntas[letra]?.random()
    }

    // Función para responder a una pregunta y actualizar el estado del juego
    fun responder(letra: String, respuesta: String): Boolean {
        if (respuesta.isEmpty()) {
            // No se cuenta como acierto ni fallo, pero se registra como un intento
            respuestas[letra] = false
        } else {
            // Comprobar si la respuesta es correcta
            val correcto = preguntas[letra]?.any { it.respuesta.equals(respuesta, ignoreCase = true) } ?: false

            // Almacenar el resultado de la respuesta en el mapa de respuestas
            respuestas[letra] = correcto
            if (!correcto) {
                // Si la respuesta es incorrecta, añadir la letra al conjunto de Pasapalabra
                pasapalabra.add(letra)
            } else {
                // Si la respuesta es correcta, eliminar la letra del conjunto de Pasapalabra
                pasapalabra.remove(letra)
            }
            return correcto
        }
        return false
    }


    // Función para obtener el conjunto de letras de Pasapalabra
    fun getPasapalabra(): Set<String> = pasapalabra

    // Función para obtener el número de aciertos
    fun getAciertos(): Int = respuestas.values.count { it }

    // Función para obtener el número total de respuestas intentadas
    fun getRespuestasIntentadas(): Int = respuestas.size

    // Función para reiniciar el juego
    fun reset() {
        // Limpiar el conjunto de letras de Pasapalabra y el mapa de respuestas
        pasapalabra.clear()
        pasapalabra.addAll(preguntas.keys)
        respuestas.clear()
    }
}
