package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.group_recycler_row.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User

class GroupMembersRecyclerAdapter(val context: Context, var members: ArrayList<User>) :
    RecyclerView.Adapter<GroupMembersRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_recycler_row, parent, false))
    }

    override fun getItemCount() = members.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = "${members[position].firstName} ${members[position].surname}"
        Glide.with(context).load(members[position].photoPath).into(holder.userPhoto)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userName = view.group_row_name
        var userPhoto = view.group_row_photo

    }
}