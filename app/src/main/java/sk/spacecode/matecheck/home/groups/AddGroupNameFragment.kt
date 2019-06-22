package sk.spacecode.matecheck.home.groups


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_group_name.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.enums.Colors
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

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val groupName = rootView.groups_name_input.text.toString().trim()

                if (groupName.isNotEmpty()) {
                    rootView.groups_add_name_next_button.isEnabled = true
                    rootView.groups_add_name_next_button.alpha = 1.0F
                } else {
                    rootView.groups_add_name_next_button.isEnabled = false
                    rootView.groups_add_name_next_button.alpha = 0.5F
                }
            }
        })

        rootView.groups_name_input.addTextChangedListener(textWatcher)


        rootView.groups_add_name_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.groups_add_name_next_button.setOnClickListener {
            rootView.groups_add_progressBar.visibility = View.VISIBLE

            val groupName = rootView.groups_name_input.text.toString()
            val creator = auth.currentUser?.uid.toString()
            val currentTime = Timestamp(System.currentTimeMillis()).time
            val usersId: ArrayList<String> = arrayListOf()
            val color : String = Colors.pickRandom()

            AddGroupFragment.addedUsers.forEach {
                usersId.add(it.id!!)
            }

            val group = Group(groupName, creator, currentTime, usersId, color)

            FirebaseFirestore.getInstance().collection("Groups")
                .document(UUID.randomUUID().toString()).set(group).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "createGroupFirestore:success")
                    } else {
                        Log.d(TAG, "createGroupFirestore:failure", it.exception)
                    }

                    rootView.groups_add_progressBar.visibility = View.GONE

                    val bundle = Bundle()
                    val fragment = ConcreteGroupFragment()
                    bundle.putSerializable("groupDetail", group)
                    fragment.arguments = bundle

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.home_fragment_container, fragment)
                        ?.commit()

                }
        }
    }
}

