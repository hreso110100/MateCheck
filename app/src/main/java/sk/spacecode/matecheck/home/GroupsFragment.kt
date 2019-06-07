package sk.spacecode.matecheck.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_groups.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.login.LoginFragment

class GroupsFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_groups, container, false)

        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE

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
            AddGroupFragment.addedUsers.clear()

        }
    }

}
