package org.fossify.arfoxmessages.interfaces

import androidx.room.Dao
import androidx.room.Query
import org.fossify.arfoxmessages.models.Attachment

@Dao
interface AttachmentsDao {
    @Query("SELECT * FROM attachments")
    fun getAll(): List<Attachment>
}
