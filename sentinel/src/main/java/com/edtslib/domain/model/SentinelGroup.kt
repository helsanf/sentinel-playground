package com.edtslib.domain.model

sealed  class SentinelGroup {
    data object Click: SentinelGroup() {
        override fun toString() = "CLICK"
    }
}