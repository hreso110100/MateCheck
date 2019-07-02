package sk.spacecode.matecheck.home.tasks.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_concrete_group.view.*
import kotlinx.android.synthetic.main.tasks_recycler_row.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.Task
import sk.spacecode.matecheck.model.User

open class TasksGroupAdapter(
    val context: Context,
    var tasks: ArrayList<Task>,
    var users: ArrayList<User>,
    var rootview: View
) :
    RecyclerView.Adapter<TasksGroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.tasks_recycler_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        rootview.tasks_empty_text.visibility = View.GONE
        holder.taskName.text = tasks[position].name

        holder.taskDetail.text = run {
            val membersName = arrayListOf<String>()

            users.forEach {
                if (it.ID in tasks[position].membersID) {
                    membersName.add("${it.firstName} ${it.surname}")
                }
            }
            membersName.joinToString()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var statusIcon = view.task_row_state_icon
        var taskName = view.task_row_name
        var taskDetail = view.task_row_detail
        var dotsIcon = view.task_row_dots
    }
}