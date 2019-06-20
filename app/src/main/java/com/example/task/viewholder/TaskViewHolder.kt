package com.example.task.viewholder

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.task.R
import com.example.task.business.PriorityBusiness
import com.example.task.entities.OnTaskListFragmentInteractionListener
import com.example.task.entities.TaskEntity
import com.example.task.repository.PriorityCacheConstants

class TaskViewHolder(
    itemView: View,
    private val context: Context,
    private val listener: OnTaskListFragmentInteractionListener
) : RecyclerView.ViewHolder(itemView) {

    private val mRelativeLayout = itemView.findViewById<RelativeLayout>(R.id.relativeLayout)
    private val mTaskDescription = itemView.findViewById<TextView>(R.id.textDescription)
    private val mTextPriority = itemView.findViewById<TextView>(R.id.textPriority)
    private val mTextDueDate = itemView.findViewById<TextView>(R.id.textDueDate)
    private val mImageTask = itemView.findViewById<ImageView>(R.id.imageTask)

    private lateinit var mPriorityBusiness: PriorityBusiness


    fun bindData(taskEntity: TaskEntity) {

        mPriorityBusiness = PriorityBusiness(itemView.context)

        val priorities = mPriorityBusiness.getList()

        mTaskDescription.text = taskEntity.description
        mTextPriority.text = PriorityCacheConstants.getPriorityDescription(taskEntity.priorityId)
        mTextDueDate.text = taskEntity.dueDate
        if (taskEntity.complete)
            mImageTask.setImageResource(R.drawable.ic_done)

        //Evento de click para edição
        mRelativeLayout.setOnClickListener {
            listener.onListClick(taskEntity.id)
        }

        mRelativeLayout.setOnLongClickListener {
            showDeleteConfirmationDialog(taskEntity)
            true
        }

        mImageTask.setOnClickListener {
            showUpdateConfirmationDialog(taskEntity)
        }
    }

    private fun showUpdateConfirmationDialog(taskEntity: TaskEntity) {

        AlertDialog.Builder(context)
            .setTitle("Atualização de tarefa")
            .setMessage("Deseja atualizar ${taskEntity.description}?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Atualizar", { dialog, which -> listener.setTaskComplete(taskEntity) })
            .setNegativeButton("Cancelar", null).show()

    }

    private fun showDeleteConfirmationDialog(taskEntity: TaskEntity) {

        AlertDialog.Builder(context)
            .setTitle("Remoção de tarefa")
            .setMessage("Deseja remover ${taskEntity.description}?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Remover", { dialog, which -> listener.onDeleteClick(taskEntity.id) })
            .setNegativeButton("Cancelar", null).show()

    }

    /* private class handleRemoval(val listener: OnTaskListFragmentInteractionListener, val taskId: Int): DialogInterface.OnClickListener{
         override fun onClick(dialog: DialogInterface?, which: Int) {
             listener.onDeleteClick(taskId)
         }
     }*/
}