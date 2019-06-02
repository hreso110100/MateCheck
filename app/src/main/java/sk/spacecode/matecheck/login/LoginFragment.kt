package sk.spacecode.matecheck.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.Validators
import sk.spacecode.matecheck.home.HomeActivity

class LoginFragment : Fragment() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_login, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = rootView.login_email_input.text.toString().trim()
                val password = rootView.login_password_input.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    rootView.login_button.isEnabled = true
                    rootView.login_button.alpha = 1.0F
                } else {
                    rootView.login_button.isEnabled = false
                    rootView.login_button.alpha = 0.5F
                }
            }

        })

        rootView.login_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.login_button.setOnClickListener {
            rootView.login_error_text.visibility = View.GONE
            val email = rootView.login_email_input.text.toString()
            val password = rootView.login_password_input.text.toString()
            signIn(email, password)
        }

        rootView.login_email_input.addTextChangedListener(textWatcher)
        rootView.login_password_input.addTextChangedListener(textWatcher)

    }

    private fun signIn(email: String, password: String) {
        rootView.login_progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(activity, HomeActivity::class.java))
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    rootView.login_error_text.visibility = View.VISIBLE
                }
                rootView.login_progressBar.visibility = View.GONE
            }
    }
}
