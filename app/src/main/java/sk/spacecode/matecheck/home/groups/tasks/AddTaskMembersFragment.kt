package sk.spacecode.matecheck.home.groups.tasks


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_add_task_members.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.home.groups.adapters.GroupMemberPhotoAdapter
import sk.spacecode.matecheck.home.groups.adapters.GroupMemberSelectableAdapter
import sk.spacecode.matecheck.home.tasks.ConcreteTaskFragment
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.User

class AddTaskMembersFragment : CommonFragment() {
    private var membersList = arrayListOf<User>()
    private var assignedList = arrayListOf<User>()
    private lateinit var group: Group
    private var selectableRecyclerAdapter: GroupMemberSelectableAdapter? = null
    private lateinit var assignedRecyclerAdapter: GroupMemberPhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        group = bundle?.getSerializable("groupDetail") as Group
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_add_task_members, container, false)

        activity?.let {

            with(rootView.add_task_members_added_users_recycler) {
                layoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.HORIZONTAL, false)
                assignedRecyclerAdapter =
                    GroupMemberPhotoAdapter(it.applicationContext, assignedList, rootView, selectableRecyclerAdapter)
                adapter = assignedRecyclerAdapter
            }

            with(rootView.add_task_members_recycler) {
                layoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.VERTICAL, false)
                selectableRecyclerAdapter = GroupMemberSelectableAdapter(
                    it.applicationContext,
                    membersList,
                    assignedList,
                    rootView,
                    assignedRecyclerAdapter
                )
                adapter = selectableRecyclerAdapter
            }

            assignedRecyclerAdapter.selectableAdapter = selectableRecyclerAdapter
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavVisibility(false)
        getUsers()

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val member = rootView.add_task_members_input.text.toString().trim()
                selectableRecyclerAdapter?.filter?.filter(member)
            }
        })

        with(rootView) {
            add_task_members_progressBar.visibility = View.VISIBLE
            add_task_members_input.addTextChangedListener(textWatcher)
            add_task_members_back_button.goBack()
            add_task_members_next_button.goNext(ConcreteTaskFragment())
        }
    }

    private fun getUsers() {
        firestore.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    if (user.id in group.membersID) {
                        membersList.add(user)
                    }
                }
                rootView.add_task_members_recycler.adapter?.notifyDataSetChanged()

            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.add_task_members_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.add_task_members_progressBar.visibility = View.GONE
                Log.w(AddTaskMembersFragment::class.simpleName, "Error getting documents: ", exception)
            }
    }
}
