/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.google.android.glass.sample.timer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.testglass.R;
import com.google.android.glass.widget.CardScrollAdapter;

/**
 * Adapter for the {@link CardSrollView} inside {@link SelectValueActivity}.
 */
public class SelectValueScrollAdapter extends CardScrollAdapter {

    private final Context mContext;
    private final int mCount;

    public SelectValueScrollAdapter(Context context, int count) {
        mContext = context;
        mCount = count;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_select_value, parent);
        }

        final TextView view = (TextView) convertView.findViewById(R.id.value);
        view.setText(String.format("%02d", position));

        return convertView;
    }

    @Override
    public int getPosition(Object item) {
        if (item instanceof Integer) {
            int itemInt = (Integer) item;
            if (itemInt >= 0 && itemInt < mCount) {
                return itemInt;
            }
        }
        return AdapterView.INVALID_POSITION;
    }
}
