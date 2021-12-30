package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create a listener to delete tasks by long clicking on them
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove item from list
                listOfTasks.removeAt(position)
                //Notify adapter that data set has changed
                adapter.notifyDataSetChanged()

                saveTasks()
            }
        }

        loadTasks()

        //get text field
        val textField = findViewById<EditText>(R.id.addTaskField)

        //Lookup recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.taskList)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Detect when user clicks on add button
        findViewById<Button>(R.id.button).setOnClickListener {
            //Code in here is going to be executed when the user clicks on a button

            //Get text user has entered into text field
            val newTask = textField.text.toString()

            //Don't add task if field is empty
            if (newTask == "") {
                return@setOnClickListener
            }

            //Add string to our list of tasks
            listOfTasks.add(newTask)

            //Notify adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //Reset text field
            textField.setText("")

            saveTasks()
        }
    }

    //Persistence methods
    //Get data file
    fun getDataFile() : File {
        return File(filesDir, "tasks.txt")
    }

    //Load items by reading every line in the data file
    fun loadTasks() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch (ioException : IOException) {
            ioException.printStackTrace()
        }

    }

    //Save items by writing to data file
    fun saveTasks() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }

    }
}