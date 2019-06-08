package sk.spacecode.matecheck.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.groups.GroupsFragment
import sk.spacecode.matecheck.home.profile.ProfileFragment
import sk.spacecode.matecheck.home.tasks.TasksFragment


class HomeActivity : AppCompatActivity() {

    private val groupsFragment = GroupsFragment()
    private val tasksFragment = TasksFragment()
    private val profileFragment = ProfileFragment()
    var active: Fragment = groupsFragment

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

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        with(supportFragmentManager) {
            beginTransaction().add(R.id.home_fragment_container, profileFragment, "3").hide(profileFragment).commit()
            beginTransaction().add(R.id.home_fragment_container, tasksFragment, "2").hide(tasksFragment).commit()
            beginTransaction().add(R.id.home_fragment_container, groupsFragment, "1").commit()
        }

    }
}
