package com.yc.monitorapplib.db;

import android.provider.BaseColumns;


final class DbConst {

    static final String DATABASE_NAME = "timeline";

    private DbConst() {
    }

    static class TableIgnore implements BaseColumns {
        static final String TABLE_NAME = "ignore";
        static final String FIELD_PACKAGE_NAME = "package_name";
        static final String FIELD_CREATE_TIME = "created_time";
    }

    static class TableHistory implements BaseColumns {
        static final String TABLE_NAME = "history";
        static final String FIELD_DATE = "date";
        static final String FIELD_PACKAGE_NAME = "package_name";
        static final String FIELD_NAME = "name";
        static final String FIELD_SYSTEM = "is_system";
        static final String FIELD_DURATION = "duration";
        static final String FIELD_TIMESTAMP = "timestamp";
        static final String FIELD_MOBILE_TRAFFIC = "mobile";
    }

}
