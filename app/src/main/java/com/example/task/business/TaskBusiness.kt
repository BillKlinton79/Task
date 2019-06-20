package com.example.task.business

import android.content.Context
import com.example.task.constants.TaskConstants
import com.example.task.entities.TaskEntity
import com.example.task.repository.TaskRepository
import com.example.task.util.SecurityPreferences

class TaskBusiness(context: Context) {

    private val mTaskRepository: TaskRepository = TaskRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)
    fun getList(taskFilter: Int): MutableList<TaskEntity> {
        val userId = mSecurityPreferences.getStoredString(TaskConstants.KEY.USER_ID).toInt()
        return mTaskRepository.getList(userId, taskFilter)
    }

    fun get(id: Int) = mTaskRepository.get(id)

    fun insert(taskEntity: TaskEntity) = mTaskRepository.insert(taskEntity)

    fun update(taskEntity: TaskEntity) = mTaskRepository.update(taskEntity)

    fun delete(taskId: Int) = mTaskRepository.delete(taskId)

    fun complete(taskEntity: TaskEntity) {
        if (taskEntity != null) {
            taskEntity.complete = !taskEntity.complete
            mTaskRepository.update(taskEntity)
        }
    }

}