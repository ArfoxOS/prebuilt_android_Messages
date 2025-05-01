package org.fossify.arfoxmessages.activities

import org.fossify.commons.activities.BaseSimpleActivity
import org.fossify.arfoxmessages.R

open class SimpleActivity : BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)

    override fun getRepositoryName() = "Messages"
}
