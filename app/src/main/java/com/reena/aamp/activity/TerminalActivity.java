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

        initializeViews();
        setupTerminal();
    }

    private void initializeViews() {
        terminalView = findViewById(R.id.terminal_view);
    }

    private void setupTerminal() {
        String[] environment = getEnvironment();
        String workingDirectory = getWorkingDirectory();
        String[] command = {"/system/bin/sh"};
        var sessionClient = new TerminalSessionClient(() -> {
            TerminalActivity.this.onScreenChanged();
            return null;
        });

        terminalSession = new TerminalSession(
                command[0],         // shell command
                workingDirectory,   // working directory
                command,           // arguments
                environment,       // environment variable
                TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS, // transcript rows
                sessionClient      // Session
        );

        terminalView.setTerminalViewClient(new CustomTerminalViewClient());
        terminalView.attachSession(terminalSession);
        terminalView.setTextSize(30);
    }

    private void onScreenChanged() {
        terminalView.onScreenUpdated();
    }

    private String[] getEnvironment() {
        return new String[]{
                "TERM=xterm-256color",
                "HOME=" + getFilesDir().getAbsolutePath(),
                "PATH=/system/bin:/system/xbin",
                "SHELL=/system/bin/sh"
        };
    }

    private String getWorkingDirectory() {
        File workDir = new File(getFilesDir(), "home");
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
        return workDir.getAbsolutePath();
    }
    private static class CustomTerminalViewClient implements com.reena.aamp.terminal.CustomTerminalViewClient {
        @Override
        public float onScale(float scale) {
            return Math.max(0.9f, Math.min(1.6f, scale));
        }

        @Override
        public void onSingleTapUp(MotionEvent e) {

        }

        @Override
        public void onSingleTapUp(TerminalView view, float x, float y) {
            view.requestFocus();
        }

        @Override
        public boolean shouldBackButtonBeMappedToEscape() {
            return false;
        }

        @Override
        public boolean shouldEnforceCharBasedInput() {
            return false;
        }

        @Override
        public boolean shouldUseCtrlSpaceWorkaround() {
            return false;
        }

        @Override
        public boolean isTerminalViewSelected() {
            return false;
        }

        @Override
        public void copyModeChanged(boolean copyMode) {}

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
            return false;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent e) {
            return false;
        }

        @Override
        public boolean onLongPress(MotionEvent event) {
            return false;
        }

        @Override
        public boolean readControlKey() {
            return false;
        }

        @Override
        public boolean readAltKey() {
            return false;
        }
        @Override
        public boolean readShiftKey() {
            return false;
        }

        @Override
        public boolean readFnKey() {
            return false;
        }

        @Override
        public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
            return false;
        }

        @Override
        public void onEmulatorSet() {

        }

        @Override
        public void logError(String tag, String message) {

        }

        @Override
        public void logWarn(String tag, String message) {

        }

        @Override
        public void logInfo(String tag, String message) {

        }

        @Override
        public void logDebug(String tag, String message) {

        }

        @Override
        public void logVerbose(String tag, String message) {

        }

        @Override
        public void logStackTraceWithMessage(String tag, String message, Exception e) {

        }

        @Override
        public void logStackTrace(String tag, Exception e) {

        }

        @Override
        public boolean onLongPress(TerminalView view, float x, float y) {
            // Handle long press
            return false;
        }
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
            // Send Ctrl+C to interrupt current command
            terminalSession.write("\u0003");
        } else {
            super.onBackPressed();
        }
    }
}