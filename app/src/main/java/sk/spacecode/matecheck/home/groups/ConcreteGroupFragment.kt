package sk.spacecode.matecheck.home.groups

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_concrete_group.*
import kotlinx.android.synthetic.main.fragment_concrete_group.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.adapters.GroupMembersRecyclerAdapter
import sk.spacecode.matecheck.home.groups.tasks.AddTaskDescriptionFragment
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.User





class ConcreteGroupFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    companion object {
        private const val TAG = "ConcreteGroupFragment"
    }

    private lateinit var rootView: View
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var group: Group
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        group = bundle?.getSerializable("groupDetail") as Group
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_concrete_group, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val gradient = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(Color.parseColor(group.color),Color.parseColor("#000000"),Color.parseColor("#000000")))
        rootView.concrete_groups_root_layout.background=gradient

        with(rootView.concrete_group_recycle_layout) {
            activity?.let {
                adapter = GroupMembersRecyclerAdapter(it.applicationContext, AddGroupFragment.addedUsers)
                linearLayoutManager = LinearLayoutManager(it.applicationContext, RecyclerView.VERTICAL, false)
                layoutManager = linearLayoutManager

            }
        }

        rootView.concrete_group_add_task_button.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_fragment_container, AddTaskDescriptionFragment())
                ?.addToBackStack(null)
                ?.commit()
        }

        rootView.concrete_group_appBarLayout.addOnOffsetChangedListener(this)

        return rootView
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rootView.concrete_group_progressBar.visibility = View.VISIBLE
        var userName: String

        firestore.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(User::class.java)

                    if (documentData.id == group.creatorID) {
                        userName = "${documentData.firstName} ${documentData.surname}"
                        rootView.concrete_group_text.text = group.name
                        rootView.concrete_group_creator_text.text = "By $userName"

                            break
                    }
                }
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    rootView.concrete_group_progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                rootView.concrete_group_progressBar.visibility = View.GONE
                Log.w(TAG, "Error getting documents: ", exception)
            }
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
}
