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

import org.bouncycastle2.bcpg.CompressionAlgorithmTags;

public final class Id {
    public static final class menu {
        public static final int export = 0x21070001;
        public static final int delete = 0x21070002;
        public static final int edit = 0x21070003;
        public static final int update = 0x21070004;

        public static final class option {
            public static final int new_pass_phrase = 0x21070001;
            public static final int create = 0x21070002;
            public static final int about = 0x21070003;
            public static final int manage_public_keys = 0x21070004;
            public static final int manage_secret_keys = 0x21070005;
            public static final int import_keys = 0x21070006;
            public static final int export_keys = 0x21070007;
            public static final int preferences = 0x21070008;
            public static final int search = 0x21070009;
            public static final int help = 0x21070010;
            public static final int key_server = 0x21070011;
        }
    }

    public static final class message {
        public static final int progress_update = 0x21070001;
        public static final int done = 0x21070002;
        public static final int import_keys = 0x21070003;
        public static final int export_keys = 0x21070004;
        public static final int import_done = 0x21070005;
        public static final int export_done = 0x21070006;
        public static final int create_key = 0x21070007;
        public static final int edit_key = 0x21070008;
        public static final int delete_done = 0x21070009;
        public static final int query_done = 0x21070010;
        public static final int unknown_signature_key = 0x21070011;
    }

    public static final class request {
        public static final int public_keys = 0x21070001;
        public static final int secret_keys = 0x21070002;
        public static final int filename = 0x21070003;
        public static final int output_filename = 0x21070004;
        public static final int key_server_preference = 0x21070005;
        public static final int look_up_key_id = 0x21070006;
    }

    public static final class dialog {
        public static final int pass_phrase = 0x21070001;
        public static final int encrypting = 0x21070002;
        public static final int decrypting = 0x21070003;
        public static final int new_pass_phrase = 0x21070004;
        public static final int pass_phrases_do_not_match = 0x21070005;
        public static final int no_pass_phrase = 0x21070006;
        public static final int saving = 0x21070007;
        public static final int delete_key = 0x21070008;
        public static final int import_keys = 0x21070009;
        public static final int importing = 0x2107000a;
        public static final int export_key = 0x2107000b;
        public static final int export_keys = 0x2107000c;
        public static final int exporting = 0x2107000d;
        public static final int new_account = 0x2107000e;
        public static final int about = 0x2107000f;
        public static final int change_log = 0x21070010;
        public static final int output_filename = 0x21070011;
        public static final int delete_file = 0x21070012;
        public static final int deleting = 0x21070013;
        public static final int help = 0x21070014;
        public static final int querying = 0x21070015;
        public static final int lookup_unknown_key = 0x21070016;
    }

    public static final class task {
        public static final int import_keys = 0x21070001;
        public static final int export_keys = 0x21070002;
    }

    public static final class database {
        public static final int type_public = 0;
        public static final int type_secret = 1;
    }

    public static final class type {
        public static final int public_key = 0x21070001;
        public static final int secret_key = 0x21070002;
        public static final int user_id = 0x21070003;
        public static final int key = 0x21070004;
    }

    public static final class choice {
        public static final class algorithm {
            public static final int dsa = 0x21070001;
            public static final int elgamal = 0x21070002;
            public static final int rsa = 0x21070003;
        }

        public static final class compression {
            public static final int none = 0x21070001;
            public static final int zlib = CompressionAlgorithmTags.ZLIB;
            public static final int bzip2 = CompressionAlgorithmTags.BZIP2;
            public static final int zip = CompressionAlgorithmTags.ZIP;
        }

        public static final class usage {
            public static final int sign_only = 0x21070001;
            public static final int encrypt_only = 0x21070002;
            public static final int sign_and_encrypt = 0x21070003;
        }

        public static final class action {
            public static final int encrypt = 0x21070001;
            public static final int decrypt = 0x21070002;
            public static final int import_public = 0x21070003;
            public static final int import_secret = 0x21070004;
        }
    }

    public static final class return_value {
        public static final int ok = 0;
        public static final int error = -1;
        public static final int no_master_key = -2;
        public static final int updated = 1;
        public static final int bad = -3;
    }

    public static final class target {
        public static final int clipboard = 0x21070001;
        public static final int email = 0x21070002;
        public static final int file = 0x21070003;
        public static final int message = 0x21070004;
    }

    public static final class mode {
        public static final int undefined = 0x21070001;
        public static final int byte_array = 0x21070002;
        public static final int file = 0x21070003;
        public static final int stream = 0x21070004;
    }

    public static final class key {
        public static final int none = 0;
        public static final int symmetric = -1;
    }

    public static final class content {
        public static final int unknown = 0;
        public static final int encrypted_data = 1;
        public static final int keys = 2;
    }

    public static final class query {
        public static final int search = 0x21070001;
        public static final int get = 0x21070002;
    }
}
