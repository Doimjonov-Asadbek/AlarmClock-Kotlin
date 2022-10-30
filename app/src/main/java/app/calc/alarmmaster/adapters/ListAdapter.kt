package app.calc.alarmmaster.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import app.calc.alarmmaster.ListData
import app.calc.alarmmaster.R

class ListAdapter(private val context:Activity, private val list:ArrayList<ListData>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ViewHolder", "UseSwitchCompatOrMaterialCode", "InflateParams", "MissingInflatedId")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_item, null, true)

        val clock = rowView.findViewById(R.id.textClock) as TextView
        val comment = rowView.findViewById(R.id.comments) as TextView
        val switchs = rowView.findViewById(R.id.switch1) as Switch

        clock.text = list[position].Clock
        comment.text = list[position].comment
        switchs.text = list[position].switchs

        rowView.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val v = inflater.inflate(R.layout.add_item, null)
            val digitalClock = v.findViewById(R.id.digitalClock) as TimePicker
            val comment = v.findViewById<EditText>(R.id.comment)
            val addDialog = AlertDialog.Builder(context)
            addDialog.setView(v)
            addDialog.setTitle("Tahrirlash")
            addDialog.setPositiveButton("Saqlash") { _, _ ->
                val clock = digitalClock.hour.toString() + ":" + digitalClock.minute.toString()
                val comment = comment.text.toString()
                val switchs = switchs.text.toString()
                val listData = ListData(clock, comment, switchs)
                list[position] = listData
                notifyDataSetChanged()
            }
            addDialog.setNegativeButton("Bekor qilish") { dialog, which -> }
            addDialog.create()
            addDialog.show()
        }

        return rowView
    }
}