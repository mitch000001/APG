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

import org.thialfihar.android.apg.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KeyServerEditor extends LinearLayout implements Editor, OnClickListener {
    private EditorListener mEditorListener = null;

    ImageButton mDeleteButton;
    TextView mServer;

    public KeyServerEditor(Context context) {
        super(context);
    }

    public KeyServerEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        setDrawingCacheEnabled(true);
        setAlwaysDrawnWithCacheEnabled(true);

        mServer = (TextView) findViewById(R.id.server);

        mDeleteButton = (ImageButton) findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(this);

        super.onFinishInflate();
    }

    public void setValue(String value) {
        mServer.setText(value);
    }

    public String getValue() {
        return mServer.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        final ViewGroup parent = (ViewGroup)getParent();
        if (v == mDeleteButton) {
            parent.removeView(this);
            if (mEditorListener != null) {
                mEditorListener.onDeleted(this);
            }
        }
    }

    @Override
    public void setEditorListener(EditorListener listener) {
        mEditorListener = listener;
    }
}
