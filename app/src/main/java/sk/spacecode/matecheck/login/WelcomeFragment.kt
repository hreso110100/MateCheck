package sk.spacecode.matecheck.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_welcome.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.ComponentBuilder
import sk.spacecode.matecheck.home.HomeActivity

class WelcomeFragment : Fragment() {

    companion object {
        private const val TAG = "WelcomeFragment"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var builder: ComponentBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder = ComponentBuilder()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity as Activity, gso)

        firebaseAuth = FirebaseAuth.getInstance()

        rootView.welcome_create_account_button.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_registration_fragment, RegistrationFirstFragment())?.addToBackStack(null)
                ?.commit()
        }

        rootView.welcome_login_button.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_registration_fragment, LoginFragment())?.addToBackStack(null)
                ?.commit()
        }

        rootView.welcome_google_button.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                builder.createSnackBar(rootView.welcome_google_button, "Authentication failed.", activity)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        rootView.welcome_progressBar.visibility = View.VISIBLE

        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(activity, HomeActivity::class.java))
                } else {
                    builder.createSnackBar(rootView.welcome_google_button, "Authentication failed.", activity)
                }
                rootView.welcome_progressBar.visibility = View.GONE
            }
    }

}
