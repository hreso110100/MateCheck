package sk.spacecode.matecheck.home.groups


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_group_name.view.*
import kotlinx.android.synthetic.main.fragment_groups.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.adapters.GroupMembersRecyclerAdapter
import sk.spacecode.matecheck.model.Group
import java.sql.Timestamp
import java.util.*

class AddGroupNameFragment : Fragment() {

    companion object {
        private const val TAG = "AddGroupNameFragment"
    }

    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        rootView = inflater.inflate(R.layout.fragment_add_group_name, container, false)

        with(rootView.groups_add_name_recycler) {
            activity?.let {
                adapter = GroupMembersRecyclerAdapter(it.applicationContext, AddGroupFragment.addedUsers)
                linearLayoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.VERTICAL, false)
                layoutManager = linearLayoutManager
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.groups_add_name_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.groups_add_name_next_button.setOnClickListener {
            val groupName = rootView.groups_name_input.text.toString()
            val creator = auth.currentUser?.uid.toString()
            val currentTime = Timestamp(System.currentTimeMillis()).time
            val usersId: ArrayList<String?> = arrayListOf()

            AddGroupFragment.addedUsers.forEach {
                usersId.add(it.id)
            }

            val group = Group(groupName, creator, currentTime, usersId)

            FirebaseFirestore.getInstance().collection("Groups")
                .document(UUID.randomUUID().toString()).set(group).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "createGroupFirestore:success")
                    } else {
                        Log.d(TAG, "createGroupFirestore:failure", it.exception)
                    }
                }
        }
    }
}

