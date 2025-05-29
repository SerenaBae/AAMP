package com.reena.aamp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.reena.aamp.terminal.TerminalSessionClient;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;
import java.io.File;
import com.reena.aamp.R;

public class TerminalActivity extends Activity {
    private TerminalView terminalView;
    private TerminalSession terminalSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        terminalView = findViewById(R.id.terminal_view);
        setupTerminal();
    }

    private void setupTerminal() {
        File workDir = new File(getFilesDir(), "home");
        if (!workDir.exists()) workDir.mkdirs();

        String[] env = {
                "TERM=xterm-256color",
                "HOME=" + getFilesDir().getAbsolutePath(),
                "PATH=/system/bin:/system/xbin",
                "SHELL=/system/bin/sh"
        };

        terminalSession = new TerminalSession(
                "/system/bin/sh",
                workDir.getAbsolutePath(),
                new String[]{"/system/bin/sh"},
                env,
                TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS,
                new TerminalSessionClient(() -> {
                    terminalView.onScreenUpdated();
                    return null;
                })
        );

        terminalView.setTerminalViewClient(new TerminalViewClient());
        terminalView.attachSession(terminalSession);
        terminalView.setTextSize(30);
    }

    private static class TerminalViewClient implements com.reena.aamp.terminal.CustomTerminalViewClient {
        @Override
        public float onScale(float scale) {
            return Math.max(0.9f, Math.min(1.6f, scale));
        }

        @Override
        public void onSingleTapUp(TerminalView view, float x, float y) {
            view.requestFocus();
        }

        @Override public void onSingleTapUp(MotionEvent e) {}
        @Override public boolean shouldBackButtonBeMappedToEscape() { return false; }
        @Override public boolean shouldEnforceCharBasedInput() { return false; }
        @Override public boolean shouldUseCtrlSpaceWorkaround() { return false; }
        @Override public boolean isTerminalViewSelected() { return false; }
        @Override public void copyModeChanged(boolean copyMode) {}
        @Override public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) { return false; }
        @Override public boolean onKeyUp(int keyCode, KeyEvent e) { return false; }
        @Override public boolean onLongPress(MotionEvent event) { return false; }
        @Override public boolean onLongPress(TerminalView view, float x, float y) { return false; }
        @Override public boolean readControlKey() { return false; }
        @Override public boolean readAltKey() { return false; }
        @Override public boolean readShiftKey() { return false; }
        @Override public boolean readFnKey() { return false; }
        @Override public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) { return false; }
        @Override public void onEmulatorSet() {}
        @Override public void logError(String tag, String message) {}
        @Override public void logWarn(String tag, String message) {}
        @Override public void logInfo(String tag, String message) {}
        @Override public void logDebug(String tag, String message) {}
        @Override public void logVerbose(String tag, String message) {}
        @Override public void logStackTraceWithMessage(String tag, String message, Exception e) {}
        @Override public void logStackTrace(String tag, Exception e) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (terminalSession != null) {
            terminalSession.finishIfRunning();
        }
    }

    @Override
    public void onBackPressed() {
        if (terminalSession != null && terminalSession.isRunning()) {
            terminalSession.write("\u0003"); // Ctrl+C
        } else {
            super.onBackPressed();
        }
    }
}