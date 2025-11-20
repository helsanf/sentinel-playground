package com.edtslib.domain.model

sealed  class SentinelGroup {
    object Click: SentinelGroup() {
        override fun toString() = "CLICK"
    }
}