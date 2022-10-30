package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.calc.alarmmaster.model.ApiClient
import app.calc.alarmmaster.model.SignUp
import app.calc.alarmmaster.model.User
import retrofit2.Call

class RegistrationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registration)

        val txt_kirish = findViewById<TextView>(R.id.txt_kirish)
        val email_adres = findViewById<EditText>(R.id.email_adres)
        val signIn_Password = findViewById<EditText>(R.id.signIn_Password)
        val signup_Password = findViewById<EditText>(R.id.signup_Password)
        val btn_kirish = findViewById<Button>(R.id.btn_kirish)

        btn_kirish.setOnClickListener {
            if (email_adres.text.toString().isEmpty()) {
                email_adres.error = "Emailni kiriting"
            } else if (!email_adres.text.toString().endsWith("@gmail.com")) {
                email_adres.error = "@gmail.com"
            } else if (signIn_Password.text.toString() != signup_Password.text.toString()) {
                signup_Password.error = "Parolni noto'g'ri kiritdingiz"
                signIn_Password.error = "Parolni noto'g'ri kiritdingiz"
            } else if (signIn_Password.text.toString().isEmpty()) {
                signIn_Password.error = "Parolni kiriting"
            } else if (signup_Password.text.toString().isEmpty()) {
                signup_Password.error = "Parolni kiriting"
            } else {
                val register = SignUp(
                    email_adres.text.toString(),
                    signIn_Password.text.toString()
                )

                val signUp: Call<SignUp> = ApiClient.userService.signUp(register)
                signUp.enqueue(object : retrofit2.Callback<SignUp> {
                    override fun onResponse(call: Call<SignUp>, response: retrofit2.Response<SignUp>) {
                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                println(" buuuuuuuuuuuuuuuuuuuuuuuuu "+ user.verefy)
                                println(" buuuuuuuuuuuuuuuuuuuuuuuuu "+response.body()!!.token)
                                Toast.makeText(this@RegistrationActivity, "Siz ro'yxatdan o'tdingiz", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegistrationActivity, VerificationCode::class.java)
                                intent.putExtra("token",response.body()!!.token)
                                intent.putExtra("verify",response.body()!!.verefy)
                                intent.putExtra("email",email_adres.text.toString())
                                startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SignUp>, t: Throwable) {
                        Toast.makeText(this@RegistrationActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        txt_kirish.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}