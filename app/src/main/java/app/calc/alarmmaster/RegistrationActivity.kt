package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.calc.alarmmaster.model.ApiClient
import app.calc.alarmmaster.model.SignUp
import retrofit2.Call

class RegistrationActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_registration)

        val txtEnter= findViewById<TextView>(R.id.txt_kirish)
        val email= findViewById<EditText>(R.id.email_adres)
        val signInPassword = findViewById<EditText>(R.id.signIn_Password)
        val returnPassword = findViewById<EditText>(R.id.signup_Password)
        val btnEnter = findViewById<Button>(R.id.btn_kirish)

        btnEnter.setOnClickListener {
            if (email.text.toString().isEmpty()) {
                email.error = "Emailni kiriting"
            } else if (!email.text.toString().endsWith("@gmail.com")) {
                email.error = "@gmail.com"
            } else if (signInPassword.text.toString() != returnPassword.text.toString()) {
                returnPassword.error = "Parolni noto'g'ri kiritdingiz"
                signInPassword.error = "Parolni noto'g'ri kiritdingiz"
            } else if (signInPassword.text.toString().isEmpty()) {
                signInPassword.error = "Parolni kiriting"
            } else if (returnPassword.text.toString().isEmpty()) {
                returnPassword.error = "Parolni kiriting"
            } else {
                val register = SignUp(
                    email.text.toString(),
                    signInPassword.text.toString()
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
                                intent.putExtra("email",email.text.toString())
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

        txtEnter.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}