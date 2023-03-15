package com.example.pasapalabra

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        game = Game(cargarPreguntas())

        // Configurar los elementos de la interfaz de usuario
        val questionTextView: TextView = findViewById(R.id.questionTextView)
        val answerEditText: EditText = findViewById(R.id.answerEditText)
        val submitButton: Button = findViewById(R.id.submitButton)
        val pasapalabraButton: Button = findViewById(R.id.pasapalabraButton)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)

        // Función para mostrar una pregunta en la interfaz de usuario
        fun showNextQuestion() {
            val pasapalabra = game.getPasapalabra()
            currentLetter = if (pasapalabra.isNotEmpty()) {
                pasapalabra.first()
            } else {
                game.getAciertos().toString()
            }

            println("Pasapalabra: $pasapalabra") // Añade esta línea

            if (currentLetter.isEmpty() || currentLetter == "0") {
                // No hay más preguntas
                scoreTextView.text = "Fin del juego. Aciertos: ${game.getAciertos()}/${game.getRespuestasIntentadas()}"
                submitButton.isEnabled = false
                pasapalabraButton.isEnabled = false
            } else {
                val question = game.getPregunta(currentLetter)
                println("Mostrando pregunta: ${question.pregunta}")
                questionTextView.text = "Pregunta para la letra '$currentLetter': ${question.pregunta}"
            }
        }




// Manejar el botón de envío
        submitButton.setOnClickListener {
            val userAnswer = answerEditText.text.toString()
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

    private fun cargarPreguntas(): HashMap<String, List<Question>> {
        return try {
            val reader = InputStreamReader(assets.open("preguntas.json"))
            val gson = Gson()
            val type = object : TypeToken<QuestionsWrapper>() {}.type
            val wrapper: QuestionsWrapper = gson.fromJson(reader, type)
            println("Preguntas cargadas: ${wrapper.preguntas}") // Añade esta línea
            wrapper.preguntas
        } catch (e: IOException) {
            e.printStackTrace()
            hashMapOf()
        }
    }
}
