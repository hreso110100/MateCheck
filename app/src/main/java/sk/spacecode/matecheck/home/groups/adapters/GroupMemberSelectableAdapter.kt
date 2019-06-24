package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_task_members.view.*
import kotlinx.android.synthetic.main.group_members_selectable_row.view.*
import org.apache.commons.lang3.StringUtils
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User


class GroupMemberSelectableAdapter(
    val context: Context,
    var members: ArrayList<User>,
    var assigned: ArrayList<User>,
    var rootView: View,
    var assignedAdapter: GroupMemberPhotoAdapter
) :
    RecyclerView.Adapter<GroupMemberSelectableAdapter.ViewHolder>(), Filterable {

    private var resultsList = members

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                constraint?.let {

                    if (constraint.isEmpty()) {
                        resultsList = members
                    } else {
                        val suggestions = ArrayList<User>()
                        val filterPattern = StringUtils.stripAccents(it.toString().toLowerCase().trim())

                        for (user in members) {
                            user.firstName?.let { firstName ->
                                user.surname?.let { surName ->
                                    if (StringUtils.stripAccents(
                                            ("$firstName $surName").toLowerCase().trim()
                                        ).contains(filterPattern)
                                    ) {
                                        suggestions.add(user)
                                    }
                                }
                            }
                        }
                        resultsList = suggestions
                    }
                }

                results.values = resultsList
                results.count = resultsList.size

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                resultsList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.group_members_selectable_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = resultsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        val user = resultsList[position]
        holder.userName.text = "${user.firstName} ${user.surname}"
        Glide.with(context).load(user.photoPath).into(holder.userPhoto)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var userName = view.group_member_selectable_row_name
        var userPhoto = view.group_member_selectable_row_photo

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            userName.isChecked = resultsList[position] in assigned
        }

        override fun onClick(v: View) {
            if (resultsList[adapterPosition] !in assigned) {
                userName.isChecked = true
                assigned.add(resultsList[adapterPosition])
                assignedAdapter.notifyDataSetChanged()
            } else {
                assigned.remove(resultsList[adapterPosition])
                userName.isChecked = false
                assignedAdapter.notifyDataSetChanged()
            }

            if (assigned.isEmpty()) {
                with(rootView.add_task_members_next_button) {
                    isEnabled = false
                    alpha = 0.5F
                }
            } else {
                with(rootView.add_task_members_next_button) {
                    isEnabled = true
                    alpha = 1F
                }
            }

        }
    }
}