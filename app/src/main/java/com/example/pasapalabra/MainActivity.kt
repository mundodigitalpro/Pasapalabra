package com.example.pasapalabra

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var game: Game
    private lateinit var currentLetter: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar las preguntas del archivo JSON
        game = Game(cargarPreguntas())

        // Configurar los elementos de la interfaz de usuario
        val questionTextView: TextView = findViewById(R.id.questionTextView)
        val answerEditText: EditText = findViewById(R.id.answerEditText)
        val submitButton: Button = findViewById(R.id.submitButton)
        val pasapalabraButton: Button = findViewById(R.id.pasapalabraButton)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)

        // Función para mostrar una pregunta en la interfaz de usuario
        fun showNextQuestion() {
            // Obtener el siguiente conjunto de preguntas
            val pasapalabra = game.getPasapalabra()
            currentLetter = if (pasapalabra.isNotEmpty()) {
                pasapalabra.first()
            } else {
                game.getAciertos().toString()
            }

            //println("Pasapalabra: $pasapalabra")

            // Si no hay más preguntas, terminar el juego
            if (currentLetter == "0") {
                // No hay más preguntas
                scoreTextView.text = "Fin del juego. Aciertos: ${game.getAciertos()}/${game.getRespuestasIntentadas()}"
                submitButton.isEnabled = false
                pasapalabraButton.isEnabled = false

            } else {
                // Mostrar la pregunta para la letra actual
                val question = game.getPregunta(currentLetter)
                if (question == null) {
                    /// Si no hay preguntas disponibles para la letra actual, informar al usuario y comenzar una nueva ronda
                    AlertDialog.Builder(this)
                        .setTitle("Ronda terminada")
                        .setMessage("La ronda ha terminado. ¿Quieres empezar una nueva ronda?")
                        .setPositiveButton("Sí") { _, _ ->
                            game.reset()
                            showNextQuestion()
                        }
                        .setNegativeButton("No") { _, _ ->
                            // Opcional: puedes finalizar la actividad aquí si no quieres que el usuario continúe
                        }
                        .show()
                } else {
                    println("Mostrando pregunta: ${question.pregunta}")
                    questionTextView.text = "Con la letra '$currentLetter':\n\n${question.pregunta}"
                }
            }
        }

        // Manejar el botón de envío
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString().trim()
            if (userAnswer.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa una respuesta.", Toast.LENGTH_SHORT).show()
            } else {
                val correct = game.responder(currentLetter, userAnswer)
                if (correct) {
                    Toast.makeText(this, "Correcto!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Incorrecto!", Toast.LENGTH_SHORT).show()
                }
                answerEditText.text.clear()
                showNextQuestion()
                scoreTextView.text = "Aciertos: ${game.getAciertos()}/${game.getRespuestasIntentadas()}"
            }
        }



        // Manejar el botón Pasapalabra
        pasapalabraButton.setOnClickListener {
            game.responder(currentLetter, "")
            answerEditText.text.clear()
            showNextQuestion()
            scoreTextView.text = "Aciertos: ${game.getAciertos()}/${game.getRespuestasIntentadas()}"
        }


        // Comenzar el juego mostrando la primera pregunta
        showNextQuestion()
    }

    // Función para cargar las preguntas del archivo JSON
    private fun cargarPreguntas(): HashMap<String, List<Question>> {
        return try {
            // Leer el archivo JSON desde los recursos de la aplicación
            val reader = InputStreamReader(assets.open("preguntas.json"))
            // Crear un objeto Gson para deserializar el JSON
            val gson = Gson()
            // Especificar el tipo de objeto que se va a deserializar
            val type = object : TypeToken<QuestionsWrapper>() {}.type
            // Deserializar el JSON en un objeto QuestionsWrapper
            val wrapper: QuestionsWrapper = gson.fromJson(reader, type)
            //println("Preguntas cargadas: ${wrapper.preguntas}") // Añade esta línea
            // Devolver las preguntas cargadas
            wrapper.preguntas
        } catch (e: IOException) {
            // Si hay algún error al cargar el archivo, imprimir la traza de error y devolver un HashMap vacío
            e.printStackTrace()
            hashMapOf()
        }
    }
}
