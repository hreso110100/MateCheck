package sk.spacecode.matecheck.home.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.spacecode.matecheck.R
import sk.spacecode.matecheck.common.CommonFragment

class NotificationsFragment : CommonFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }


}
