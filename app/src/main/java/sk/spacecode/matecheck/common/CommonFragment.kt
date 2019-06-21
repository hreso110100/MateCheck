package sk.spacecode.matecheck.common

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import sk.spacecode.matecheck.R

open class CommonFragment : Fragment() {

    protected lateinit var rootView: View

    protected var auth = FirebaseAuth.getInstance()
    protected var firestore = FirebaseFirestore.getInstance()
    protected var storage = FirebaseStorage.getInstance()

    /**
     * Proceed to previous fragment
     */
    protected fun View.goBack() {
        setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }
    }

    /**
     * Proceed to next fragment
     */
    protected fun View.goNext(fragment: Fragment) {
        setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.home_fragment_container, fragment)
                ?.addToBackStack(null)?.commit()
        }
    }

    /**
     * Set visibility to bottom navigation
     */
    protected fun setBottomNavVisibility(visible: Boolean) {
        if (visible) {
            activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
        } else {
            activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        }
    }

}