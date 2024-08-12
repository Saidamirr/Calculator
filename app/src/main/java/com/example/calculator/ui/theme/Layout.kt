package com.example.calculator.ui.theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Stack


@Composable
fun ButtonModel(
    modifier: Modifier = Modifier,
    label: String,
    action: () -> Unit,
    color: Color
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        modifier = modifier
            .background(Color.Black)
    ) {
        Text(text = "$label",
            color = color,
            fontSize = 25.sp)
    }
}

@Composable
fun layout () {
    var inputString by remember {
        mutableStateOf("")
    }

    val buttons = listOf(
        ButtonInstance("AC", { inputString = ""}, ButtonColor.ACTION.value),
        ButtonInstance("←", { inputString = inputString.dropLast(1)}, ButtonColor.ACTION.value),
        ButtonInstance("%", {}, ButtonColor.ACTION.value),
        ButtonInstance("÷", { inputString += "÷" }, ButtonColor.ACTION.value),
        ButtonInstance("7", { inputString += "7" }, ButtonColor.NUMBER.value),
        ButtonInstance("8", { inputString += "8" }, ButtonColor.NUMBER.value),
        ButtonInstance("9", { inputString += "9" }, ButtonColor.NUMBER.value),
        ButtonInstance("×", { inputString += "×" }, ButtonColor.ACTION.value),
        ButtonInstance("4", { inputString += "4" }, ButtonColor.NUMBER.value),
        ButtonInstance("5", { inputString += "5" }, ButtonColor.NUMBER.value),
        ButtonInstance("6", { inputString += "6" }, ButtonColor.NUMBER.value),
        ButtonInstance("—", { inputString += "—" }, ButtonColor.ACTION.value),
        ButtonInstance("1", { inputString += "1" }, ButtonColor.NUMBER.value),
        ButtonInstance("2", { inputString += "2" }, ButtonColor.NUMBER.value),
        ButtonInstance("3", { inputString += "3" }, ButtonColor.NUMBER.value),
        ButtonInstance("+", { inputString += "+" }, ButtonColor.ACTION.value),
        ButtonInstance(" ", {}, ButtonColor.NUMBER.value),
        ButtonInstance("0", { inputString += "0" }, ButtonColor.NUMBER.value),
        ButtonInstance(".", { inputString += "." }, ButtonColor.NUMBER.value),
        ButtonInstance("=", { inputString = solve(inputString).toString()}, ButtonColor.ACTION.value),
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(43, 43, 43))
                .padding(20.dp)
        ) {
            Text(
                text = inputString,
                color = Color.White,
                fontSize = 45.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }
        Column (
            modifier = Modifier
                .weight(1f)
        ) {
            ButtonsRow(range = (0..3), buttons = buttons,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                label = buttons[i].label,
                action = buttons[i].action,
                color = buttons[i].color)
        }
    }
}

fun solve(input: String): Number {
    val regex = Regex("([—+×÷])|([0-9]+\\.?[0-9]*)")
    val numberStack = Stack<Number>()
    val operatorStack = Stack<Operation>()

    val matches = regex.findAll(input)

    try {
        for (match in matches) {
            val value = match.value
            var currentOperator: Operation? = null

            if (value.matches(Regex("[—+×÷]"))) {
                currentOperator = Operation.makeOperator(value[0])

                // Выполнение операций с более высоким или равным приоритетом
                while (operatorStack.isNotEmpty() && operatorStack.peek().priority >= currentOperator.priority) {
                    val operator = operatorStack.pop()
                    val num2 = numberStack.pop().toFloat()
                    val num1 = numberStack.pop().toFloat()
                    numberStack.push(calculate(num1, num2, operator))
                }

                // Добавляем текущий оператор в стек
                operatorStack.push(currentOperator)
            } else {
                // Добавляем число в стек
                if (value.contains(".")) {
                    numberStack.push(value.toFloat())
                } else {
                    numberStack.push(value.toInt())
                }
            }
        }

        // Выполнение оставшихся операций
        while (operatorStack.isNotEmpty()) {
            val operator = operatorStack.pop()
            val num2 = numberStack.pop().toFloat()
            val num1 = numberStack.pop().toFloat()
            numberStack.push(calculate(num1, num2, operator))
        }

    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }

    val result = numberStack.pop()

    return if (result.toFloat() % 1 == 0f) {
        result.toInt()
    } else {
        result
    }
}

fun calculate(num1: Float, num2: Float, operation: Operation): Float {
    return when (operation.value) {
        '+' -> num1 + num2
        '-' -> num1 - num2
        '*' -> num1 * num2
        '/' -> num1 / num2
        else -> 0f
    }
}

class Operation() {
    var value: Char = ' '
        private set
    var priority = 0
        private set

    companion object {
        fun makeOperator(value: Char): Operation {
            val result = Operation()

            when (value) {
                '+' -> {
                    result.value = '+'
                    result.priority = 1
                }
                '—' -> {
                    result.value = '-'
                    result.priority = 1
                }
                '×' -> {
                    result.value = '*'
                    result.priority = 2
                }
                '÷' -> {
                    result.value = '/'
                    result.priority = 2
                }
            }
            return result
        }
    }
}