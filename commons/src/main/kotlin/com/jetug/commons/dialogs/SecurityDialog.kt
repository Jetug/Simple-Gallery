package com.jetug.commons.dialogs

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.biometric.auth.AuthPromptHost
import androidx.fragment.app.FragmentActivity
import com.jetug.commons.R
import com.jetug.commons.adapters.PasswordTypesAdapter
import com.jetug.commons.extensions.*
import com.jetug.commons.helpers.PROTECTION_FINGERPRINT
import com.jetug.commons.helpers.PROTECTION_PATTERN
import com.jetug.commons.helpers.PROTECTION_PIN
import com.jetug.commons.helpers.SHOW_ALL_TABS
import com.jetug.commons.interfaces.HashListener
import com.jetug.commons.views.MyDialogViewPager
import kotlinx.android.synthetic.main.dialog_security.view.*

class SecurityDialog(
    private val activity: FragmentActivity,
    private val requiredHash: String,
    private val showTabIndex: Int,
    private val callback: (hash: String, type: Int, success: Boolean) -> Unit
) : HashListener {
    private var dialog: AlertDialog? = null
    private val view = LayoutInflater.from(activity).inflate(R.layout.dialog_security, null)
    private var tabsAdapter: PasswordTypesAdapter
    private var viewPager: MyDialogViewPager

    init {
        view.apply {
            viewPager = findViewById(R.id.dialog_tab_view_pager)
            viewPager.offscreenPageLimit = 2
            tabsAdapter = PasswordTypesAdapter(
                context = context,
                requiredHash = requiredHash,
                hashListener = this@SecurityDialog,
                scrollView = dialog_scrollview,
                biometricPromptHost = AuthPromptHost(activity),
                showBiometricIdTab = shouldShowBiometricIdTab()
            )
            viewPager.adapter = tabsAdapter
            viewPager.onPageChangeListener {
                dialog_tab_layout.getTabAt(it)?.select()
            }

            viewPager.onGlobalLayout {
                updateTabVisibility()
            }

            if (showTabIndex == SHOW_ALL_TABS) {
                val textColor = context.baseConfig.textColor

                if (shouldShowBiometricIdTab()) {
                    val tabTitle = if (context.isTargetSdkVersion30Plus()) R.string.biometrics else R.string.fingerprint
                    dialog_tab_layout.addTab(dialog_tab_layout.newTab().setText(tabTitle), PROTECTION_FINGERPRINT)
                }

                dialog_tab_layout.setTabTextColors(textColor, textColor)
                dialog_tab_layout.setSelectedTabIndicatorColor(context.getAdjustedPrimaryColor())
                dialog_tab_layout.onTabSelectionChanged(tabSelectedAction = {
                    viewPager.currentItem = when {
                        it.text.toString().equals(resources.getString(R.string.pattern), true) -> PROTECTION_PATTERN
                        it.text.toString().equals(resources.getString(R.string.pin), true) -> PROTECTION_PIN
                        else -> PROTECTION_FINGERPRINT
                    }
                    updateTabVisibility()
                })
            } else {
                dialog_tab_layout.beGone()
                viewPager.currentItem = showTabIndex
                viewPager.allowSwiping = false
            }
        }

        dialog = AlertDialog.Builder(activity)
            .setOnCancelListener { onCancelFail() }
            .setNegativeButton(R.string.cancel) { _, _ -> onCancelFail() }
            .create().apply {
                activity.setupDialogStuff(view, this)
            }
    }

    private fun onCancelFail() {
        callback("", 0, false)
        dialog!!.dismiss()
    }

    override fun receivedHash(hash: String, type: Int) {
        callback(hash, type, true)
        if (!activity.isFinishing) {
            dialog?.dismiss()
        }
    }

    private fun updateTabVisibility() {
        for (i in 0..2) {
            tabsAdapter.isTabVisible(i, viewPager.currentItem == i)
        }
    }

    private fun shouldShowBiometricIdTab(): Boolean {
        return if (activity.isTargetSdkVersion30Plus()) {
            activity.isBiometricIdAvailable()
        } else {
            activity.isFingerPrintSensorAvailable()
        }
    }
}
