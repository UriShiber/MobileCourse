package com.sr.tictactoe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {
    private lateinit var gameBoard: Array<Array<Button>>
    private var currPlayer = "X"
    private var board = Array(3) { Array(3) { "" } }

    @SuppressLint("DiscouragedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        gameBoard = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("cell_${row}_${col}", "id", requireContext().packageName)
                view.findViewById<Button>(buttonId).apply {
                    setOnClickListener { pressCell(this, row, col) }
                }
            }
        }

        view.findViewById<Button>(R.id.replay_button).setOnClickListener {
            resetGame()
        }

        return view
    }

    private fun pressCell(button: Button, row: Int, col: Int) {
        if (board[row][col].isNotEmpty()) return

        board[row][col] = currPlayer
        button.text = currPlayer
        button.setTextColor(if (currPlayer == "X") Color.RED else Color.BLUE)

        if (hasWinner()) {
            view?.findViewById<TextView>(R.id.game_status)?.text = "Player $currPlayer Wins!"
            endGame()
        } else if (hasDraw()) {
            view?.findViewById<TextView>(R.id.game_status)?.text = "It's a Draw!"
            endGame()
        } else {
            currPlayer = if (currPlayer == "X") "O" else "X"
            view?.findViewById<TextView>(R.id.game_status)?.text = "Player $currPlayer's Turn"
        }
    }

    private fun hasWinner(): Boolean {
        var flag = false
        // Row and Cols
        for (i in 0..2) {
            if ((board[i][0] == currPlayer && board[i][1] == currPlayer && board[i][2] == currPlayer) ||
                (board[0][i] == currPlayer && board[1][i] == currPlayer && board[2][i] == currPlayer))
                flag = true
        }

        // Diagonals
        if ((board[0][0] == currPlayer && board[1][1] == currPlayer && board[2][2] == currPlayer) ||
         (board[0][2] == currPlayer && board[1][1] == currPlayer && board[2][0] == currPlayer)) flag = true

        return flag
    }

    private fun hasDraw(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun endGame() {
        gameBoard.flatten().forEach { it.isEnabled = false }
        view?.findViewById<Button>(R.id.replay_button)?.visibility = View.VISIBLE
    }

    private fun resetGame() {
        board = Array(3) { Array(3) { "" } }
        gameBoard.flatten().forEach {
            it.text = ""
            it.isEnabled = true
        }
        currPlayer = "X"
        view?.findViewById<TextView>(R.id.game_status)?.text = "Player X's Turn"
        view?.findViewById<Button>(R.id.replay_button)?.visibility = View.GONE
    }
}