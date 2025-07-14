package com.appnapps.sample

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class ParticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val particles = mutableListOf<Particle>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var isRunning = false

    data class Particle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var life: Int,
        val color: Int
    )

    fun startExplosion(centerX: Float, centerY: Float) {
        particles.clear()
        for (i in 0 until 100) {
            val angle = Random.nextFloat() * 360f
            val speed = Random.nextFloat() * 10 + 5
            val rad = Math.toRadians(angle.toDouble())
            val vx = (cos(rad) * speed).toFloat()
            val vy = (sin(rad) * speed).toFloat()
            val life = Random.nextInt(20, 40)
            val color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            particles.add(Particle(centerX, centerY, vx, vy, life, color))
        }

        isRunning = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isRunning) return

        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            paint.color = p.color
            canvas.drawCircle(p.x, p.y, 8f, paint)
            p.x += p.vx
            p.y += p.vy
            p.life -= 1
            if (p.life <= 0) {
                iterator.remove()
            }
        }

        if (particles.isNotEmpty()) {
            postInvalidateDelayed(16)
        } else {
            isRunning = false
        }
    }
}
