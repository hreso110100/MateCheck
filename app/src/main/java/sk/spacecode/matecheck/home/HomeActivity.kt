package sk.spacecode.matecheck.home

import android.os.Bundle
import android.util.Log
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

    private val groupsFragment = GroupsFragment()
    private val tasksFragment = TasksFragment()
    private val profileFragment = ProfileFragment()

    companion object {
        lateinit var active: Fragment
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_groups -> {
                supportFragmentManager.beginTransaction().hide(active).show(groupsFragment).commit()
                active = groupsFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tasks -> {
                supportFragmentManager.beginTransaction().hide(active).show(tasksFragment).commit()
                active = tasksFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                supportFragmentManager.beginTransaction().hide(active).show(profileFragment).commit()
                active = profileFragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        active = groupsFragment

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        with(supportFragmentManager) {
            beginTransaction().add(R.id.home_fragment_container, profileFragment, "3").hide(profileFragment).commit()
            beginTransaction().add(R.id.home_fragment_container, tasksFragment, "2").hide(tasksFragment).commit()
            beginTransaction().add(R.id.home_fragment_container, groupsFragment, "1").commit()
        }

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
