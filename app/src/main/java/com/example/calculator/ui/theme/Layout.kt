package com.example.calculator.ui.theme
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Stack


@Composable
fun ButtonModel(
    modifier: Modifier = Modifier,
    label: String,
    action: () -> Unit,
    color: Color,
    size: FontSize
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        modifier = modifier
            .background(Color.Black)
            .border(0.1.dp, Color(43,43,43))
    ) {
        Text(text = "$label",
            color = color,
            fontSize = size.value)
    }
}

enum class FontSize(val value: TextUnit) {
    SMALL(20.sp),
    NORMAL(30.sp),
    BIG(40.sp)
}

@Composable
fun layout () {
    var inputString by remember {
        mutableStateOf("")
    }

    val buttons = listOf(
        ButtonInstance("AC", { inputString = ""}, ButtonColor.ACTION.value, FontSize.SMALL),
        ButtonInstance("←", { inputString = inputString.dropLast(1)}, ButtonColor.ACTION.value, FontSize.BIG),
        ButtonInstance("%", {}, ButtonColor.ACTION.value, FontSize.NORMAL),
        ButtonInstance("÷", {
            try{
                if(isLastOperator(inputString)) inputString = inputString.dropLast(1)
                if(inputString.isNotEmpty()) inputString += "÷"
            } catch (e: Exception) {
                println("Ошибка: ${e.message}")
            }

                            }, ButtonColor.ACTION.value, FontSize.NORMAL),
        ButtonInstance("7", { inputString += "7" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("8", { inputString += "8" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("9", { inputString += "9" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("×", {
            if(isLastOperator(inputString)) inputString = inputString.dropLast(1)
            if(inputString.isNotEmpty()) inputString += "×"
                            }, ButtonColor.ACTION.value, FontSize.NORMAL),
        ButtonInstance("4", { inputString += "4" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("5", { inputString += "5" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("6", { inputString += "6" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("—", {
            if(isLastOperator(inputString)) inputString = inputString.dropLast(1)
            if(inputString.isNotEmpty()) inputString += "—"
                            }, ButtonColor.ACTION.value, FontSize.NORMAL),
        ButtonInstance("1", { inputString += "1" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("2", { inputString += "2" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("3", { inputString += "3" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("+", {
            if(isLastOperator(inputString)) inputString = inputString.dropLast(1)
            if(inputString.isNotEmpty()) inputString += "+"
                            }, ButtonColor.ACTION.value, FontSize.NORMAL),
        ButtonInstance(" ", { inputString = "Не используй калькулятор, баран"}, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("0", { inputString += "0" }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance(".", { inputString += "." }, ButtonColor.NUMBER.value, FontSize.NORMAL),
        ButtonInstance("=", { inputString = solve(inputString).toString()}, ButtonColor.ACTION.value, FontSize.NORMAL),
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

fun isLastOperator(str: String): Boolean {
    if( str.isNotEmpty() && Operation.makeOperator(str[str.length-1]).value != null ) return true
    else return false
}


class ButtonInstance (
    val label: String,
    val action: () -> Unit,
    val color: Color,
    val size: FontSize
)

enum class ButtonColor (val value: Color) {
    ACTION(Color(235,76,66)),
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
                color = buttons[i].color,
                size = buttons[i].size)
        }
    }
}

fun solve(input: String): Number {
    val regex = Regex("([—+×÷])|([0-9]+\\.?[0-9]*)")
    val numberStack = Stack<Number>()
    val operatorStack = Stack<Operation>()
    var lastWasOperator = false

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
                lastWasOperator = true
            } else {
                // Добавляем число в стек
                if (value.contains(".")) {
                    numberStack.push(value.toFloat())
                } else {
                    numberStack.push(value.toInt())
                }
                lastWasOperator = false
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
    var value: Char? = ' '
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
                else -> result.value = null
            }
            return result
        }
    }
}