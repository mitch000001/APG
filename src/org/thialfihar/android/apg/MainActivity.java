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

package org.thialfihar.android.apg;

import org.thialfihar.android.apg.provider.Accounts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends BaseActivity {
    private ListView mAccounts = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button encryptMessageButton = (Button) findViewById(R.id.btn_encryptMessage);
        Button decryptMessageButton = (Button) findViewById(R.id.btn_decryptMessage);
        Button encryptFileButton = (Button) findViewById(R.id.btn_encryptFile);
        Button decryptFileButton = (Button) findViewById(R.id.btn_decryptFile);
        mAccounts = (ListView) findViewById(R.id.account_list);

        encryptMessageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EncryptMessageActivity.class));
            }
        });

        decryptMessageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DecryptMessageActivity.class));
            }
        });

        encryptFileButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EncryptFileActivity.class));
            }
        });

        decryptFileButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DecryptFileActivity.class));
            }
        });

        Cursor accountCursor = managedQuery(Accounts.CONTENT_URI, null, null, null, null);

        mAccounts.setAdapter(new AccountListAdapter(this, accountCursor));
        mAccounts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
                Cursor cursor =
                        managedQuery(Uri.withAppendedPath(Accounts.CONTENT_URI, "" + id), null,
                                     null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int nameIndex = cursor.getColumnIndex(Accounts.NAME);
                    String accountName = cursor.getString(nameIndex);
                    startActivity(new Intent(MainActivity.this, MailListActivity.class)
                                        .putExtra("account", accountName));
                }
            }
        });
        registerForContextMenu(mAccounts);

        if (!hasSeenChangeLog()) {
            showDialog(Id.dialog.change_log);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Id.dialog.new_account: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Add Account");
                alert.setMessage("Specify the Google Mail account you want to add.");

                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                MainActivity.this.removeDialog(Id.dialog.new_account);
                                                String accountName = "" + input.getText();

                                                Cursor testCursor =
                                                        managedQuery(Uri.parse("content://gmail-ls/conversations/" +
                                                                               accountName),
                                                                     null, null, null, null);
                                                if (testCursor == null) {
                                                    Toast.makeText(MainActivity.this,
                                                                   "Error: account '" + accountName +
                                                                     "' not found",
                                                                   Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                ContentValues values = new ContentValues();
                                                values.put(Accounts.NAME, accountName);
                                                try {
                                                    MainActivity.this.getContentResolver()
                                                                     .insert(Accounts.CONTENT_URI,
                                                                             values);
                                                } catch (SQLException e) {
                                                    Toast.makeText(MainActivity.this,
                                                                   "Error: failed to add account '" +
                                                                              accountName + "'",
                                                                   Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                alert.setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                MainActivity.this.removeDialog(Id.dialog.new_account);
                                            }
                                        });

                return alert.create();
            }

            case Id.dialog.about: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("About " + Apg.FULL_VERSION);
                ScrollView scrollView = new ScrollView(this);
                TextView message = new TextView(this);

                SpannableString info =
                        new SpannableString("This is an attempt to bring OpenPGP to Android. " +
                                            "It is far from complete, but more features are " +
                                            "planned (see website).\n" +
                                            "\n" +
                                            "Feel free to send bug reports, suggestions, feature " +
                                            "requests, feedback, photographs.\n" +
                                            "\n" +
                                            "mail: thi@thialfihar.org\n" +
                                            "site: http://apg.thialfihar.org\n" +
                                            "\n" +
                                            "This software is provided \"as is\", without " +
                                            "warranty of any kind.");
                Linkify.addLinks(info, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
                message.setMovementMethod(LinkMovementMethod.getInstance());
                message.setText(info);
                // 5dip padding
                int padding = (int) (10 * getResources().getDisplayMetrics().densityDpi / 160);
                message.setPadding(padding, padding, padding, padding);
                message.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                scrollView.addView(message);
                alert.setView(scrollView);

                alert.setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                MainActivity.this.removeDialog(Id.dialog.about);
                                            }
                });

                return alert.create();
            }

            case Id.dialog.change_log: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Changes " + Apg.FULL_VERSION);
                ScrollView scrollView = new ScrollView(this);
                TextView message = new TextView(this);

                SpannableString info =
                        new SpannableString("Read the warnings!\n\n" +
                                            "Changes:\n" +
                                            "\n" +
                                            "WARNING: be careful editing your existing keys, as they " +
                                            "WILL be stripped of certificates right now.\n" +
                                            "WARNING: key creation/editing doesn't support all " +
                                            "GPG features yet. In particular: " +
                                            "key cross-certification is NOT supported, so signing " +
                                            "with those keys will get a warning when the signature is " +
                                            "checked.\n" +
                                            "\n" +
                                            "I hope APG continues to be useful to you, please send " +
                                            "bug reports, feature wishes, feedback.");
                message.setText(info);
                // 5dip padding
                int padding = (int) (10 * getResources().getDisplayMetrics().densityDpi / 160);
                message.setPadding(padding, padding, padding, padding);
                message.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                scrollView.addView(message);
                alert.setView(scrollView);

                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                MainActivity.this.removeDialog(Id.dialog.change_log);
                                                setHasSeenChangeLog(true);
                                            }
                });

                return alert.create();
            }

            default: {
                break;
            }
        }

        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Id.menu.option.create, 0, R.string.menu_addAccount)
                .setIcon(android.R.drawable.ic_menu_add);
        menu.add(1, Id.menu.option.manage_public_keys, 1, R.string.menu_managePublicKeys)
                .setIcon(android.R.drawable.ic_menu_manage);
        menu.add(1, Id.menu.option.manage_secret_keys, 2, R.string.menu_manageSecretKeys)
                .setIcon(android.R.drawable.ic_menu_manage);
        menu.add(2, Id.menu.option.preferences, 3, R.string.menu_preferences)
                .setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(2, Id.menu.option.about, 4, R.string.menu_about)
                .setIcon(android.R.drawable.ic_menu_info_details);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Id.menu.option.create: {
                showDialog(Id.dialog.new_account);
                return true;
            }

            case Id.menu.option.about: {
                showDialog(Id.dialog.about);
                return true;
            }

            case Id.menu.option.manage_public_keys: {
                startActivity(new Intent(this, PublicKeyListActivity.class));
                return true;
            }

            case Id.menu.option.manage_secret_keys: {
                startActivity(new Intent(this, SecretKeyListActivity.class));
                return true;
            }

            case Id.menu.option.preferences: {
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            }

            default: {
                break;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        TextView nameTextView = (TextView) v.findViewById(R.id.account_name);
        if (nameTextView != null) {
            menu.setHeaderTitle(nameTextView.getText());
            menu.add(0, Id.menu.delete, 0, "Delete Account");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();

        switch (menuItem.getItemId()) {
            case Id.menu.delete: {
                Uri uri = Uri.withAppendedPath(Accounts.CONTENT_URI, "" + info.id);
                this.getContentResolver().delete(uri, null, null);
                return true;
            }

            default: {
                return super.onContextItemSelected(menuItem);
            }
        }
    }


    private static class AccountListAdapter extends CursorAdapter {
        private LayoutInflater minflater;

        public AccountListAdapter(Context context, Cursor cursor) {
            super(context, cursor);
            minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return minflater.inflate(R.layout.account_item, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView nameTextView = (TextView) view.findViewById(R.id.account_name);
            int nameIndex = cursor.getColumnIndex(Accounts.NAME);
            final String account = cursor.getString(nameIndex);
            nameTextView.setText(account);
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }
}