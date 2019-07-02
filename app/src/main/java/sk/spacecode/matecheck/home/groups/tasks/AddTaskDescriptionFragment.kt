package sk.spacecode.matecheck.home.groups.tasks


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_add_task_description.view.*
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment
import sk.spacecode.matecheck.enums.TaskPriority
import sk.spacecode.matecheck.model.Group
import sk.spacecode.matecheck.model.Task
import java.text.SimpleDateFormat
import java.util.*


class AddTaskDescriptionFragment : CommonFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var group: Group
    private var task = Task()

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
        bundle.putSerializable("taskDetail", task)
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

            task_description_radio_group.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = findViewById<RadioButton>(checkedId)
                task.priority = TaskPriority.valueOf(radioButton.text.toString().toUpperCase())
            }
        }

        val textWatcher: TextWatcher = (object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val taskName = rootView.add_task_name_input.text.toString().trim()

                if (taskName.isNotEmpty()) {
                    rootView.add_task_description_next_button.isEnabled = true
                    rootView.add_task_description_next_button.alpha = 1.0F
                    task.name = taskName
                } else {
                    rootView.add_task_description_next_button.isEnabled = false
                    rootView.add_task_description_next_button.alpha = 0.5F
                }
            }
        })
        rootView.add_task_name_input.addTextChangedListener(textWatcher)
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth.${monthOfYear + 1}.$year"
        rootView.add_task_expiration_input.setText(date)

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        val parsedDate = formatter.parse(date)
        task.dateOfExpiration = parsedDate.time
    }
}
