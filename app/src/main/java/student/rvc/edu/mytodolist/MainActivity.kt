package student.rvc.edu.mytodolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var task = findViewById<EditText>(R.id.txtTask)
        var names = findViewById<EditText>(R.id.txtMessage)
        var btnMessage = findViewById<Button>(R.id.btnMessage)
        var messages = findViewById<TextView>(R.id.txtNotes)
        var ref = FirebaseDatabase.getInstance().getReference("Message")
        var txtTask = findViewById<EditText>(R.id.txtTask)
        
        btnMessage.setOnClickListener{
            // Write a message to the database
            txtTask.requestFocus()
            var messageid = ref.push().key
            var messageg = Message(messageid.toString(), task.text.toString(), names.text.toString())
            hideKeyboard()
            task.setText("")
            names.setText("")
            txtTask.requestFocus()
            ref.child(messageid.toString()).setValue(messageg).addOnCompleteListener {
                Toast.makeText(this, "Task Added!", 3).show()
            }
        }
//listen and show data changes
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages.text = ""
                val children = dataSnapshot.children
                children.forEach {
                    println("data: " + it.toString())
                    if (messages.text.toString() != "") {
                        messages.text = messages.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }else{
                        messages.text = "My Tasks"
                        messages.text = messages.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Message", "Failed to read value.", error.toException())
            }
        })
    }
    // function to hide keyboard goes right before the last right bracket of Class MainActivity
//import android.content.Context
//import android.view.inputmethod.InputMethodManager
    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }

}