package sk.spacecode.matecheck.home.tasks.adapters

import android.content.Context
import android.view.View
import sk.spacecode.matecheck.model.Task
import sk.spacecode.matecheck.model.User

class TasksAdapter(context: Context, tasks: ArrayList<Task>, users: ArrayList<User>, rootview: View) :
    TasksGroupAdapter(context, tasks, users, rootview) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskName.text = tasks[position].name

        if (tasks[position].public) {
            holder.taskDetail.text = "${tasks[position].groupID}"
        } else {
            holder.taskDetail.text = "${tasks[position].creatorID}"
        }

    }
}