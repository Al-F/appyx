package com.bumble.appyx.interactions.sample

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bumble.appyx.interactions.core.ui.FrameModel
import com.bumble.appyx.interactions.theme.appyx_dark
import com.bumble.appyx.samples.common.profile.Profile
import com.bumble.appyx.samples.common.profile.ProfileCard
import com.bumble.appyx.transitionmodel.cards.Cards
import com.bumble.appyx.transitionmodel.cards.CardsModel
import com.bumble.appyx.transitionmodel.cards.interpolator.CardsProps

sealed class DatingCardsNavTarget {
    class ProfileCard(val profile: Profile) : DatingCardsNavTarget()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatingCards(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val cards = remember {
        Cards(
            scope = coroutineScope,
            model = CardsModel(
                initialItems = Profile.allProfiles.shuffled().map {
                    DatingCardsNavTarget.ProfileCard(it)
                }
            ),
            interpolator = { CardsProps(it) },
            gestureFactory = { CardsProps.Gestures(it) },
        )
    }

    Children(
        modifier = modifier
            .fillMaxSize()
            .background(appyx_dark)
            .padding(16.dp),
        interactionModel = cards,
        element = {
            ElementWrapper(
                frameModel = it,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(it.navElement.key) {
                        detectDragGestures(
                            onDragStart = { position -> cards.onStartDrag(position) },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                cards.onDrag(dragAmount, density)
                            },
                            onDragEnd = {
                                cards.onDragEnd(
                                    completionThreshold = 0.15f,
                                    completeGestureSpec = spring(stiffness = Spring.StiffnessLow),
                                    revertGestureSpec = spring(stiffness = Spring.StiffnessMedium)
                                )
                            }
                        )
                    }
            )
        }
    )
}

@Composable
fun ElementWrapper(
    frameModel: FrameModel<DatingCardsNavTarget.ProfileCard, CardsModel.State>,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .then(frameModel.modifier)
            .then(modifier)
    ) {
        ProfileCard(profile = frameModel.navElement.key.navTarget.profile)
    }
}
