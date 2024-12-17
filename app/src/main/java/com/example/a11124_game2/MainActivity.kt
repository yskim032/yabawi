package com.example.a11124_game2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {

    private var funds = 1000000
    private var totalGames = 0
    private var wins = 0
    private var losses = 0
    private var currentBet = 10000 // Default bet amount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Google AdMob
        MobileAds.initialize(this) {}

        val mAdView:AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        //Google AdMob

        val gameStatsTextView = findViewById<TextView>(R.id.gameStatsTextView)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val betAmountEditText = findViewById<EditText>(R.id.betAmountEditText)
        val setBetButton = findViewById<Button>(R.id.setBetButton)
        val record = findViewById<Button>(R.id.record)

        //scoring record page
        record.setOnClickListener{

            setContentView(R.layout.activity_record)
            val textView2 = findViewById<TextView>(R.id.textView2)
            textView2.text = "now in new page"


        }

        val dotButtons = listOf(
            findViewById<Button>(R.id.dot1),
            findViewById<Button>(R.id.dot2),
            findViewById<Button>(R.id.dot3)
        )

        updateGameStats(gameStatsTextView)

        // Set new bet amount
        setBetButton.setOnClickListener {
            val newBet = betAmountEditText.text.toString().toIntOrNull()
            if (newBet != null && newBet > 0) {
                currentBet = newBet
                Toast.makeText(this, "Bet amount set to ${currentBet} Won.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            }
        }

        // Hidden dot selection
        var hiddenDotIndex = (0..2).random()

        dotButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (funds > 0) {
                    totalGames++

                    if (index == hiddenDotIndex) {
                        funds += currentBet * 2
                        wins++
                        resultTextView.text = "You Win!"
                    } else {
                        funds -= currentBet
                        losses++
                        resultTextView.text = "You Lost!"
                    }

                    revealHiddenDot(dotButtons, hiddenDotIndex)
                    updateGameStats(gameStatsTextView)

                    if (funds <= 0) {
                        resultTextView.text = "Game Over"
                        disableAllButtons(dotButtons)
                    } else {
                        // Prepare for the next round
                        resultTextView.postDelayed({
                            startNewRound(dotButtons, resultTextView)
                            hiddenDotIndex = (0..2).random()
                        }, 1500) // Delay to show the result before resetting
                    }
                }
            }
        }

        resetButton.setOnClickListener {
            resetGame(dotButtons, resultTextView, gameStatsTextView)
        }
    }

    private fun updateGameStats(gameStatsTextView: TextView) {
        val winRate = if (totalGames > 0) (wins * 100) / totalGames else 0
        gameStatsTextView.text = """
            Total Games: ${totalGames}
            Wins: ${wins}
            Losses: ${losses}
            Win Rate: $winRate%
            Current Balance: ${funds.formatAsCurrency()} Won
        """.trimIndent()
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

    private fun resetDots(buttons: List<Button>) {
        buttons.forEach { it.backgroundTintList = getColorStateList(android.R.color.darker_gray) }
    }

    private fun resetGame(
        dotButtons: List<Button>,
        resultTextView: TextView,
        gameStatsTextView: TextView
    ) {
        funds = 1000000
        totalGames = 0
        wins = 0
        losses = 0
        currentBet = 10000

        resultTextView.text = "Select a dot!"
        dotButtons.forEach { it.isEnabled = true }
        resetDots(dotButtons)
        updateGameStats(gameStatsTextView)
    }

    private fun startNewRound(dotButtons: List<Button>, resultTextView: TextView) {
        resetDots(dotButtons)
        resultTextView.text = "New round. Please select a dot."
    }

    private fun Int.formatAsCurrency(): String {
        return "%,d".format(this)
    }
}
//update//