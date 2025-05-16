package org.fossify.arfoxmessages.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import org.fossify.commons.extensions.showErrorToast
import org.fossify.commons.helpers.ensureBackgroundThread
import org.fossify.arfoxmessages.extensions.conversationsDB
import org.fossify.arfoxmessages.extensions.deleteScheduledMessage
import org.fossify.arfoxmessages.extensions.getAddresses
import org.fossify.arfoxmessages.extensions.messagesDB
import org.fossify.arfoxmessages.helpers.SCHEDULED_MESSAGE_ID
import org.fossify.arfoxmessages.helpers.THREAD_ID
import org.fossify.arfoxmessages.helpers.refreshMessages
import org.fossify.arfoxmessages.messaging.sendMessageCompat

class ScheduledMessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "simple.messenger:scheduled.message.receiver")
        wakelock.acquire(3000)


        ensureBackgroundThread {
            handleIntent(context, intent)
        }
    }

    private fun handleIntent(context: Context, intent: Intent) {
        val threadId = intent.getLongExtra(THREAD_ID, 0L)
        val messageId = intent.getLongExtra(SCHEDULED_MESSAGE_ID, 0L)
        val message = try {
            context.messagesDB.getScheduledMessageWithId(threadId, messageId)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        val addresses = message.participants.getAddresses()
        val attachments = message.attachment?.attachments ?: emptyList()

        try {
            Handler(Looper.getMainLooper()).post {
                context.sendMessageCompat(message.body, addresses, message.subscriptionId, attachments)
            }

            // delete temporary conversation and message as it's already persisted to the telephony db now
            context.deleteScheduledMessage(messageId)
            context.conversationsDB.deleteThreadId(messageId)
            refreshMessages()
        } catch (e: Exception) {
            context.showErrorToast(e)
        } catch (e: Error) {
            context.showErrorToast(e.localizedMessage ?: context.getString(org.fossify.commons.R.string.unknown_error_occurred))
        }
    }
}
