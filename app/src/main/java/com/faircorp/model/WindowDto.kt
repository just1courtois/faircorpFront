package com.faircorp.model

import com.faircorp.Status

enum class Status { OPEN, CLOSED}

data class WindowDto(val id: Long,
                     val name: String,
                     val room: RoomDto,
                     val status: Status,
                     val roomId: Long)
