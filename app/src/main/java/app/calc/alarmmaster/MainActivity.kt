package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreFence:SharedPreferences
    private var pref = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        sharedPreFence = getSharedPreferences("MyReg", MODE_PRIVATE)
        pref = sharedPreFence.getString("preference","").toString()

            Handler().postDelayed({
                if (pref.isEmpty()) {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, ClockActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 1000)
    }
}