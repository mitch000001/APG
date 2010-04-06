/*
 * Copyright (C) 2010 Thialfihar <thi@thialfihar.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thialfihar.android.apg.ui.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.thialfihar.android.apg.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class UserIdEditor extends LinearLayout implements Editor, OnClickListener {
    private EditorListener mEditorListener = null;

    private ImageButton mDeleteButton;
    private RadioButton mIsMainUserId;
    private EditText mName;
    private EditText mEmail;
    private EditText mComment;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+[.]([a-zA-Z])+([a-zA-Z])+",
                            Pattern.CASE_INSENSITIVE);

    public static class NoNameException extends Exception {
        static final long serialVersionUID = 0xf812773343L;

        public NoNameException(String message) {
            super(message);
        }
    }

    public static class NoEmailException extends Exception {
        static final long serialVersionUID = 0xf812773344L;

        public NoEmailException(String message) {
            super(message);
        }
    }

    public static class InvalidEmailException extends Exception {
        static final long serialVersionUID = 0xf812773345L;

        public InvalidEmailException(String message) {
            super(message);
        }
    }

    public UserIdEditor(Context context) {
        super(context);
    }

    public UserIdEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        setDrawingCacheEnabled(true);
        setAlwaysDrawnWithCacheEnabled(true);

        mDeleteButton = (ImageButton) findViewById(R.id.edit_delete);
        mDeleteButton.setOnClickListener(this);
        mIsMainUserId = (RadioButton) findViewById(R.id.is_main_user_id);
        mIsMainUserId.setOnClickListener(this);

        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mComment = (EditText) findViewById(R.id.comment);

        super.onFinishInflate();
    }

    public void setValue(String userId) {
        mName.setText("");
        mComment.setText("");
        mEmail.setText("");

        Pattern withComment = Pattern.compile("^(.*) [(](.*)[)] <(.*)>$");
        Matcher matcher = withComment.matcher(userId);
        if (matcher.matches()) {
            mName.setText(matcher.group(1));
            mComment.setText(matcher.group(2));
            mEmail.setText(matcher.group(3));
            return;
        }

        Pattern withoutComment = Pattern.compile("^(.*) <(.*)>$");
        matcher = withoutComment.matcher(userId);
        if (matcher.matches()) {
            mName.setText(matcher.group(1));
            mEmail.setText(matcher.group(2));
            return;
        }
    }

    public String getValue() throws NoNameException, NoEmailException, InvalidEmailException {
        String name = ("" + mName.getText()).trim();
        String email = ("" + mEmail.getText()).trim();
        String comment = ("" + mComment.getText()).trim();

        if (email.length() > 0) {
            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
            if (!emailMatcher.matches()) {
                throw new InvalidEmailException("invalid email '" + email + "'");
            }
        }

        String userId = name;
        if (comment.length() > 0) {
            userId += " (" + comment + ")";
        }
        if (email.length() > 0) {
            userId += " <" + email + ">";
        }

        if (userId.equals("")) {
            // ok, empty one...
            return userId;
        }

        // otherwise make sure that name and email exist
        if (name.equals("")) {
            throw new NoNameException("need a name");
        }

        if (email.equals("")) {
            throw new NoEmailException("need an email");
        }

        return userId;
    }

    @Override
    public void onClick(View v) {
        final ViewGroup parent = (ViewGroup)getParent();
        if (v == mDeleteButton) {
            boolean wasMainUserId = mIsMainUserId.isChecked();
            parent.removeView(this);
            if (mEditorListener != null) {
                mEditorListener.onDeleted(this);
            }
            if (wasMainUserId && parent.getChildCount() > 0) {
                UserIdEditor editor = (UserIdEditor) parent.getChildAt(0);
                editor.setIsMainUserId(true);
            }
        } else if (v == mIsMainUserId) {
            for (int i = 0; i < parent.getChildCount(); ++i) {
                UserIdEditor editor = (UserIdEditor) parent.getChildAt(i);
                if (editor == this) {
                    editor.setIsMainUserId(true);
                } else {
                    editor.setIsMainUserId(false);
                }
            }
        }
    }

    public void setIsMainUserId(boolean value) {
        mIsMainUserId.setChecked(value);
    }

    public boolean isMainUserId() {
        return mIsMainUserId.isChecked();
    }

    @Override
    public void setEditorListener(EditorListener listener) {
        mEditorListener = listener;
    }
}
