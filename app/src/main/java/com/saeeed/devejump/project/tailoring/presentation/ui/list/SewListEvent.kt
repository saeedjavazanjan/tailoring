package com.saeeed.devejump.project.tailoring.presentation.ui.list

sealed class SewListEvent {
    object NewSearchEvent : SewListEvent()

    object NextPageEvent : SewListEvent()

    // restore after process death
    object RestoreStateEvent: SewListEvent()
}