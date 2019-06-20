package sk.spacecode.matecheck.login.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_registration_second.*
import kotlinx.android.synthetic.main.fragment_registration_second.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.ComponentBuilder
import sk.spacecode.matecheck.home.HomeActivity
import sk.spacecode.matecheck.model.User
import java.io.File


class RegistrationSecondFragment : Fragment() {

    companion object {
        private const val TAG = "RegSecondFragment"
    }

    private lateinit var rootView: View
    private lateinit var builder: ComponentBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        builder = ComponentBuilder()
        rootView = inflater.inflate(R.layout.fragment_registration_second, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = rootView.registration_account_email_input.text.toString().trim()
                val password = rootView.registration_account_password_input.text.toString().trim()

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8) {
                    rootView.registration_account_next_button.isEnabled = true
                    rootView.registration_account_next_button.alpha = 1.0F
                } else {
                    rootView.registration_account_next_button.isEnabled = false
                    rootView.registration_account_next_button.alpha = 0.5F
                }
            }

        })

        rootView.registration_account_email_input.addTextChangedListener(textWatcher)
        rootView.registration_account_password_input.addTextChangedListener(textWatcher)

        rootView.registration_account_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.registration_account_next_button.setOnClickListener {
            createAccount(
                registration_account_email_input.text.toString(),
                registration_account_password_input.text.toString()
            )
        }
    }

    private fun createAccount(email: String, password: String) {
        rootView.registration_account_progressBar.visibility = View.VISIBLE

        val auth = FirebaseAuth.getInstance()
        val storage = FirebaseStorage.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    val bundle = this.arguments
                    bundle?.let {
                        val firstName = bundle.getString("firstName")
                        val surName = bundle.getString("surname")
                        val photoPath = bundle.getString("photoPath")
                        val userId = auth.currentUser?.uid.toString()
                        val userData = User(userId, firstName, surName, photoPath, email)

                        val photoURI = Uri.fromFile(File(photoPath))
                        val storageRef = storage.reference.child("profilePhotos/$userId")

                        storageRef.putFile(photoURI)
                            .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let {
                                        throw it
                                    }
                                }
                                return@Continuation storageRef.downloadUrl
                            }).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    userData.photoPath = storageRef.downloadUrl.toString()
                                } else {

                                }
                            }

                        FirebaseFirestore.getInstance().collection("Users")
                            .document(userId).set(userData).addOnCompleteListener {
                                if (task.isSuccessful) {
                                    Log.d(TAG, "createUserFirestore:success")
                                } else {
                                    Log.d(TAG, "createUserFirestore:failure", task.exception)
                                }
                            }
                    }
                    startActivity(Intent(activity, HomeActivity::class.java))
                } else {
                    builder.createSnackBar(rootView.registration_account_next_button, "Registration failed.", activity)
                }
                rootView.registration_account_progressBar.visibility = View.GONE
            }
    }
}
