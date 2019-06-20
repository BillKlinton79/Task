package com.example.task.entities

interface OnTaskListFragmentInteractionListener {
    fun onListClick(taskId: Int)
    fun onDeleteClick(taskId: Int)
    fun setTaskComplete(taskEntity: TaskEntity)
}