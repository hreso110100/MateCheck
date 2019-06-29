package sk.spacecode.matecheck.home.groups.tasks


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_add_task_description.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.model.Group
import java.util.*


class AddTaskDescriptionFragment : CommonFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        group = bundle?.getSerializable("groupDetail") as Group
    }

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

        val bundle = Bundle()
        val fragment = AddTaskMembersFragment()
        bundle.putSerializable("groupDetail", group)
        fragment.arguments = bundle

        with(rootView) {
            add_task_description_back_button.goBack()
            add_task_description_next_button.goNext(fragment)

            add_task_expiration_input.setOnClickListener {
                val calendar = Calendar.getInstance()
                val dpd = DatePickerDialog.newInstance(
                    this@AddTaskDescriptionFragment,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dpd.isThemeDark = true
                dpd.accentColor = resources.getColor(R.color.colorPrimary)
                dpd.setOkColor("#FFFFFF")
                dpd.setCancelColor("#FFFFFF")
                dpd.show(activity!!.supportFragmentManager, "DatePickerDialog")
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth.${monthOfYear + 1}.$year"
        rootView.add_task_expiration_input.setText(date)
    }


}
