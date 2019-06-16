package com.example.task.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.task.constants.DataBaseConstants
import com.example.task.entities.TaskEntity
import java.lang.Exception

class TaskRepository private constructor(context: Context) {
    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
            return INSTANCE as TaskRepository
        }
    }

    fun insert(taskEntity: TaskEntity) {
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            var complete = if (taskEntity.complete) 1 else 0

            val insertValues = ContentValues()
            insertValues.put(DataBaseConstants.TASK.COLUMNS.USERID, taskEntity.userId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, taskEntity.priorityId)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, taskEntity.description)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, taskEntity.dueDate)
            insertValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            db.insert(DataBaseConstants.TASK.TABLE_NAME, null, insertValues).toInt()
        } catch (e: Exception) {
            throw e
        }
    }

    fun update(taskEntity: TaskEntity) {
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            var complete = if (taskEntity.complete) 1 else 0

            val upDateValues = ContentValues()
            upDateValues.put(DataBaseConstants.TASK.COLUMNS.USERID, taskEntity.userId)
            upDateValues.put(DataBaseConstants.TASK.COLUMNS.PRIORITYID, taskEntity.priorityId)
            upDateValues.put(DataBaseConstants.TASK.COLUMNS.DESCRIPTION, taskEntity.description)
            upDateValues.put(DataBaseConstants.TASK.COLUMNS.DUEDATE, taskEntity.dueDate)
            upDateValues.put(DataBaseConstants.TASK.COLUMNS.COMPLETE, complete)

            val selection =
                "${DataBaseConstants.TASK.COLUMNS.ID} = ?"

            val selectionArgs = arrayOf(taskEntity.id.toString())

            db.update(DataBaseConstants.TASK.TABLE_NAME, upDateValues, selection, selectionArgs)
        } catch (e: Exception) {
            throw e
        }
    }

    fun delete(id: Int) {
        try {
            val db = mTaskDataBaseHelper.writableDatabase


            val where =
                "${DataBaseConstants.TASK.COLUMNS.ID} = ?"

            val whereArgs = arrayOf(id.toString())

            db.delete(DataBaseConstants.TASK.TABLE_NAME, where, whereArgs)
        } catch (e: Exception) {
            throw e
        }
    }

    fun get(id: Int): TaskEntity? {

        var taskEntity: TaskEntity? = null

        try {
            val cursor: Cursor

            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(
                DataBaseConstants.TASK.COLUMNS.ID,
                DataBaseConstants.TASK.COLUMNS.USERID,
                DataBaseConstants.TASK.COLUMNS.PRIORITYID,
                DataBaseConstants.TASK.COLUMNS.DESCRIPTION,
                DataBaseConstants.TASK.COLUMNS.DUEDATE,
                DataBaseConstants.TASK.COLUMNS.COMPLETE
            )

            val selection =
                "${DataBaseConstants.TASK.COLUMNS.ID} = ?"

            val selectionArgs = arrayOf(id.toString())

            cursor = db.query(DataBaseConstants.TASK.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()

                val taskId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                val userId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.USERID))
                val priorityId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                val description = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))
                val dueDate = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                val complete = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)

                taskEntity = TaskEntity(id, userId, priorityId, description, complete, dueDate)

            }
            cursor.close()
            //db.rawQuery("select * from user where email = tal",null)
        } catch (e: Exception) {
            return taskEntity
        }
        return taskEntity
    }

    fun getList(userId: Int): MutableList<TaskEntity> {

        val list = mutableListOf<TaskEntity>()

        try {
            val cursor: Cursor
            val db = mTaskDataBaseHelper.readableDatabase

            cursor = db.rawQuery(
                "select * from ${DataBaseConstants.TASK.TABLE_NAME} " +
                        "WHERE ${DataBaseConstants.TASK.COLUMNS.USERID} = $userId", null
            )

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.ID))
                    val priorityId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.PRIORITYID))
                    val complete = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.COMPLETE)) == 1)
                    val dueDate = cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DUEDATE))
                    val description =
                        cursor.getString(cursor.getColumnIndex(DataBaseConstants.TASK.COLUMNS.DESCRIPTION))

                    list.add(TaskEntity(id, userId, priorityId, description, complete, dueDate))
                }
            }
            cursor.close()

        } catch (e: Exception) {
            return list
        }
        return list
    }
}