@file:Suppress("NOTHING_TO_INLINE", "UNUSED")

package xerus.ktutil

import java.time.LocalDate
import java.util.*

// Strings

/** If this String is null or empty, other is returned, else this */
inline fun String?.or(other: String) =
		if (this.isNullOrEmpty()) other else this!!

inline fun String?.nullIfEmpty() =
		if (isNullOrEmpty()) null else this

/** checks if this String contains any of the given [sequences], case-insensitive */
fun String.containsAny(vararg sequences: CharSequence) =
		sequences.any { contains(it, true) }

fun String.containsEach(other: String) = contains(other, true) || other.contains(this, true)

// Numbers

private val powersOf10 = intArrayOf(1, 10, 100, 1000, 10000)
/** rounds a double to [digits] decimal places, by default 2 */
fun Double.round(digits: Int = 2): Double {
	if (digits < 5)
		return Math.rint(this * powersOf10[digits]) / powersOf10[digits]
	val c = Math.pow(10.0, digits.toDouble())
	return Math.rint(this * c) / c
}

fun Double.format(digits: Int) = "%.${digits}f".format(Locale.ENGLISH, this)

inline val Double.square
	get() = this * this

fun Long.byteCountString(): String {
	val unit = 1024
	val exp = (Math.log(toDouble()) / Math.log(unit.toDouble())).toInt()
	val prefix = " KMGTPE"[Math.max(exp, 0)]
	return String.format("%.1f %sB", this / Math.pow(unit.toDouble(), exp.toDouble()), prefix)
}

// Statistics

tailrec fun Int.factorial(total: Double = 1.0): Double = if (this <= 1) total else (this - 1).factorial(total * this)

tailrec fun Int.factorial(downTo: Int, total: Double = 1.0): Double = if (this <= downTo) total else (this - 1).factorial(downTo, total * this)

fun binominalCD(k: Int, n: Int, p: Double = 0.5): Double =
		(0..k).sumByDouble { binominalPD(it, n, p) }

fun binominalPD(k: Int, n: Int, p: Double = 0.5) =
		Math.pow(p, k.toDouble()) * Math.pow(1 - p, (n - k).toDouble()) * (n.factorial(k) / (n - k).factorial())

// Basics

/** @return 1 if true, 0 if false */
inline fun Boolean.toInt() = to(1, 0)

/** @return [ifTrue] when this is true, [ifFalse] when this is false */
inline fun <T> Boolean.to(ifTrue: T, ifFalse: T) = if (this) ifTrue else ifFalse

/** @return result of [ifTrue] when this is true, result of [ifFalse] when this is false */
inline fun <T> Boolean.to(ifTrue: () -> T, ifFalse: () -> T) = if (this) ifTrue() else ifFalse()

/** @return the [value] if true or else null */
inline fun <T> Boolean.ifTrue(value: T) = to(value, null)

/** @return result of [calc] if true or else null */
inline fun <T> Boolean.ifTrue(calc: () -> T) = to(calc, { null })

/** @return the [value] if false or else null */
inline fun <T> Boolean.ifFalse(value: T) = to(null, value)

/** @return result of [calc] if false or else null */
inline fun <T> Boolean.ifFalse(calc: () -> T) = to({ null }, calc)

inline fun <T, U> T.pair(function: T.() -> U): Pair<T, U> =
		Pair(this, this.run(function))

inline fun <T> T?.ifNull(runnable: () -> Unit) =
		also { if (it == null) runnable() }

inline fun <T> T?.ifNotNull(runnable: (T) -> Unit) =
		also { if (it != null) runnable(it) }

// DEBUG

inline fun <T> T?.printWith(function: (T) -> Any?) =
		println(if (this == null) "null" else "${this.testString()} - ${function(this).testString()}")

fun <T> T?.testString(): String = when (this) {
	is Array<*> -> this.joinToString { it.testString() }
	is Collection<*> -> this.joinToString { it.testString() }
	null -> "null"
	else -> toString()
}

inline fun <T> T.printIt(name: Any? = null) =
		apply { testString().let { println(if (name != null) "$name: $it" else it) } }

// Other

fun Throwable.str() = "${javaClass.simpleName}: $message"

fun Any.reflectField(fieldName: String): Any = try {
	javaClass.getField(fieldName).get(this)
} catch (ex: NoSuchFieldException) {
	javaClass.getMethod("get" + fieldName.first().toUpperCase() + fieldName.substring(1)).invoke(this)
}

/** Converts a String in `yyyy-mm-dd` format to a LocalDate */
fun String.toLocalDate(): LocalDate? {
	val split = split("-").map { it.toIntOrNull() ?: return null }
	return LocalDate.of(split[0], split[1], split[2])
}

/** calls [action] with all values from [start], inclusive, to [end], exclusive
 *
 * dedicated mainly for performance-critical algorithms */
inline fun forRange(start: Int, end: Int, action: (Int) -> Unit) {
	var i = start
	while (i < end) {
		action(i)
		i++
	}
}


