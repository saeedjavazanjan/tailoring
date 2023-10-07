package com.saeeed.devejump.project.tailoring.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

enum class HeartButtonState {
    IDLE, ACTIVE
}

val color = ColorPropKey()
val size = DpPropKey()

@ExperimentalCoroutinesApi
@Composable
fun AnimatedHeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartButtonState>,
    onToggle: () -> Unit,
    iconSize: Dp,
    expandIconSize: Dp,
) {

    val transitionDefinition = transitionDefinition<HeartButtonState> {
        state(HeartButtonState.IDLE) {
            this[color] = Color.LightGray
            this[size] = iconSize
        }

        state(HeartButtonState.ACTIVE) {
            this[color] = Color.Red
            this[size] = iconSize
        }

        transition(HeartButtonState.IDLE to HeartButtonState.ACTIVE) {
            color using tween(durationMillis = 500)

            size using keyframes {
                durationMillis = 500
                expandIconSize at 100
                iconSize at 200
            }
        }

        transition(HeartButtonState.ACTIVE to HeartButtonState.IDLE) {
            color using tween(durationMillis = 500)

            size using keyframes {
                durationMillis = 500
                expandIconSize at 100
                iconSize at 200
            }
        }
    }

    val toState = if (buttonState.value == HeartButtonState.IDLE) {
        HeartButtonState.ACTIVE
    } else {
        HeartButtonState.IDLE
    }

    val state = transition(
        definition = transitionDefinition,
        initState = buttonState.value,
        toState = toState
    )

    HeartButton(
        modifier = modifier,
        buttonState = buttonState,
        state = state,
        onToggle = onToggle
    )
}

@ExperimentalCoroutinesApi
@Composable
private fun HeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartButtonState>,
    state: TransitionState,
    onToggle: () -> Unit,
) {
    if (buttonState.value == HeartButtonState.ACTIVE) {
        loadPicture(drawable = R.drawable.heart_red).value?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                modifier = modifier
                    .clickable(
                        onClick = onToggle,
                        indication = null,
                    )
                    .width(state[size])
                    .height(state[size])
                ,
            )
        }
    } else {
        loadPicture(drawable = R.drawable.heart_grey).value?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                modifier = modifier
                    .clickable(
                        onClick = onToggle,
                        indication = null,
                    )
                    .width(state[size])
                    .height(state[size]),
            )
        }
    }
}