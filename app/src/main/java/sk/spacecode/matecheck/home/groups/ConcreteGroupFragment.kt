package sk.spacecode.matecheck.home.groups

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_concrete_group.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.home.tasks.adapters.TasksGroupAdapter
import sk.spacecode.matecheck.home.groups.tasks.AddTaskDescriptionFragment
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.Task
import sk.spacecode.matecheck.model.User


class ConcreteGroupFragment : CommonFragment(), AppBarLayout.OnOffsetChangedListener {
    private lateinit var group: Group
    private lateinit var users: ArrayList<User>
    private lateinit var tasks: ArrayList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        group = bundle?.getSerializable("groupDetail") as Group
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tasks = arrayListOf()
        users = arrayListOf()
        rootView = inflater.inflate(R.layout.fragment_concrete_group, container, false)

        setBackgroundGradient()
        setBottomNavVisibility(true)

        rootView.concrete_group_add_task_button.setOnClickListener {
            val bundle = Bundle()
            val fragment = AddTaskDescriptionFragment()
            bundle.putSerializable("groupDetail", group)
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        rootView.concrete_group_appBarLayout.addOnOffsetChangedListener(this)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.concrete_group_progressBar.visibility = View.VISIBLE
        getUsers()
        getTask()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) =
        when {
            Math.abs(verticalOffset) == appBarLayout?.totalScrollRange -> {
                rootView.concrete_group_toolbar_text.alpha = 1F
                rootView.concrete_group_toolbar_text.text = group.name
            }
            verticalOffset == 0 -> {
                rootView.concrete_group_toolbar_text.text = ""
            }
            else -> {
                rootView.concrete_group_toolbar_text.text = group.name
                rootView.concrete_group_toolbar_text.alpha = Math.abs(verticalOffset.toFloat()) / 1000
            }
        }

    private fun getUsers() {
        firestore.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(User::class.java)

                    if (documentData.ID == group.creatorID) {
                        rootView.concrete_group_text.text = group.name
                        rootView.concrete_group_creator_text.text =
                            "By ${documentData.firstName} ${documentData.surname}"
                    }
                    users.add(documentData)
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.concrete_group_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.concrete_group_progressBar.visibility = View.GONE
                Log.w(ConcreteGroupFragment::class.simpleName, "Error getting documents: ", exception)
            }
    }

    private fun getTask() {
        firestore.collection("Tasks")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(Task::class.java)

                    if (documentData.groupID == group.ID) {
                        tasks.add(documentData)
                    }
                }

                with(rootView.concrete_group_recycle_layout) {
                    activity?.let {
                        adapter = TasksGroupAdapter(
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
                    rootView.concrete_group_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.concrete_group_progressBar.visibility = View.GONE
                Log.w(ConcreteGroupFragment::class.simpleName, "Error getting documents: ", exception)
            }
    }

    private fun setBackgroundGradient() {
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor(group.color), Color.parseColor("#000000"), Color.parseColor("#000000"))
        )

        rootView.concrete_groups_root_layout.background = gradient
    }
}
