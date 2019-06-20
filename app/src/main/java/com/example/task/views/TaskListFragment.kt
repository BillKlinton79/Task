package com.example.task.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.task.R
import com.example.task.adapter.TaskListAdapter
import com.example.task.business.TaskBusiness
import com.example.task.constants.TaskConstants
import com.example.task.entities.OnTaskListFragmentInteractionListener
import com.example.task.entities.TaskEntity
import com.example.task.util.SecurityPreferences
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment(), View.OnClickListener {

    private lateinit var mContext: Context
    private lateinit var mRecyclerTaskList: RecyclerView
    private lateinit var mTaskBusiness: TaskBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mListener: OnTaskListFragmentInteractionListener
    private var mTaskFilter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTaskFilter = it.getInt(TaskConstants.TASKFILTER.KEY)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        rootView.findViewById<FloatingActionButton>(R.id.floatAddTask).setOnClickListener(this)

        mContext = rootView.context
        mTaskBusiness = TaskBusiness(mContext)
        mSecurityPreferences = SecurityPreferences(mContext)
        mListener = object : OnTaskListFragmentInteractionListener {

            override fun setTaskComplete(taskEntity: TaskEntity) {
                mTaskBusiness.complete(taskEntity)
                loadTasks()
            }

            override fun onDeleteClick(taskId: Int) {
                mTaskBusiness.delete(taskId)
                loadTasks()
                Toast.makeText(mContext, "Tarefa removida com sucesso!", Toast.LENGTH_LONG).show()
            }

            override fun onListClick(taskId: Int) {
                val intent = Intent(mContext, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, taskId)
                intent.putExtras(bundle)

                startActivity(intent)
            }
        }

        mRecyclerTaskList = rootView.findViewById(R.id.recyclerTaskList)

        mRecyclerTaskList.adapter = TaskListAdapter(mutableListOf(), mListener)

        mRecyclerTaskList.layoutManager = LinearLayoutManager(mContext)

        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        val taskList = mTaskBusiness.getList(mTaskFilter)

        mRecyclerTaskList.adapter = TaskListAdapter(taskList, mListener)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.floatAddTask -> {
                startActivity(Intent(mContext, TaskFormActivity::class.java))
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(taskFilter: Int): TaskListFragment =
            TaskListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TaskConstants.TASKFILTER.KEY, taskFilter)
                }
            }
    }
}
