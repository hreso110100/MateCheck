package sk.spacecode.matecheck.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pchmn.materialchips.ChipView
import kotlinx.android.synthetic.main.fragment_add_group.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.model.User


class AddGroupFragment : Fragment() {

    companion object {
        private const val TAG = "AddGroupFragment"
        var addedUsers = ArrayList<User>()
    }

    private lateinit var rootView: View
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var suggestionUsers = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        rootView = inflater.inflate(R.layout.fragment_add_group, container, false)

        return rootView
    }

    private fun setSearchBoxConstraint(value: Float) {
        val set = ConstraintSet()
        val layout = rootView.groups_add_root_layout
        set.clone(layout)
        set.setVerticalBias(R.id.groups_add_search, value)
        set.applyTo(layout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.groups_add_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.groups_add_next_button.setOnClickListener {
            val fragment = AddGroupNameFragment()

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()

            activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        }

        firestore.collection("Users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.toObject(User::class.java).id == auth.currentUser?.uid) {
                        addedUsers.add(document.toObject(User::class.java))
                    } else {
                        suggestionUsers.add(document.toObject(User::class.java))
                    }
                }
                val adapter = GroupAdapter(context!!, suggestionUsers)

                rootView.groups_add_search.threshold = 1
                rootView.groups_add_search.setAdapter(adapter)

                rootView.groups_add_search.setOnItemClickListener { _, _, position, _ ->
                    val chip = ChipView(context)
                    chip.setDeletable(true)
                    chip.setLabelColor(resources.getColor(android.R.color.white))
                    chip.setDeleteIconColor(resources.getColor(android.R.color.white))
                    chip.setChipBackgroundColor(resources.getColor(R.color.colorPrimary))
                    setSearchBoxConstraint(0.05F)

                    if (!addedUsers.contains(suggestionUsers[position])) {
                        chip.label = suggestionUsers[position].firstName
                        rootView.groups_chipGroup.addView(chip as View)

                        addedUsers.add(suggestionUsers[position])
                    } else {
                        Toast.makeText(context, "User already added", Toast.LENGTH_SHORT).show()
                    }

                    if (addedUsers.size >= 2) {
                        rootView.groups_add_next_button.isEnabled = true
                        rootView.groups_add_next_button.alpha = 1.0F
                    }

                    chip.setOnDeleteClicked {
                        rootView.groups_chipGroup.removeView(chip as View)

                        if (rootView.groups_chipGroup.childCount == 0) {
                            rootView.groups_add_next_button.isEnabled = false
                            rootView.groups_add_next_button.alpha = 0.5F
                            setSearchBoxConstraint(0.07F)
                        }
                        addedUsers.remove(addedUsers.find { it.firstName == chip.label })
                    }
                    rootView.groups_add_search.text.clear()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}
