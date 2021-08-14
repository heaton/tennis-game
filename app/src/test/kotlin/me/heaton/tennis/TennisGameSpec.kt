package me.heaton.tennis

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class TennisGameSpec : WordSpec({

    isolationMode = IsolationMode.InstancePerTest

    "A Tennis Game" should {
        val game = TennisGame("A", "B")

        "start from 0:0" {
            game.score() shouldBe "love, love"
        }

        "show 15, love if player A scores" {
            game.firstPlayerScores()
            game.score() shouldBe "15, love"
        }

        "show 30, love if player A scores twice" {
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.score() shouldBe "30, love"
        }

        "show 40, love if player A scores 3 times" {
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.score() shouldBe "40, love"
        }

        "show A wins if player A scores 4 times continuously" {
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.score() shouldBe "A wins"
        }

        "show love, 15 if player B scores" {
            game.secondPlayerScores()
            game.score() shouldBe "love, 15"
            game.secondPlayerScores()
            game.score() shouldBe "love, 30"
        }

        "show B wins if player B scores 4 times continuously" {
            game.secondPlayerScores()
            game.secondPlayerScores()
            game.secondPlayerScores()
            game.secondPlayerScores()
            game.score() shouldBe "B wins"
        }

        "show deuce if both win 3 times" {
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.score() shouldBe "30, 30"
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.score() shouldBe "deuce"
        }

        fun makeADeuce() {
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.firstPlayerScores()
            game.secondPlayerScores()
        }

        "show advantage A if player A scores after a deuce" {
            makeADeuce()
            game.firstPlayerScores()
            game.score() shouldBe "advantage A"
        }

        "show advantage B if player B scores after a deuce" {
            makeADeuce()
            game.secondPlayerScores()
            game.score() shouldBe "advantage B"
        }

        "back to deuce if both scores after a deuce" {
            makeADeuce()
            game.firstPlayerScores()
            game.secondPlayerScores()
            game.score() shouldBe "deuce"
        }

        "show A wins if A scores after advantage A" {
            makeADeuce()
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.score() shouldBe "A wins"
        }

        "show B wins if B scores after advantage B with many deuces" {
            makeADeuce()
            makeADeuce()
            game.secondPlayerScores()
            game.secondPlayerScores()
            game.score() shouldBe "B wins"
        }

        "throw exception for scores if game has end" {
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.firstPlayerScores()
            game.firstPlayerScores()
            shouldThrow<GameEndedException> {
                game.firstPlayerScores()
            }.message shouldBe "the game has ended"
            shouldThrow<GameEndedException> {
                game.secondPlayerScores()
            }.message shouldBe "the game has ended"
        }
    }
})
