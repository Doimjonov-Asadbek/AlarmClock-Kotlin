package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.calc.alarmmaster.model.ApiClient
import app.calc.alarmmaster.model.SendVerify
import app.calc.alarmmaster.model.SignUp
import app.calc.alarmmaster.model.User
import retrofit2.Call

class SignInActivity : AppCompatActivity() {
    var sharedPreferences: SharedPreferences? = null
    var preference = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_in)

        val txt_royxatdan_otish = findViewById<TextView>(R.id.txt_royxatdan_otish)
        val parolingizni_unutdingizmi = findViewById<TextView>(R.id.parolingizni_unutdingizmi)
        val email_adres = findViewById<EditText>(R.id.email_adres)
        val signIn_Password = findViewById<EditText>(R.id.signIn_Password)
        val btn_kirish = findViewById<Button>(R.id.btn_kirish)
        val Progress = findViewById<ProgressBar>(R.id.Progress)

        sharedPreferences = getSharedPreferences("MyReg", MODE_PRIVATE)
        preference = sharedPreferences?.getString("preference", "").toString()

        btn_kirish.setOnClickListener {
            if (email_adres.text.toString().isEmpty()) {
                email_adres.error = "Emailni kiriting"
            } else if (signIn_Password.text.toString().isEmpty()) {
                signIn_Password.error = "Parolni kiriting"
            } else {
                val user = User(
                    email_adres.text.toString(),
                    signIn_Password.text.toString()
                )

                val signIn: Call<User> = ApiClient.userService.signIn(user)
                signIn.enqueue(object : retrofit2.Callback<User> {
                    override fun onResponse(call: Call<User>, response: retrofit2.Response<User>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {

                                btn_kirish.visibility = View.GONE
                                Progress.visibility = View.VISIBLE

                                val editor = sharedPreferences?.edit()
                                editor?.putString("preference", response.body()?.token)
                                editor?.apply()

                                val checkVerify = SignUp(email_adres.text.toString(), signIn_Password.text.toString())

                                val check: Call<SignUp> = ApiClient.userService.cheskverefy(checkVerify)
                                check.enqueue(object : retrofit2.Callback<SignUp> {
                                    override fun onResponse(
                                        call: Call<SignUp>,
                                        response: retrofit2.Response<SignUp>
                                    ) {
                                        if (response.isSuccessful) {
                                            val user = response.body()!!.verefy
                                            if (user != "false") {
                                                startActivity(Intent(this@SignInActivity, ClockActivity::class.java))
                                                finish()
                                            } else {

                                                val sendVerify = SendVerify(email_adres.text.toString(), signIn_Password.text.toString())

                                                val send: Call<SendVerify> =
                                                    ApiClient.userService.resendVerefication(sendVerify)
                                                send.enqueue(object : retrofit2.Callback<SendVerify> {
                                                    override fun onResponse(
                                                        call: Call<SendVerify>,
                                                        response: retrofit2.Response<SendVerify>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            val user = response.body()!!.verefyCode
                                                            val intent = Intent(
                                                                this@SignInActivity,
                                                                VerificationCode::class.java
                                                            )
                                                            intent.putExtra("email", email_adres.text.toString())
                                                            intent.putExtra("verify", user)
                                                            startActivity(intent)
                                                            finish()

                                                        }
                                                    }

                                                    override fun onFailure(call: Call<SendVerify>, t: Throwable) {
                                                        Toast.makeText(this@SignInActivity, "Internet bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
                                                    }
                                                })
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<SignUp>, t: Throwable) {
                                        Toast.makeText(this@SignInActivity, "Internet bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                Toast.makeText(
                                    this@SignInActivity, "Email yoki parol xato", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@SignInActivity, "Internet bilan bog'lanishda xatolik", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        txt_royxatdan_otish.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        parolingizni_unutdingizmi.setOnClickListener {
            val intent = Intent(this, RecoveryPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}














