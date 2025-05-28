package com.reena.aamp.activity;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalView;
import com.termux.view.TerminalViewClient;

public interface CustomTerminalViewClient extends TerminalViewClient {
    @Override
    float onScale(float scale);

    @Override
    void onSingleTapUp(MotionEvent e);

    void onSingleTapUp(TerminalView view, float x, float y);

    @Override
    boolean shouldBackButtonBeMappedToEscape();

    @Override
    boolean shouldEnforceCharBasedInput();

    @Override
    boolean shouldUseCtrlSpaceWorkaround();

    @Override
    boolean isTerminalViewSelected();

    @Override
    void copyModeChanged(boolean copyMode);

    @Override
    boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session);

    @Override
    boolean onKeyUp(int keyCode, KeyEvent e);

    @Override
    boolean onLongPress(MotionEvent event);

    @Override
    boolean readControlKey();

    @Override
    boolean readAltKey();

    @Override
    boolean readShiftKey();

    @Override
    boolean readFnKey();

    @Override
    boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session);

    @Override
    void onEmulatorSet();

    @Override
    void logError(String tag, String message);

    @Override
    void logWarn(String tag, String message);

    @Override
    void logInfo(String tag, String message);

    @Override
    void logDebug(String tag, String message);

    @Override
    void logVerbose(String tag, String message);

    @Override
    void logStackTraceWithMessage(String tag, String message, Exception e);

    @Override
    void logStackTrace(String tag, Exception e);

    boolean onLongPress(TerminalView view, float x, float y);
}
