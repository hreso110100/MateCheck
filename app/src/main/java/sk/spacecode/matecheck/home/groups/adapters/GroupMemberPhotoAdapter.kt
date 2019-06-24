package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_task_members.view.*
import kotlinx.android.synthetic.main.group_members_recycler_horizontal.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User

class GroupMemberPhotoAdapter(
    val context: Context, var assigned: ArrayList<User>,
    val rootView: View,
    var selectableAdapter: GroupMemberSelectableAdapter?
) :
    RecyclerView.Adapter<GroupMemberPhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.group_members_recycler_horizontal,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = assigned.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(assigned[position].photoPath).into(holder.userPhoto)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var userPhoto = view.task_member_horizontal_photo

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            assigned.removeAt(adapterPosition)
            rootView.add_task_members_recycler.adapter = selectableAdapter
            notifyDataSetChanged()

            if (assigned.isEmpty()) {
                with(rootView.add_task_members_next_button) {
                    isEnabled = false
                    alpha = 0.5F
                }
            }

        }
    }
}