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
import kotlinx.android.synthetic.main.group_members_selectable_row.view.*
import org.apache.commons.lang3.StringUtils
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User


class GroupMemberSelectableAdapter(val context: Context, var members: ArrayList<User>) :
    RecyclerView.Adapter<GroupMemberSelectableAdapter.ViewHolder>(), Filterable {

    private var membersList = members

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestions = ArrayList<User>()

                constraint?.let {
                    if (it.isEmpty()) {
                        membersList.addAll(members)
                    } else {
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
                            membersList = suggestions
                        }
                    }
                }
                results.values = membersList
                results.count = membersList.size

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                membersList.addAll(results?.values as ArrayList<User>)
                Log.d("TEST", membersList.toString())
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

    override fun getItemCount() = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.userName.text = "${members[position].firstName} ${members[position].surname}"
        Glide.with(context).load(members[position].photoPath).into(holder.userPhoto)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val itemStateArray = SparseBooleanArray()
        var userName = view.group_member_selectable_row_name
        var userPhoto = view.group_member_selectable_row_photo

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            userName.isChecked = itemStateArray.get(position, false)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            if (!itemStateArray.get(adapterPosition, false)) {
                userName.isChecked = true
                itemStateArray.put(adapterPosition, true)
            } else {
                userName.isChecked = false
                itemStateArray.put(adapterPosition, false)
            }
        }

    }
}