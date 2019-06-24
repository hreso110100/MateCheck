package sk.spacecode.matecheck.home.groups.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_card.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.ConcreteGroupFragment
import sk.spacecode.matecheck.model.Group


class GroupsListAdapter(val context: Context, var  groups: ArrayList<Group>, var fm: FragmentManager) :
    RecyclerView.Adapter<GroupsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.group_card, parent, false))
    }

    override fun getItemCount() = groups.size

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.groupName.text = "${groups[position].name}"
        holder.groupContainer.setCardBackgroundColor(Color.parseColor("${groups[position].color}"))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var groupName = view.group_card_name
        var groupContainer = view.group_card_view

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val bundle = Bundle()
            val fragment = ConcreteGroupFragment()
            bundle.putSerializable("groupDetail", groups[position])
            fragment.arguments = bundle

            fm.beginTransaction()
                .replace(R.id.home_fragment_container, fragment)
                .addToBackStack(null)
                .commit()

        }
    }
}