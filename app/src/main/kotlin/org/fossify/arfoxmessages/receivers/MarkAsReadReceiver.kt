package org.fossify.arfoxmessages.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.fossify.commons.extensions.notificationManager
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.arfoxmessages.extensions.conversationsDB
import org.fossify.arfoxmessages.extensions.markThreadMessagesRead
import org.fossify.arfoxmessages.extensions.updateUnreadCountBadge
import org.fossify.arfoxmessages.helpers.MARK_AS_READ
import org.fossify.arfoxmessages.helpers.THREAD_ID
import org.fossify.arfoxmessages.helpers.refreshMessages

class MarkAsReadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MARK_AS_READ -> {
                val threadId = intent.getLongExtra(THREAD_ID, 0L)
                context.notificationManager.cancel(threadId.hashCode())
                ensureBackgroundThread {
                    context.markThreadMessagesRead(threadId)
                    context.conversationsDB.markRead(threadId)
                    context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
                    refreshMessages()
                }
            }
        }
    }
}
