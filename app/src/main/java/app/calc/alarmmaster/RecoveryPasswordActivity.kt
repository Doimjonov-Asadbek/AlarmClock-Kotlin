package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class RecoveryPasswordActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recovery_password)

        val orqa = findViewById<View>(R.id.orqa)
        val email_adres = findViewById<EditText>(R.id.email_adres)
        val btn_kirish = findViewById<Button>(R.id.btn_kirish)
        val timer =findViewById<TextView>(R.id.parolingizni_unutdingizmi)

        val time = object : CountDownTimer(120000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "00:" + millisUntilFinished / 1000

                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli
                val elapsedSeconds = diff / secondsInMilli

                timer.text = "0$elapsedMinutes:$elapsedSeconds"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                if (timer.text == "00:0") {
                    timer.setTextColor(resources.getColor(R.color.red))
                    timer.text = "Kodni qayta jo'natish"
                }
                if (timer.text == "Kodni qayta jo'natish") {
                    timer.setOnClickListener {
                        timer.setTextColor(resources.getColor(R.color.black))
                        timer.text = "02:00"
                        start()
                    }
                }
            }
        }.start()


        email_adres.filters = arrayOf(InputFilter.LengthFilter(6))


        btn_kirish.setOnClickListener {
            if (email_adres.text.toString().isEmpty()) {
                email_adres.error = "Email kodini kiriting"
            }
            else {
                val intent = Intent(this, EnterNewPassword::class.java)
                startActivity(intent)
                finish()
            }
        }


        orqa.setOnClickListener {
            onBackPressed()
        }

    }
}