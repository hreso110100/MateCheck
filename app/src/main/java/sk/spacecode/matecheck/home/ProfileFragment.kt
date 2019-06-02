package sk.spacecode.matecheck.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.login.LoginActivity

class ProfileFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("Users")
            .document(firebaseAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
                rootView.home_profile_username.text = activity?.applicationContext
                    ?.getString(R.string.username, it.getString("firstName"), it.getString("surname"))

                Glide.with(this).load(it.getString("photoPath")).into(rootView.home_profile_photo)
            }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.home_profile_logout_button.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }


}
