package com.example.a11124_game2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private var funds = 1000000
    private var totalGames = 0
    private var wins = 0
    private var losses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val gameStatsTextView = findViewById<TextView>(R.id.gameStatsTextView)
        val backButton = findViewById<Button>(R.id.backButton)

        val dotButtons = listOf(
            findViewById<Button>(R.id.dot1),
            findViewById<Button>(R.id.dot2),
            findViewById<Button>(R.id.dot3)
        )

        // Randomly select a hidden dot
        val hiddenDotIndex = (0..2).random()

        // Set button click listeners
        dotButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (funds > 0) {
                    totalGames++
                    if (index == hiddenDotIndex) {
                        funds += 30000 // Example win amount
                        wins++
                        resultTextView.text = "You Win!"
                    } else {
                        funds -= 10000 // Example loss amount
                        losses++
                        resultTextView.text = "You Lost!"
                    }

                    // Update the game statistics
                    val winRate = if (totalGames > 0) (wins * 100) / totalGames else 0
                    gameStatsTextView.text = """
                        게임 횟수: ${totalGames}회
                        이긴 횟수: ${wins}회
                        실패한 횟수: ${losses}회
                        승률: $winRate%
                        현재 자금: ${funds.formatAsCurrency()}원
                    """.trimIndent()

                    // Check for game over
                    if (funds <= 0) {
                        resultTextView.text = "Game Over"
                        disableAllButtons(dotButtons)
                    }

                    // Reveal the hidden dot
                    revealHiddenDot(dotButtons, hiddenDotIndex)
                }
            }
        }

        backButton.setOnClickListener {
            finish() // Return to previous activity
        }
    }

    private fun revealHiddenDot(buttons: List<Button>, hiddenDotIndex: Int) {
        buttons.forEachIndexed { index, button ->
            button.backgroundTintList = if (index == hiddenDotIndex) {
                getColorStateList(android.R.color.black)
            } else {
                getColorStateList(android.R.color.darker_gray)
            }
        }
    }

    private fun disableAllButtons(buttons: List<Button>) {
        buttons.forEach { it.isEnabled = false }
    }

    private fun Int.formatAsCurrency(): String {
        return "%,d".format(this)
    }
}


