package com.sharedhere;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.sharedhere.SharedHereAdapterTest \
 * com.sharedhere.tests/android.test.InstrumentationTestRunner
 */
public class SharedHereAdapterTest extends ActivityInstrumentationTestCase2<SharedHereAdapter> {

    public SharedHereAdapterTest() {
        super("com.sharedhere", SharedHereAdapter.class);
    }

}
