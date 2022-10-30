package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar

class EnterNewPassword : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_enter_new_password)

        val orqa = findViewById<View>(R.id.orqa)
        val email_adres = findViewById<EditText>(R.id.email_adres)
        val return_password = findViewById<EditText>(R.id.return_password)
        val btn_kirish = findViewById<View>(R.id.btn_kirish)

        btn_kirish.setOnClickListener {

            if (email_adres.text.toString().isEmpty()) {
                email_adres.error = "Parol xato"
            }
            else if (return_password.text.toString().isEmpty()){
                return_password.error = "Parolni qayta kiriting"
            }
            else if (email_adres.text.toString() != return_password.text.toString()){
                return_password.error = "Parolni qayta kiriting"
                email_adres.error = "Parolni qayta kiriting"
            }
            else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 500)
            }
        }

        orqa.setOnClickListener {
            onBackPressed()
        }

    }
}