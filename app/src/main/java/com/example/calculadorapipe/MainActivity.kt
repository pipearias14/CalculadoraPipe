package com.example.calculadorapipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraSencilla()
        }
    }
}

@Composable
fun CalculadoraSencilla() {
    var input by remember { mutableStateOf("") }

    fun calcularResultado(): String {
        return try {
            val tokens = StringTokenizer(input, "+-*/", true)
            val valores = mutableListOf<Double>()
            val operadores = mutableListOf<String>()

            while (tokens.hasMoreTokens()) {
                val token = tokens.nextToken().trim()
                if (token.isEmpty()) continue
                when (token) {
                    "+", "-", "*", "/" -> operadores.add(token)
                    else -> valores.add(token.toDoubleOrNull() ?: return "Error")
                }
            }

            var resultado = valores[0]
            for (i in operadores.indices) {
                val op = operadores[i]
                val num = valores.getOrNull(i + 1) ?: return "Error"
                resultado = when (op) {
                    "+" -> resultado + num
                    "-" -> resultado - num
                    "*" -> resultado * num
                    "/" -> if (num != 0.0) resultado / num else return "Error"
                    else -> return "Error"
                }
            }

            resultado.toString()
        } catch (e: Exception) {
            "Error"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = input.ifBlank { "0" },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            color = Color.Black,
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                listOf("C", "/", "*", "DEL"),
                listOf("7", "8", "9", "-"),
                listOf("4", "5", "6", "+"),
                listOf("1", "2", "3", "="),
                listOf("0", ".")
            ).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { text ->
                        BotonSencillo(text, Modifier.weight(1f)) {
                            when (text) {
                                "C" -> input = ""
                                "DEL" -> if (input.isNotEmpty()) input = input.dropLast(1)
                                "=" -> input = calcularResultado()
                                else -> input += text
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BotonSencillo(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
    ) {
        Text(text = text, fontSize = 20.sp, color = Color.Black)
    }
}
