package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_concrete_group.view.*
import kotlinx.android.synthetic.main.group_tasks_recycler_row.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.Task

class TasksAdapter(val context: Context, var tasks: ArrayList<Task>, var rootview: View) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_tasks_recycler_row, parent, false))
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (tasks.size != 0) {
            rootview.concrete_group_empty_text.visibility = View.GONE
            holder.taskName.text = tasks[position].name
            holder.taskMembers.text = "${tasks[position].membersID}".removePrefix("[").removeSuffix("]")
        } else {
            rootview.concrete_group_empty_text.visibility = View.VISIBLE
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var statusIcon = view.group_task_state_icon
        var taskName = view.group_task_row_name
        var taskMembers = view.group_task_row_members
        var dotsIcon = view.group_task_row_dots
    }
}