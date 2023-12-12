package com.saeeed.devejump.project.tailoring.presentation.ui.peoples.followers

sealed class FollowersEvent {

    object NewEvent : FollowersEvent()

    object NextPageEvent : FollowersEvent()

    // restore after process death
    object RestoreStateEvent: FollowersEvent()
}