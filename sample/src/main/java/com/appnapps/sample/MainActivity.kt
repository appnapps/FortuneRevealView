package com.appnapps.sample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appnapps.fortunerevealview.FortuneRevealView
import com.appnapps.sample.R

class MainActivity : AppCompatActivity() {

    private lateinit var fortuneText: TextView
    private lateinit var fortuneView: FortuneRevealView
    private lateinit var resetButton: Button
    private lateinit var particleView: ParticleView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fortuneText = findViewById(R.id.fortuneText)
        fortuneView = findViewById(R.id.fortuneView)
        resetButton = findViewById(R.id.resetButton)
        particleView = findViewById(R.id.particleView)

        fortuneText.text = "🍀 오늘의 운세: 기회가 온다, 잡아라!"

        fortuneView.setOnRevealListener {
            Toast.makeText(this, "운세가 공개되었습니다!", Toast.LENGTH_SHORT).show()
            // 애니메이션으로 텍스트 나타내기
            fortuneText.animate()
                .alpha(1f) // 완전히 보이게
                .setDuration(600) // 0.6초간
                .start()

            // 긁는 레이어 점점 사라지게 처리
            fortuneView.fadeOutMask()

            // 파티클 위치 = 뷰 중앙
            val centerX = fortuneView.width / 2f
            val centerY = fortuneView.top + fortuneView.height / 2f

            // particleView 보여주기
            particleView.visibility = View.VISIBLE
            particleView.startExplosion(centerX, centerY)

            // 1.5초 후 다시 숨김
            particleView.postDelayed({
                particleView.visibility = View.GONE
            }, 1500)
        }

        resetButton.setOnClickListener {
            fortuneView.reset()
            fortuneText.alpha = 0.1f // 다시 숨기기
        }
    }
}
