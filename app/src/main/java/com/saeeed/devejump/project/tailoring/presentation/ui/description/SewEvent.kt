package com.saeeed.devejump.project.tailoring.presentation.ui.description

sealed class SewEvent {
    data class GetSewEvent(
        val id: Int
    ): SewEvent()

}