package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_card.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.enums.TaskState
import sk.spacecode.matecheck.home.groups.ConcreteGroupFragment
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.Task

class GroupsListAdapter(
    val context: Context,
    var groups: ArrayList<Group>,
    var tasks: ArrayList<Task>,
    var activity: FragmentActivity?
) :
    RecyclerView.Adapter<GroupsListAdapter.ViewHolder>() {
    private var completedTask = 0
    private var inProgressTask = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_card, parent, false))
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        tasks.forEach {
            if (it.groupID == groups[position].ID) {
                if (it.status == TaskState.DONE) {
                    completedTask++
                } else {
                    inProgressTask++
                }
            }
        }

        holder.groupName.text = groups[position].name
        holder.groupContainer.setCardBackgroundColor(Color.parseColor(groups[position].color))
        holder.taskDetailCompleted.text = activity?.getString(R.string.group_card_completed_task, completedTask)
        holder.taskDetailInProgress.text = activity?.getString(R.string.group_card_in_progress_task, inProgressTask)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var groupName: TextView = view.group_card_name
        var groupContainer: CardView = view.group_card_view
        var taskDetailCompleted: TextView = view.group_card_detail_completed
        var taskDetailInProgress: TextView = view.group_card_detail_in_progress

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val bundle = Bundle()
            val fragment = ConcreteGroupFragment()
            bundle.putSerializable("groupDetail", groups[adapterPosition])
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.home_fragment_container, fragment)
                ?.addToBackStack(null)?.commit()
        }
    }
}