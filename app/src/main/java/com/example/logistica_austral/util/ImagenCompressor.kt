package com.example.logistica_austral.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Utilidad para comprimir/redimensionar imágenes capturadas desde cámara o galería
 * antes de enviarlas por la red (reduce riesgo de HTTP 413 y acelera el upload).
 *
 * Estrategia:
 * 1. Se leen solo las dimensiones (inJustDecodeBounds = true) para no cargar toda la imagen gigante en memoria.
 * 2. Se calcula un inSampleSize (potencia de 2) para bajar resolución aproximada por debajo del máximo (MAX_DIM = 720).
 * 3. Se decodifica la imagen reducida.
 * 4. Si tras el sampleo alguna dimensión aún supera MAX_DIM, se escala exactamente a que el lado mayor sea 720.
 * 5. Se comprime a JPEG con QUALITY (70) en un archivo temporal en cacheDir.
 *
 * Beneficios:
 * - Menor uso de memoria.
 * - Menor tamaño del archivo (evita 413 en servidor).
 * - Mejor experiencia de usuario en conexiones móviles.
 */
object ImagenCompressor {
    private const val MAX_DIM = 720          // Lado máximo permitido
    private const val QUALITY = 70           // Calidad JPEG (0-100)

    /**
     * Comprime/Redimensiona la imagen apuntada por el Uri y devuelve un File listo para multipart.
     */
    fun comprimir(context: Context, origen: Uri): File {
        val resolver = context.contentResolver
        val inputBounds: InputStream = resolver.openInputStream(origen)
            ?: error("No se pudo abrir el stream de la imagen")

        // 1. Leer solo dimensiones
        val optsBounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        inputBounds.use { stream -> BitmapFactory.decodeStream(stream, null, optsBounds) }

        val (origW, origH) = optsBounds.outWidth to optsBounds.outHeight
        require(origW > 0 && origH > 0) { "Dimensiones inválidas de la imagen" }

        // 2. Calcular inSampleSize (potencia de 2) para bajar la resolución cerca de MAX_DIM
        var sample = 1
        while (origW / sample > MAX_DIM || origH / sample > MAX_DIM) {
            sample *= 2
        }

        // 3. Decodificar con inSampleSize
        val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sample }
        val inputFull: InputStream = resolver.openInputStream(origen)
            ?: error("No se pudo abrir stream para decodificación completa")
        val sampledBitmap = inputFull.use { BitmapFactory.decodeStream(it, null, decodeOpts) }
            ?: error("No se pudo decodificar la imagen")

        // 4. Escalado exacto si todavía supera el límite en alguno de los lados
        val (w, h) = sampledBitmap.width to sampledBitmap.height
        val finalBitmap: Bitmap = if (w > MAX_DIM || h > MAX_DIM) {
            val ratio = if (w >= h) MAX_DIM / w.toFloat() else MAX_DIM / h.toFloat()
            val targetW = (w * ratio).toInt()
            val targetH = (h * ratio).toInt()
            Bitmap.createScaledBitmap(sampledBitmap, targetW, targetH, true)
        } else {
            sampledBitmap
        }

        // 5. Comprimir a JPEG en archivo temporal
        val outFile = File(context.cacheDir, "camion_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outFile).use { fos ->
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, fos)
        }

        // Liberar bitmap fuente si se creó uno escalado distinto
        if (finalBitmap != sampledBitmap) {
            sampledBitmap.recycle()
        }

        return outFile
    }
}

