package com.saeeed.devejump.project.tailoring.presentation.ui.post

sealed class PostsEvent {
    object NewSearchEvent : PostsEvent()

    object NextPageEvent : PostsEvent()

    // restore after process death
    object RestoreStateEvent: PostsEvent()
}