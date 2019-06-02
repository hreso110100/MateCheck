package sk.spacecode.matecheck.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    public override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.login_registration_fragment, WelcomeFragment()).addToBackStack(null)
            .commit()

    }
}