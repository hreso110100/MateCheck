package sk.spacecode.matecheck.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.ConcreteGroupFragment
import sk.spacecode.matecheck.home.groups.GroupsFragment
import sk.spacecode.matecheck.home.profile.ProfileFragment
import sk.spacecode.matecheck.home.tasks.TasksFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var fragment: Fragment
    private val groupsFragment = GroupsFragment()
    private val tasksFragment = TasksFragment()
    private val profileFragment = ProfileFragment()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_groups -> {
                fragment = groupsFragment
            }
            R.id.navigation_tasks -> {
                fragment = tasksFragment
            }
            R.id.navigation_profile -> {
                fragment = profileFragment
            }
        }
        loadFragment(fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadFragment(groupsFragment)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        fragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.home_fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.home_fragment_container)

        if (fragment is ConcreteGroupFragment) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()

            supportFragmentManager.let { manager ->
                if (manager.backStackEntryCount > 0) {
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            }

        } else {
            super.onBackPressed()
        }
    }
}
