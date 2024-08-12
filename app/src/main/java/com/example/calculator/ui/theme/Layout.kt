package com.example.calculator.ui.theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ButtonModel(
    modifier: Modifier = Modifier,
    label: String,
    action: () -> Unit,
    color: Color
) {
    Button(
        onClick = action,
        modifier = modifier
            .background(Color.Black)
    ) {
        Text(text = "$label",
            color = color)
    }
}

@Composable
fun layout () {
    val buttons = listOf(
        ButtonInstance("AC", {}, ButtonColor.ACTION.value),
        ButtonInstance("<-", {}, ButtonColor.ACTION.value),
        ButtonInstance("%", {}, ButtonColor.ACTION.value),
        ButtonInstance("/", {}, ButtonColor.ACTION.value),
        ButtonInstance("7", {}, ButtonColor.NUMBER.value),
        ButtonInstance("8", {}, ButtonColor.NUMBER.value),
        ButtonInstance("9", {}, ButtonColor.NUMBER.value),
        ButtonInstance("x", {}, ButtonColor.ACTION.value),
        ButtonInstance("4", {}, ButtonColor.NUMBER.value),
        ButtonInstance("5", {}, ButtonColor.NUMBER.value),
        ButtonInstance("6", {}, ButtonColor.NUMBER.value),
        ButtonInstance("-", {}, ButtonColor.ACTION.value),
        ButtonInstance("1", {}, ButtonColor.NUMBER.value),
        ButtonInstance("2", {}, ButtonColor.NUMBER.value),
        ButtonInstance("3", {}, ButtonColor.NUMBER.value),
        ButtonInstance("+", {}, ButtonColor.ACTION.value),
        ButtonInstance(" ", {}, ButtonColor.NUMBER.value),
        ButtonInstance("0", {}, ButtonColor.NUMBER.value),
        ButtonInstance(".", {}, ButtonColor.NUMBER.value),
        ButtonInstance("=", {}, ButtonColor.ACTION.value),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black)
        )
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            ButtonsRow(range = (0..3), buttons = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ButtonsRow(range = (4..7), buttons = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ButtonsRow(range = (8..11), buttons = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ButtonsRow(range = (12..15), buttons = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            ButtonsRow(range = (16..19), buttons = buttons,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

class ButtonInstance (
    val label: String,
    val action: () -> Unit,
    val color: Color
)

enum class ButtonColor (val value: Color) {
    ACTION(Color.Yellow),
    NUMBER(Color.White)
}

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    range: IntRange,
    buttons: List<ButtonInstance>
) {
    Row(
        modifier = modifier
    ) {
        for (i in range) {
            ButtonModel(
                label = buttons[i].label,
                action = buttons[i].action,
                color = buttons[i].color)
        }
    }
}