import java.math.BigInteger
import kotlin.test.Test

class Test {
    val alpha = "abcdefghijklmnopqrstuvwxyz"
    val num = "0123456789"

    val values = num + alpha + alpha.uppercase()

    @Test
    fun testSomeFactorials() {
        val A = BigInteger("47026247687942121848144207491837523525")
        val c = BigInteger("15750249268501108917")
        val M = BigInteger.valueOf(2).pow(128) // 120
        val treshold = BigInteger.valueOf(2).pow(120) // 120
        var max = BigInteger.valueOf(2).pow(128) // 128

        var currentValue = (c).mod(M)
        println(currentValue.toDreamborn())

        while (max > BigInteger.ZERO) {
            currentValue = (A * currentValue + c).mod(M)
            if (currentValue < treshold) {
                println("remaining $max ${currentValue.toDreamborn()}")
            }
            max -= BigInteger.ONE
        }
    }

    private fun BigInteger.toDreamborn(): String {
        var copy = this
        val MOD = BigInteger.valueOf(values.length.toLong())
        val zero = BigInteger.valueOf(0)

        var output = ""
        while (copy > zero) {
            val mod = copy.mod(MOD)
            copy = (copy - mod) / MOD
            output = values[mod.toLong().toInt()] + output
        }
        return output
    }
}
