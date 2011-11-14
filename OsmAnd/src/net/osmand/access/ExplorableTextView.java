package net.osmand.access;

import java.lang.Math;

import android.content.Context;
import android.text.Layout;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.MotionEvent;
import android.widget.TextView;

// This class is intended to be used in place of the TextView
// to provide accessible exploration capability by arrow keys.
//
public class ExplorableTextView extends TextView {

    private int cursor;
    private int selectionStart;
    private int selectionEnd;
    private boolean cursorTrackingEnabled = true;


    // Conventional constructors.

    public ExplorableTextView(Context context) {
        super(context);
    }

    public ExplorableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExplorableTextView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }


    // Overrided callbacks to provide accessible exploration means.

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        cursorTrackingEnabled = false;
        boolean result = super.dispatchPopulateAccessibilityEvent(event);
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            if (isFocused()) {
                final int length = Math.min(selectionEnd - selectionStart, AccessibilityEvent.MAX_TEXT_LENGTH);
                event.getText().clear();
                event.getText().add(getText().subSequence(selectionStart, selectionStart + length));
                event.setAddedCount(length);
            } else {
                event.setAddedCount(Math.min(getText().length(), AccessibilityEvent.MAX_TEXT_LENGTH));
            }
            event.setRemovedCount(0);
            event.setFromIndex(0);
            event.setBeforeText(null);
            result = true;
        }
        cursorTrackingEnabled =true;
        return result;
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!isFocused())
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    @Override
    protected void onSelectionChanged(int start, int end) {
        super.onSelectionChanged(start, end);
        if (cursorTrackingEnabled && isFocused()) {
            if (end >= getText().length()) {
                cursor = getText().length();
            } else if (cursor != end) {
                if (Math.abs(cursor - end) > 1) {
                    final Layout layout = getLayout();
                    final int line = layout.getLineForOffset(end);
                    selectionStart = layout.getLineStart(line);
                    selectionEnd = layout.getLineEnd(line);
                } else {
                    selectionStart = end;
                    selectionEnd = end + 1;
                }
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
                cursor = end;
            }
        }
    }

}
