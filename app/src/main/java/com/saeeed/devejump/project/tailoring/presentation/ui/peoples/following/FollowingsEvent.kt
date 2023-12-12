package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.following

sealed class FollowingsEvent {

    object NewEvent : FollowingsEvent()

    object NextPageEvent : FollowingsEvent()

    // restore after process death
    object RestoreStateEvent: FollowingsEvent()
}