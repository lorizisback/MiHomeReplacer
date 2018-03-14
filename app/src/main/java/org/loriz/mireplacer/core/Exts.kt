package org.loriz.mireplacer.core

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Created by Mark0 on 14/03/2018.
 */

fun File.copy(src: File, dst: File) {
    FileInputStream(src).use({ `in` ->
        FileOutputStream(dst).use({ out ->
            // Transfer bytes from in to out
            val buf = ByteArray(1024)
            var len: Int = -1
            while ({len = `in`.read(buf); len}() > 0) {
                out.write(buf, 0, len)
            }
        })
    })
}