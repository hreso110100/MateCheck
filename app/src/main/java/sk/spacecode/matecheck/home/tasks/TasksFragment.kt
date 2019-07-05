package sk.spacecode.matecheck.home.tasks


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_concrete_group.view.*
import kotlinx.android.synthetic.main.fragment_tasks.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.enums.TaskState
import sk.spacecode.matecheck.home.groups.ConcreteGroupFragment
import sk.spacecode.matecheck.home.tasks.adapters.TasksAdapter
import sk.spacecode.matecheck.model.Task
import sk.spacecode.matecheck.model.User

class TasksFragment : CommonFragment(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var tasks: ArrayList<Task>
    private lateinit var users: ArrayList<User>
    private var completedCounter = 0
    private var inProgressCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tasks = arrayListOf()
        users = arrayListOf()
        completedCounter = 0
        inProgressCounter = 0

        rootView = inflater.inflate(R.layout.fragment_tasks, container, false)
        rootView.tasks_appBarLayout.addOnOffsetChangedListener(this)

        setBottomNavVisibility(true)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.tasks_progressBar.visibility = View.VISIBLE
        getTask()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) =
        when {
            Math.abs(verticalOffset) == appBarLayout?.totalScrollRange -> {
                rootView.tasks_toolbar_text.alpha = 1F
                rootView.tasks_toolbar_text.text = getString(R.string.your_tasks)
            }
            verticalOffset == 0 -> {
                rootView.tasks_toolbar_text.text = ""
            }
            else -> {
                rootView.tasks_toolbar_text.text = getString(R.string.your_tasks)
                rootView.tasks_toolbar_text.alpha = Math.abs(verticalOffset.toFloat()) / 1000
            }
        }

//    private fun getUsers() {
//        firestore.collection("Users")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val documentData = document.toObject(User::class.java)
//
//                    if (documentData.ID == group.creatorID) {
//                        rootView.concrete_group_text.text = group.name
//                        rootView.concrete_group_creator_text.text =
//                            "By ${documentData.firstName} ${documentData.surname}"
//                    }
//                    users.add(documentData)
//                }
//            }.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    rootView.concrete_group_progressBar.visibility = View.GONE
//                }
//            }
//            .addOnFailureListener { exception ->
//                rootView.concrete_group_progressBar.visibility = View.GONE
//                Log.w(ConcreteGroupFragment::class.simpleName, "Error getting documents: ", exception)
//            }
//    }

    private fun getTask() {
        firestore.collection("Tasks")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(Task::class.java)

                    if (auth.currentUser?.uid in documentData.membersID) {
                        tasks.add(documentData)

                        if (documentData.status == TaskState.DONE) {
                            completedCounter++
                        } else if (documentData.status == TaskState.IN_PROGRESS) {
                            inProgressCounter++
                        }
                    }
                }

                with(rootView.tasks_recycle_layout) {
                    activity?.let {
                        adapter = TasksAdapter(
                            it.applicationContext,
                            tasks,
                            users,
                            rootView
                        )
                        layoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.VERTICAL, false)
                    }
                }

            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.tasks_progressBar.visibility = View.GONE

                    activity?.let {
                        rootView.tasks_counter_text.text =
                            getString(R.string.task_counter, completedCounter, inProgressCounter)
                    }
                }
            }
            .addOnFailureListener { exception ->
                rootView.tasks_progressBar.visibility = View.GONE
                Log.w(ConcreteGroupFragment::class.simpleName, "Error getting documents: ", exception)
            }
    }
}
