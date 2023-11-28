package com.saeeed.devejump.project.tailoring.presentation.ui.search

sealed class SearchEvent {
    object NewSearchEvent : SearchEvent()

    object NextPageEvent : SearchEvent()

    // restore after process death
    object RestoreStateEvent: SearchEvent()
}