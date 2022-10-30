package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.calc.alarmmaster.model.ApiClient
import app.calc.alarmmaster.model.SendVerify
import app.calc.alarmmaster.model.SignUp
import retrofit2.Call

class VerificationCode : AppCompatActivity() {

    var sharedPreferences: SharedPreferences? = null
    var preference = ""

    var token = ""
    var verify = ""
    var email = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_verification_code)

        token = intent.getStringExtra("token").toString()
        verify = intent.getStringExtra("verify").toString()
        email = intent.getStringExtra("email").toString()

        sharedPreferences = getSharedPreferences("MyReg", MODE_PRIVATE)
        preference = sharedPreferences?.getString("preference", "").toString()

        val timer = findViewById<TextView>(R.id.timer)
        val kod = findViewById<EditText>(R.id.kod)
        val btn_kirish = findViewById<Button>(R.id.btn_kirish)
        val orqa = findViewById<View>(R.id.orqa)



        val sign = SendVerify(email, "")
        object : CountDownTimer(25000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "00:" + millisUntilFinished / 1000
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                timer.text = "Resend code"
                timer.setOnClickListener {
                    verify = ""
                    val resend: Call<SendVerify> = ApiClient.userService.resendVerefication(sign)
                    resend.enqueue(object : retrofit2.Callback<SendVerify> {
                        override fun onResponse(call: Call<SendVerify>, response: retrofit2.Response<SendVerify>) {
                            if (response.isSuccessful) {
                                val sendVerify = response.body()
                                if (sendVerify != null) {
                                    verify = sendVerify.verefyCode
                                }
                            }
                        }

                        override fun onFailure(call: Call<SendVerify>, t: Throwable) {
                            Toast.makeText(this@VerificationCode, "Kod yuborilmadi", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                orqa.visibility = View.VISIBLE
            }
        }.start()


        btn_kirish.setOnClickListener {
            if (kod.text.toString().isEmpty()) {
                kod.error = "Kodni kiriting"
            } else if (kod.text.toString() != verify) {
                kod.error = "Kodni noto'g'ri kiritdingiz"
            } else {
                val register = SignUp(email, token)
                val signUp: Call<SignUp> = ApiClient.userService.verefyuser(register)
                signUp.enqueue(object : retrofit2.Callback<SignUp> {
                    override fun onResponse(
                        call: Call<SignUp>,
                        response: retrofit2.Response<SignUp>
                    ) {
                        if (response.isSuccessful) {
                            val user = response.body()?.token
                            sharedPreferences?.edit()?.putString("preference", user)?.apply()
                            val intent = Intent(this@VerificationCode, ClockActivity::class.java)
                            intent.putExtra("token", token)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@VerificationCode, "Siz ro'yhatdan o'tdingiz", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<SignUp>, t: Throwable) {
                        Toast.makeText(
                            this@VerificationCode,
                            "Internet bilan bog'lanishda xatolik",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }
        orqa.setOnClickListener {
            onBackPressed()
        }
    }
}