package org.fossify.arfoxmessages.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.fossify.commons.extensions.notificationManager
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.arfoxmessages.extensions.conversationsDB
import org.fossify.arfoxmessages.extensions.deleteMessage
import org.fossify.arfoxmessages.extensions.updateLastConversationMessage
import org.fossify.arfoxmessages.extensions.updateUnreadCountBadge
import org.fossify.arfoxmessages.helpers.IS_MMS
import org.fossify.arfoxmessages.helpers.MESSAGE_ID
import org.fossify.arfoxmessages.helpers.THREAD_ID
import org.fossify.arfoxmessages.helpers.refreshMessages

class DeleteSmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val threadId = intent.getLongExtra(THREAD_ID, 0L)
        val messageId = intent.getLongExtra(MESSAGE_ID, 0L)
        val isMms = intent.getBooleanExtra(IS_MMS, false)
        context.notificationManager.cancel(threadId.hashCode())
        ensureBackgroundThread {
            context.deleteMessage(messageId, isMms)
            context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
            context.updateLastConversationMessage(threadId)
            refreshMessages()
        }
    }
}
