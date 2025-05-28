package com.reena.aamp.terminal

import com.termux.terminal.TerminalEmulator
import com.termux.terminal.TerminalSession

class TerminalSessionClient(private val onTextChange: () -> Unit) :
    com.termux.terminal.TerminalSessionClient {

    private var cwd: String? = null

    override fun logError(tag: String, message: String) {}

    override fun logWarn(tag: String, message: String) {}

    override fun logInfo(tag: String, message: String) {}

    override fun logDebug(tag: String, message: String) {}

    override fun logVerbose(tag: String, message: String) {}

    override fun logStackTraceWithMessage(tag: String, message: String, e: Exception) {}

    override fun logStackTrace(tag: String, e: Exception) {}

    override fun onTextChanged(changedSession: TerminalSession) {
        onTextChange()
    }

    override fun onTitleChanged(changedSession: TerminalSession) {}

    override fun onSessionFinished(finishedSession: TerminalSession) {}

    override fun onBell(session: TerminalSession) {}

    override fun onColorsChanged(session: TerminalSession) {}

    override fun onTerminalCursorStateChange(state: Boolean) {}

    override fun getTerminalCursorStyle(): Int {
        return TerminalEmulator.DEFAULT_TERMINAL_CURSOR_STYLE
    }

    override fun onCopyTextToClipboard(arg0: TerminalSession, arg1: String) {}

    override fun onPasteTextFromClipboard(session: TerminalSession?) {}
}