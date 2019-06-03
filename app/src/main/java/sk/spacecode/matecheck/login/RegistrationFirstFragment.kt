package sk.spacecode.matecheck.login

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.common.reflect.Reflection.getPackageName
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_registration_first.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.home.HomeActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RegistrationFirstFragment : Fragment() {

    companion object {
        private const val TAG = "RegFirstFragment"
    }

    private var imageFilePath: String? = ""
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_registration_first, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(imageFilePath).placeholder(R.drawable.ic_person_white_80dp)
            .into(rootView.registration_upload_photo)

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = rootView.registration_first_name_input.text.toString().trim()
                val password = rootView.registration_surname_input.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    rootView.registration_next_button.isEnabled = true
                    rootView.registration_next_button.alpha = 1.0F
                } else {
                    rootView.registration_next_button.isEnabled = false
                    rootView.registration_next_button.alpha = 0.5F
                }
            }

        })

        rootView.registration_first_name_input.addTextChangedListener(textWatcher)
        rootView.registration_surname_input.addTextChangedListener(textWatcher)

        rootView.registration_back_button.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }

        rootView.registration_upload_photo.setOnClickListener {

            Dexter.withActivity(activity)
                .withPermission(READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        uploadPhoto()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        response?.let {
                            if (response.isPermanentlyDenied) {
                                openSettingsDialog()
                            } else {
                                startActivity(Intent(activity, LoginActivity::class.java))
                            }
                        }
                    }
                })
                .check()
        }

        rootView.registration_next_button.setOnClickListener {
            val firstName = rootView.registration_first_name_input.text.toString()
            val surname = rootView.registration_surname_input.text.toString()
            val photoPath = imageFilePath

            val bundle = Bundle()

            with(bundle) {
                putString("firstName", firstName)
                putString("surname", surname)
                putString("photoPath", photoPath)
            }

            val fragment = RegistrationSecondFragment()
            fragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.login_registration_fragment, fragment)
                ?.addToBackStack(null)?.commit()
        }
    }

    private fun uploadPhoto() {
        val alertDialog = activity?.let { AlertDialog.Builder(it) }

        alertDialog?.let {
            it.setTitle("Add profile photo")

            alertDialog.setItems(arrayOf("Upload from gallery", "Use camera")) { _, which ->
                if (which == 0) {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, 0)
                } else if (which == 1) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val photoURI =
                        activity?.let { context ->
                            FileProvider.getUriForFile(
                                context,
                                "sk.spacecode.matecheck.provider", createImageFile()
                            )
                        }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 1)
                }
            }
            it.create()
            it.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                val imageURI = imageReturnedIntent?.data

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity?.contentResolver?.query(imageURI, filePathColumn, null, null, null)
                cursor?.moveToFirst()
                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                imageFilePath = columnIndex?.let { cursor.getString(it) }
                cursor?.close()

                Glide.with(this).load(imageFilePath).into(rootView.registration_upload_photo)
            }
            1 -> if (resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(imageFilePath).into(rootView.registration_upload_photo)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = activity?.getExternalFilesDir(DIRECTORY_PICTURES)

        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        imageFilePath = image.absolutePath

        return image
    }

    private fun openSettingsDialog() {
        val alertDialog = activity?.let { AlertDialog.Builder(it) }

        alertDialog?.let {

            it.setTitle("Required permission")
            it.setMessage("Application requires permissions to continue. Grant them in settings.")
            it.setPositiveButton("Go to SETTINGS") { dialog, _ ->
                dialog.cancel()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity?.packageName, null)
                intent.data = uri
                startActivityForResult(intent, 101)
            }
            it.create()
            it.show()
        }
    }
}
