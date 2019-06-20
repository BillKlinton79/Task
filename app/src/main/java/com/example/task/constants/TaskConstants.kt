package com.example.task.constants

class TaskConstants {
    object KEY{
        const val USER_ID = "userId"
        const val USER_NAME = "userName"
        const val USER_EMAIL = "userEmail"
    }

    object TASKFILTER{
        val KEY = "taskFilterKey"
        val COMPLETE = 1
        val TODO = 0
    }

    object BUNDLE{
        val TASKID = "filterTaskId"
    }
}