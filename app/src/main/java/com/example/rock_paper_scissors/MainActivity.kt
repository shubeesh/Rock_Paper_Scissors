package com.example.rockpaperscissors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RpsApp()
        }
    }
}

@Composable
fun RpsApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RpsGame()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RpsAppPreview() {
    RpsApp()
}

enum class Choice {
    ROCK, PAPER, SCISSORS
}

enum class RoundResult {
    PLAYER_WIN, COMPUTER_WIN, DRAW
}

data class RpsState(
    val playerScore: Int = 0, //playerScore: Int, default value 0
    val computerScore: Int = 0, // computerScore: Int, default value 0
    val lastPlayerChoice: Choice? = null, // lastPlayerChoice: Choice?, default value null
    val lastComputerChoice: Choice? = null, // lastComputerChoice: Choice?, default value nul
    val lastResult: RoundResult? = null
)

// --- UI ---

@Composable
fun RpsGame() {
    var state by remember { mutableStateOf(RpsState()) } //  Create a mutable state variable named "state" of type RpsState.
    //       It should be remembered across recompositions using remember + mutableStateOf.
    //       This will store all current game information (scores, last choices, etc.).

    val statusText = when (state.lastResult){
        RoundResult.PLAYER_WIN -> "You won the last round! 🎉"
        RoundResult.COMPUTER_WIN -> "Computer won the last round! 🤖"
        RoundResult.DRAW ->  "Last round was a draw. 😐"
        null -> "Choose Rock, Paper, or Scissors to start!"
    }
    // Create a String named "statusText" describing the last round.
    //       It should depend on state.lastResult:
    //         • If PLAYER_WIN  → "You won the last round! 🎉"
    //         • If COMPUTER_WIN → "Computer won the last round! 🤖"
    //         • If DRAW        → "Last round was a draw. 😐"
    //         • If null        → "Choose Rock, Paper, or Scissors to start!"
    //       A when-expression on state.lastResult is a good way to do this.

    Box(
        modifier = Modifier.fillMaxSize(), // Make this Box fill the whole screen and use a dark background color.
        contentAlignment = Alignment.Center // Keep the Column centered inside the Box.
    ) {
        Column(
            modifier = Modifier.padding(), //  Make this Column as wide as the screen and add some padding around it
            horizontalAlignment = Alignment.CenterHorizontally //  Center the children of this Column horizontally.

        ) {

            Text(
                text = "Rock - Paper - Scissor", // Set the title text to something like "Rock – Paper – Scissors".
                color = Color.White, // Use a white color for the text.
                fontSize = 28.sp, // Use a large font size (around 28.sp).
                fontWeight = FontWeight.Bold,//Use a bold font weight for the title.
                modifier = Modifier.padding(3.dp) // Add some space below the title (bottom padding).
            )


            Text(
                text = "who won the last round or what to do next", // Status Text (who won last round or what to do next), // Display the statusText string you computed above.
                color = Color.White, //  Use white text here.
                fontSize = 18.sp, //  Use a medium font size (around 18.sp).
                modifier = Modifier.padding(3.dp) // Add some bottom padding to separate it from the next section.
            )

            // Display last choices
            LastChoicesDisplay(
                playerChoice = state.lastPlayerChoice, //  Pass the last player choice from the current state.
                computerChoice = state.lastComputerChoice // Pass the last computer choice from the current state.
            )

            Spacer(
                modifier = Modifier.height(16.dp) //Add some vertical space between the choices display and the buttons
            )

            // Buttons for Rock / Paper / Scissors
            ChoiceButtons(
                onChoiceSelected = { choice ->
                    state = handlePlayerChoice(state, choice)
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp) //  Add some vertical space between the choices display and the buttons
            )

            // Buttons for Rock / Paper / Scissors
            ChoiceButtons(
                onChoiceSelected = { choice ->
                    state = handlePlayerChoice(state, choice)
                }
            )

            Spacer(

                modifier = Modifier.height(16.dp)  //  Add more vertical space between the buttons and the scoreboard.
            )

            // Scoreboard
            ScoreBoard(
                playerScore = state.playerScore, //  Pass the player's score from the state.
                computerScore = state.computerScore // //  Pass the computer's score from the state.
            )

            Spacer(
                modifier = Modifier.height(8.dp) // Add a small vertical gap before the reset button.
            )

            // Reset button
            Button(
                onClick = {
                    state = RpsState() // Reset the game by assigning a brand new RpsState() to your "state" variable. This should clear scores and last-round information.
                }
            ) {
                Text(
                    text = "Reset Scores" // Give this button a label, for example: "Reset Scores".
                )
            }
        }
    }
}

@Composable
fun LastChoicesDisplay(
    playerChoice: Choice?,
    computerChoice: Choice?
) {
    val playerText = "You: ${choiceLabel(playerChoice)}" //playerText should look like: "You: " followed by the label for playerChoice.
    val computerText = "Computer: ${choiceLabel(computerChoice)}" //computerText should look like: "Computer: " followed by the label for computerChoice.

    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Keep both texts centered horizontally inside this Column.
    ) {
        Text(
            text = playerText, //  Display playerText here
            color = Color.White, // Use a white text color.
            fontSize = 16.sp //Use a reasonable font size (around 16.sp).
        )
        Text(
            text = computerText, //  Display computerText here.
            color = Color.White, //  Use a white text color.
            fontSize = 16.sp //  Use a similar font size to the one above.
        )
    }
}

@Composable
fun ChoiceButtons(
    onChoiceSelected: (Choice) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally // Center the Row of buttons horizontally.
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp) //Add some horizontal spacing between the three buttons
        ) {
            // Rock button
            Button(
                onClick = {
                    onChoiceSelected(Choice.ROCK) //When this button is tapped, call onChoiceSelected
                }
            ) {
                Text(
                    text = "🪨 Rock" //Give this button a label, e.g. "🪨 Rock".
                )
            }

            // Paper button
            Button(
                onClick = {
                    onChoiceSelected(Choice.PAPER) //  When this button is tapped, call onChoiceSelected
                }
            ) {
                Text(
                    text = "📄 Paper" //Give this button a label, e.g. "📄 Paper".
                )
            }

            // Scissors button
            Button(
                onClick = {
                    onChoiceSelected(Choice.SCISSORS) // When this button is tapped, call onChoiceSelected
                }
            ) {
                Text(
                    text = "✂️ Scissors" //  Give this button a label, e.g. "✂️ Scissors".
                )
            }
        }
    }
}

@Composable
fun ScoreBoard(
    playerScore: Int,
    computerScore: Int
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(), // Add a little top padding above the scores so they don’t touch

        horizontalArrangement = Arrangement.SpaceEvenly //  Add horizontal spacing between the “player” column and “computer” column.
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Score", // Label this column, e.g. "Your Score".
                color = Color.White, // Use white text with a smaller size (around 16.sp).
                fontSize = 16.sp
            )
            Text(
                text = playerScore.toString(), //  Display the player's score value here (convert playerScore to a String)
                color = Color.White, //  Use white text with a larger size (around 22.sp).
                fontSize = 22.sp, // Use a bold font weight to emphasize the score.
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Computer Score", //Label this column, e.g. "Computer Score".
                color = Color.White, // Use white text with a smaller size (around 16.sp).
                fontSize = 16.sp
            )
            Text(
                text = computerScore.toString(), // Display the computer's score value here (convert computerScore to a String).
                color = Color.White, // Use white text with a larger size (around 22.sp).
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold // Use a bold font weight here as well
            )
        }
    }
}

// Helper for nice display text.
fun choiceLabel(choice: Choice?): String =
    when (choice) {
        Choice.ROCK -> "Rock 🪨"
        Choice.PAPER -> "Paper 📄"
        Choice.SCISSORS -> "Scissors ✂️"
        null -> "—"
    }

// --- Game Logic ---


fun handlePlayerChoice(oldState: RpsState, playerChoice: Choice): RpsState {
    val computerChoice = randomComputerChoice()
    val result = determineRoundResult(playerChoice, computerChoice)

    var newPlayerScore = oldState.playerScore
    var newComputerScore = oldState.computerScore

    when (result) {
        RoundResult.PLAYER_WIN -> newPlayerScore += 1
        RoundResult.COMPUTER_WIN -> newComputerScore += 1
        RoundResult.DRAW -> {
            /* no score change */
        }
        null -> { /* won't happen, keep scores */ }
    }

    return oldState.copy(
        playerScore = newPlayerScore,
        computerScore = newComputerScore,
        lastPlayerChoice = playerChoice,
        lastComputerChoice = computerChoice,
        lastResult = result
    )
}



fun randomComputerChoice(): Choice {
    val values = Choice.values()
    return values[Random.nextInt(values.size)] //Get all possible values and pick one by index with Random.nextInt(...)
}


fun determineRoundResult(player: Choice, computer: Choice): RoundResult {
    if (player == computer) return RoundResult.DRAW

    return when (player) {
        Choice.ROCK -> if (computer == Choice.SCISSORS) RoundResult.PLAYER_WIN else RoundResult.COMPUTER_WIN
        Choice.PAPER -> if (computer == Choice.ROCK) RoundResult.PLAYER_WIN else RoundResult.COMPUTER_WIN
        Choice.SCISSORS -> if (computer == Choice.PAPER) RoundResult.PLAYER_WIN else RoundResult.COMPUTER_WIN
    }
}