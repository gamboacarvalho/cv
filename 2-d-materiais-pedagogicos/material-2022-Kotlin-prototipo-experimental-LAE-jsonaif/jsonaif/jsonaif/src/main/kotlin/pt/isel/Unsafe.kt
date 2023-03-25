package pt.isel

import sun.misc.Unsafe

object Unsafe {
    val UNSAFE: Unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").let {
        it.isAccessible = true
        it[null] as Unsafe
    }
}
