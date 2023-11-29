package com.saeeed.devejump.project.tailoring.presentation.ui.home

sealed class FollowingsPostsEvent {
    object NewLoad : FollowingsPostsEvent()

    object NextPageEvent : FollowingsPostsEvent()

    // restore after process death
}