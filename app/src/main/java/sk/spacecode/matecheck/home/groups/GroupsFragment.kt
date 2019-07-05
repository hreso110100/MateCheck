package sk.spacecode.matecheck.home.groups


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_groups.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.home.groups.adapters.GroupsListAdapter
import sk.spacecode.matecheck.home.groups.decorators.RecyclerViewHorizontalDecorator
import sk.spacecode.matecheck.home.groups.decorators.RecyclerViewItemDecorator
import sk.spacecode.matecheck.model.BaseModel
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.Task
import sk.spacecode.matecheck.model.User


class GroupsFragment : CommonFragment(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        private const val TAG = "GroupsFragment"
    }

    private lateinit var favouriteGroupsList: ArrayList<Group>
    private lateinit var otherGroupsList: ArrayList<Group>
    private lateinit var tasksList: ArrayList<Task>
    private lateinit var currentUserData: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_groups, container, false)
        rootView.groups_appBarLayout.addOnOffsetChangedListener(this)

        favouriteGroupsList = arrayListOf()
        otherGroupsList = arrayListOf()
        tasksList = arrayListOf()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.groups_progressBar.visibility = View.VISIBLE

        firestore.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                documents.forEach {
                    if (auth.currentUser?.uid == it.toObject(User::class.java).ID) {
                        currentUserData = it.toObject(User::class.java)
                    }
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.groups_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.groups_progressBar.visibility = View.GONE
                Log.w(TAG, "Error getting documents: ", exception)
            }

        firestore.collection("Tasks")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tasksList.add(document.toObject(Task::class.java))
                }

            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.groups_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.groups_progressBar.visibility = View.GONE
                Log.w(TAG, "Error getting documents: ", exception)
            }

        firestore.collection("Groups")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(Group::class.java)

                    if (documentData.membersID.contains(auth.currentUser?.uid)) {

                        if (documentData.ID in currentUserData.favouriteGroups) {
                            favouriteGroupsList.add(documentData)
                        } else {
                            otherGroupsList.add(documentData)
                        }
                    }
                }

                with(rootView.groups_recycle_layout) {
                    activity?.let {
                        adapter = GroupsListAdapter(it.applicationContext, otherGroupsList, tasksList, activity)
                        layoutManager = GridLayoutManager(it.applicationContext, 2, RecyclerView.VERTICAL, false)
                        addItemDecoration(RecyclerViewItemDecorator(16))
                    }
                }

                with(rootView.groups_recycle_layout_starred) {
                    activity?.let {
                        adapter = GroupsListAdapter(it.applicationContext, favouriteGroupsList, tasksList, activity)
                        layoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.HORIZONTAL, false)
                        addItemDecoration(RecyclerViewHorizontalDecorator(64))
                    }
                }

            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.groups_progressBar.visibility = View.GONE

                    activity?.let {
                        rootView.groups_counter_text.text = getString(R.string.groups_counter, favouriteGroupsList.size)
                    }
                }
            }
            .addOnFailureListener { exception ->
                rootView.groups_progressBar.visibility = View.GONE
                Log.w(TAG, "Error getting documents: ", exception)
            }

        rootView.groups_add_group_button.goNext(AddGroupFragment())
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) =
        when {
            Math.abs(verticalOffset) == appBarLayout?.totalScrollRange -> {
                rootView.groups_toolbar_text.alpha = 1F
                rootView.groups_toolbar_text.text = getString(R.string.groups_text)
            }
            verticalOffset == 0 -> {
                rootView.groups_toolbar_text.text = ""
            }
            else -> {
                rootView.groups_toolbar_text.text = getString(R.string.groups_text)
                rootView.groups_toolbar_text.alpha = Math.abs(verticalOffset.toFloat()) / 1000
            }
        }
}
