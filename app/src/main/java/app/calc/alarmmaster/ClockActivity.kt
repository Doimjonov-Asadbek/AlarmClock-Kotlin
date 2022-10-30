package app.calc.alarmmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import app.calc.alarmmaster.adapters.ListAdapter
import app.calc.alarmmaster.model.ApiClient
import app.calc.alarmmaster.model.GetTime
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call

class ClockActivity : AppCompatActivity() {

    private lateinit var addsBtn:FloatingActionButton
    private lateinit var userList: ArrayList<ListData>
    private lateinit var ListAdapter: ListAdapter
    private lateinit var listView:ListView
    private lateinit var settings:View


    private lateinit var sharedPreFence:SharedPreferences
    var pref = ""

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_clock)

        refreshApp()

        sharedPreFence = getSharedPreferences("MyReg", MODE_PRIVATE)
        pref = sharedPreFence.getString("preference","").toString()
        userList = ArrayList()
        settings = findViewById(R.id.settings)
        addsBtn = findViewById(R.id.ActionButton)
        listView = findViewById(R.id.listView)

        getTime()

        addsBtn.setOnClickListener { addInfo() }
        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun refreshApp() {
        val swipeRefresh = findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            val intent = Intent(this, ClockActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
            swipeRefresh.isRefreshing = false
        }
    }

    private fun getTime() {
        val token = pref
        val getTime: Call<Any>? = ApiClient.userService.getTimes("Bearer $token")
        getTime?.enqueue(object : retrofit2.Callback<Any> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Any>, response: retrofit2.Response<Any>) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val json = gson.toJson(response.body())
                    val jsonObject = JSONObject(json)
                    val jsonTimes = jsonObject.getJSONArray("times")
                    val jsonComents = jsonObject.getJSONArray("coments")
                    val jsonSwitchs = jsonObject.getJSONArray("switchs")
                    for (i in 0 until jsonTimes.length()) {
                        val time = jsonTimes.getString(i)
                        val coment = jsonComents.getString(i)
                        val switchs = jsonSwitchs.getString(i)

                        userList.add(ListData(time, coment, switchs))
                    }
                    ListAdapter = ListAdapter(this@ClockActivity, userList)
                    listView.adapter = ListAdapter
                    ListAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@ClockActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("NotifyDataSetChanged")
    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item, null)
        val digitalClock = v.findViewById<TimePicker>(R.id.digitalClock)
        val comment = v.findViewById<EditText>(R.id.comment)
        val addDialog = AlertDialog.Builder(this)
            .setView(v)
            .setPositiveButton("Qo'shish") { dialog, which ->
                val time = digitalClock.hour.toString() + ":" + digitalClock.minute.toString()
                val coment = comment.text.toString()
                val switchs = "true"
                userList.add(ListData(time, coment, switchs))
                ListAdapter = ListAdapter(this@ClockActivity, userList)
                listView.adapter = ListAdapter
                ListAdapter.notifyDataSetChanged()
                val token = pref

                val addTimess = GetTime()
                addTimess.times = time
                addTimess.coments = coment
                addTimess.switchs = switchs

                val addTime: Call<GetTime>? = ApiClient.userService.addTime("Bearer $token",addTimess )
                addTime?.enqueue(object : retrofit2.Callback<GetTime> {
                    override fun onResponse(call: Call<GetTime>, response: retrofit2.Response<GetTime>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ClockActivity, "Yangi vaqt qo'shildi", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this@ClockActivity, "Nimadir xato ketti", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GetTime>, t: Throwable) {
                        Toast.makeText(this@ClockActivity, "Xatolik", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Bekor qilish") { dialog, which -> }
            .create()
        addDialog.create()
        addDialog.show()
    }

}