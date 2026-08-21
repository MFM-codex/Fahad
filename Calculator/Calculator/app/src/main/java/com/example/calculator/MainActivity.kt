package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvExpression: TextView

    // Calculator state
    private var currentInput: String = "0"
    private var pendingOperand: BigDecimal? = null
    private var pendingOperator: String? = null
    private var expressionText: String = ""
    private var justEvaluated: Boolean = false

    private val df = DecimalFormat("#,##0.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvExpression = findViewById(R.id.tvExpression)

        setupNumberButton(R.id.btn0, "0")
        setupNumberButton(R.id.btn1, "1")
        setupNumberButton(R.id.btn2, "2")
        setupNumberButton(R.id.btn3, "3")
        setupNumberButton(R.id.btn4, "4")
        setupNumberButton(R.id.btn5, "5")
        setupNumberButton(R.id.btn6, "6")
        setupNumberButton(R.id.btn7, "7")
        setupNumberButton(R.id.btn8, "8")
        setupNumberButton(R.id.btn9, "9")

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDot() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClear() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { onPlusMinus() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercent() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEquals() }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { onOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { onOperator("−") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperator("÷") }

        updateDisplay()
    }

    private fun setupNumberButton(id: Int, digit: String) {
        findViewById<Button>(id).setOnClickListener { onDigit(digit) }
    }

    private fun onDigit(digit: String) {
        if (justEvaluated) {
            currentInput = "0"
            expressionText = ""
            justEvaluated = false
        }
        currentInput = if (currentInput == "0") digit else currentInput + digit
        updateDisplay()
    }

    private fun onDot() {
        if (justEvaluated) {
            currentInput = "0"
            expressionText = ""
            justEvaluated = false
        }
        if (!currentInput.contains(".")) {
            currentInput += "."
            updateDisplay()
        }
    }

    private fun onClear() {
        currentInput = "0"
        pendingOperand = null
        pendingOperator = null
        expressionText = ""
        justEvaluated = false
        updateDisplay()
    }

    private fun onPlusMinus() {
        currentInput = if (currentInput.startsWith("-")) {
            currentInput.substring(1)
        } else if (currentInput == "0") {
            currentInput
        } else {
            "-$currentInput"
        }
        updateDisplay()
    }

    private fun onPercent() {
        val value = currentInput.toBigDecimalOrNull() ?: return
        val result = value.divide(BigDecimal(100), MathContext.DECIMAL64)
        currentInput = formatBigDecimal(result)
        updateDisplay()
    }

    private fun onOperator(op: String) {
        val value = currentInput.toBigDecimalOrNull() ?: return

        if (pendingOperator != null && !justEvaluated) {
            // Chain: evaluate the pending operation first
            val result = compute(pendingOperand!!, value, pendingOperator!!)
            pendingOperand = result
            currentInput = formatBigDecimal(result)
        } else {
            pendingOperand = value
        }

        pendingOperator = op
        expressionText = "${formatBigDecimal(pendingOperand!!)} $op"
        justEvaluated = false
        updateDisplay(showResultOnly = true)
    }

    private fun onEquals() {
        val op = pendingOperator
        val a = pendingOperand
        val b = currentInput.toBigDecimalOrNull()

        if (op == null || a == null || b == null) return

        val result = compute(a, b, op)
        expressionText = "${formatBigDecimal(a)} $op ${formatBigDecimal(b)} ="
        currentInput = formatBigDecimal(result)
        pendingOperand = null
        pendingOperator = null
        justEvaluated = true
        updateDisplay()
    }

    private fun compute(a: BigDecimal, b: BigDecimal, op: String): BigDecimal {
        return try {
            when (op) {
                "+" -> a.add(b)
                "−" -> a.subtract(b)
                "×" -> a.multiply(b)
                "÷" -> {
                    if (b.compareTo(BigDecimal.ZERO) == 0) {
                        BigDecimal.ZERO
                    } else {
                        a.divide(b, MathContext.DECIMAL64)
                    }
                }
                else -> b
            }
        } catch (e: ArithmeticException) {
            BigDecimal.ZERO
        }
    }

    private fun formatBigDecimal(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return stripped.toPlainString()
    }

    private fun String.toBigDecimalOrNull(): BigDecimal? {
        return try {
            BigDecimal(this)
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun updateDisplay(showResultOnly: Boolean = false) {
        tvExpression.text = expressionText
        tvResult.text = if (currentInput.length > 12) {
            currentInput.take(12)
        } else {
            currentInput
        }
    }
}
