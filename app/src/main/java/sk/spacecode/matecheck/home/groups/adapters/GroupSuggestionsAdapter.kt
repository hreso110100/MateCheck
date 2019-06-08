package sk.spacecode.matecheck.home.groups.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import org.apache.commons.lang3.StringUtils
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User

class GroupSuggestionsAdapter(context: Context, users: ArrayList<User>) :
    ArrayAdapter<User>(context, 0, users), Filterable {

    private var userList: ArrayList<User> = ArrayList(users)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertedView = convertView

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertedView = inflater.inflate(
                R.layout.users_layout_row, parent, false
            )
        }
        val user = getItem(position)
        val nameUser = convertedView?.findViewById<TextView>(R.id.users_row_name)
        val imageUser = convertedView?.findViewById<ImageView>(R.id.users_row_photo)

        user?.let {
            nameUser?.text = "${user.firstName} ${user.surname}"
            Glide.with(this.context).load(user.photoPath).into(imageUser!!)
        }

        return convertedView
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestions = ArrayList<User>()

                constraint?.let {
                    val filterPattern = StringUtils.stripAccents(constraint.toString().toLowerCase().trim())

                    for (user in userList) {
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
                }
                results.values = suggestions
                results.count = suggestions.size

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as ArrayList<User>)
                notifyDataSetChanged()
            }
        }
    }


}