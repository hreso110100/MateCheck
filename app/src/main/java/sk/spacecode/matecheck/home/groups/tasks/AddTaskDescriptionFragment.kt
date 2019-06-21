package sk.spacecode.matecheck.home.groups.tasks


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_task_description.view.*

import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment

class AddTaskDescriptionFragment : CommonFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_add_task_description, container, false)
        setBottomNavVisibility(false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rootView) {
            add_task_description_back_button.goBack()
            task_description_next_button.goNext(AddTaskMembersFragment())
        }

    }


}
