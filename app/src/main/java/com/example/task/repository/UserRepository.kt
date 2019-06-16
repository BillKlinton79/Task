package com.example.task.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.task.constants.DataBaseConstants
import com.example.task.entities.UserEntity

class UserRepository private constructor(context: Context) {

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object {
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            if (INSTANCE == null) {
                INSTANCE = UserRepository(context)
            }
            return INSTANCE as UserRepository
        }
    }

    fun insert(name: String, email: String, password: String): Int {
        val db = mTaskDataBaseHelper.writableDatabase

        val insertValues = ContentValues()
        insertValues.put(DataBaseConstants.USER.COLUMNS.NAME, name)
        insertValues.put(DataBaseConstants.USER.COLUMNS.EMAIL, email)
        insertValues.put(DataBaseConstants.USER.COLUMNS.PASSWORD, password)

        return db.insert(DataBaseConstants.USER.TABLE_NAME, null, insertValues).toInt()
    }

    fun isEmailExistent(email: String): Boolean {

        var retorno: Boolean

        try {
            val cursor: Cursor

            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID)

            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} = ?"

            val selectionArgs = arrayOf(email)

            cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)

            retorno = cursor.count > 0
            cursor.close()
            //db.rawQuery("select * from user where email = tal",null)
        } catch (e: Exception) {
            throw e
        }

        return retorno
    }

    fun get(email: String, password: String): UserEntity? {

        var userEntity: UserEntity? = null

        try {
            val cursor: Cursor

            val db = mTaskDataBaseHelper.readableDatabase

            val projection = arrayOf(
                DataBaseConstants.USER.COLUMNS.ID,
                DataBaseConstants.USER.COLUMNS.NAME,
                DataBaseConstants.USER.COLUMNS.EMAIL,
                DataBaseConstants.USER.COLUMNS.PASSWORD
            )

            val selection =
                "${DataBaseConstants.USER.COLUMNS.EMAIL} = ? AND ${DataBaseConstants.USER.COLUMNS.PASSWORD} = ?"

            val selectionArgs = arrayOf(email, password)

            cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()

                val userId = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.ID))
                val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.NAME))
                val email = cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.EMAIL))

                userEntity = UserEntity(userId,name,email)

            }
            cursor.close()
            //db.rawQuery("select * from user where email = tal",null)
        } catch (e: Exception) {
            return userEntity
        }
        return userEntity
    }
}