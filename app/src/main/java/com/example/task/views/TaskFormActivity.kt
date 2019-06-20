package com.example.task.views

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import com.example.task.R
import com.example.task.business.PriorityBusiness
import com.example.task.business.TaskBusiness
import com.example.task.constants.DataBaseConstants
import com.example.task.constants.TaskConstants
import com.example.task.entities.PriorityEntity
import com.example.task.entities.TaskEntity
import com.example.task.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mPriorityBusiness: PriorityBusiness
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences
    private val mSimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var mListPrioritiesEntity: MutableList<PriorityEntity> = mutableListOf()
    private var mListPrioritiesEntityId: MutableList<Int> = mutableListOf()
    private var mTaskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mPriorityBusiness = PriorityBusiness(this)
        mTaskBusiness = TaskBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)

        setListeners()
        loadPriorities()
        loadDataFromActivity()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            val task = mTaskBusiness.get(mTaskId)

            if (task != null) {
                editDescription.setText(task.description)
                buttonDate.text = task.dueDate
                checkComplete.isChecked = task.complete
                spinnerPriority.setSelection(getIndex(task.priorityId))
            }
        }
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0..mListPrioritiesEntity.size){
            if(priorityId == mListPrioritiesEntity[i].id){
                index = i
                break
            }
        }
        return index
    }

    private fun setListeners() {
        buttonDate.setOnClickListener(this)
        buttonSave.setOnClickListener(this)
    }

    private fun loadPriorities() {
        mListPrioritiesEntity = mPriorityBusiness.getList()
        mListPrioritiesEntityId = mListPrioritiesEntity.map { it.id }.toMutableList()

        //Converte para uma lista de Strings
        val lstPriorities = mListPrioritiesEntity.map { it.description }

        //Cria um adapter para o spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, lstPriorities)

        spinnerPriority.adapter = adapter
    }

    private fun openDatePickerDialog() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, dayOfMonth).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonDate -> {
                openDatePickerDialog()
            }
            R.id.buttonSave -> {
                handleSave()
                finish()
            }
        }
    }

    private fun handleSave() {
        try {
            val priorityId = mListPrioritiesEntityId[spinnerPriority.selectedItemPosition]
            val complete = checkComplete.isChecked
            val dueDate = buttonDate.text.toString()
            val description = editDescription.text.toString()
            val userId = mSecurityPreferences.getStoredString(TaskConstants.KEY.USER_ID)

            val taskEntity = TaskEntity(mTaskId, userId.toInt(), priorityId, description, complete, dueDate)

            if(mTaskId == 0){
                mTaskBusiness.insert(taskEntity)
                Toast.makeText(this, "Tarefa incluida com sucesso!", Toast.LENGTH_LONG).show()
            }else{
                mTaskBusiness.update(taskEntity)
                Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_LONG).show()

            }

        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.erro_inesperado), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        buttonDate.text = mSimpleDateFormat.format(calendar.time)
    }
}
