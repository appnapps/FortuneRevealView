package com.appnapps.fortunerevealview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.roundToInt

class FortuneRevealView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    private lateinit var canvasBitmap: Canvas

    private val path = Path()
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.STROKE
        strokeWidth = 60f
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val revealThreshold = 0.5f // 50% 이상 긁으면 reveal
    private var revealed = false
    private var fadingOut = false
    private var maskAlpha = 255

    private var onRevealListener: (() -> Unit)? = null
    private val sampleInterval = 10 // 픽셀 간격으로 샘플링해서 퍼센트 계산

    fun setOnRevealListener(listener: () -> Unit) {
        onRevealListener = listener
    }

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvasBitmap = Canvas(bitmap)
        canvasBitmap.drawColor(Color.GRAY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val alphaPaint = Paint().apply {
            alpha = maskAlpha
        }
        canvas.drawBitmap(bitmap, 0f, 0f, alphaPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (revealed) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                vibrate()
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                canvasBitmap.drawPath(path, paint)
                invalidate()
                checkRevealPercent()
            }
        }
        return true
    }

    private fun checkRevealPercent() {
        val width = bitmap.width
        val height = bitmap.height

        var cleared = 0
        var total = 0

        for (x in 0 until width step sampleInterval) {
            for (y in 0 until height step sampleInterval) {
                total++
                if (bitmap.getPixel(x, y) == Color.TRANSPARENT) {
                    cleared++
                }
            }
        }

        val ratio = cleared.toFloat() / total
        if (ratio >= revealThreshold && !revealed) {
            revealed = true
            onRevealListener?.invoke()
            fadeOutMask() // 마스킹 자연스럽게 사라짐
        }
    }

    fun fadeOutMask() {
        if (fadingOut) return
        fadingOut = true

        Thread {
            while (maskAlpha > 0) {
                maskAlpha -= 15
                if (maskAlpha < 0) maskAlpha = 0
                postInvalidate()
                Thread.sleep(30)
            }
            fadingOut = false
        }.start()
    }

    fun reset() {
        revealed = false
        fadingOut = false
        path.reset()
        maskAlpha = 255
        bitmap.eraseColor(Color.GRAY)
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
