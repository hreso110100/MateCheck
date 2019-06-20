package sk.spacecode.matecheck.home.groups


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_groups.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.adapters.GroupsListRecyclerAdapter
import sk.spacecode.matecheck.home.groups.decorators.RecyclerViewItemDecorator
import sk.spacecode.matecheck.model.Group


class GroupsFragment : Fragment() {

    companion object {
        private const val TAG = "GroupsFragment"
    }

    private lateinit var rootView: View
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var groupList = arrayListOf<Group>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_groups, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        groupList.clear()

        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
        rootView.groups_progressBar.visibility = View.VISIBLE

        firestore.collection("Groups")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentData = document.toObject(Group::class.java)

                    if (documentData.membersID.contains(auth.currentUser?.uid)) {
                        groupList.add(documentData)
                    }
                }

                with(rootView.groups_recycle_layout) {
                    activity?.let {
                        adapter = GroupsListRecyclerAdapter(it.applicationContext, groupList, it.supportFragmentManager)
                        gridLayoutManager = GridLayoutManager(it.applicationContext, 2, RecyclerView.VERTICAL, false)
                        layoutManager = gridLayoutManager
                        addItemDecoration(RecyclerViewItemDecorator(16))
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

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.groups_float_button.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_fragment_container, AddGroupFragment())
                ?.addToBackStack(null)
                ?.commit()

            activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
            //TODO Kopirovat pole medzi AddGroup fragmentami
            AddGroupFragment.addedUsers.clear()

        }
    }

}
