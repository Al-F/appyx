package com.bumble.appyx.utils.viewmodel.integration

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.bumble.appyx.navigation.integration.ActivityIntegrationPoint

class ActivityIntegrationPointWithViewModel(
    activity: ComponentActivity,
    savedInstanceState: Bundle?,
) : ActivityIntegrationPoint(activity, savedInstanceState) {

    val viewModel = IntegrationPointViewModel.getInstance(activity)
}
