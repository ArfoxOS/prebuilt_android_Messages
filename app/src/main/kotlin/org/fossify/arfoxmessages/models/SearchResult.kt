package org.fossify.arfoxmessages.models

data class SearchResult(val messageId: Long, val title: String, val snippet: String, val date: String, val threadId: Long, var photoUri: String)
