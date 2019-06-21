package sk.spacecode.matecheck.home.groups.tasks


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_add_task_members.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment

class AddTaskMembersFragment : CommonFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_add_task_members, container, false)
        setBottomNavVisibility(false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.add_task_members_back_button.goBack()
    }


}
