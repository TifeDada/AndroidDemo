package com.mytaxi.android_demo.dependencies.modules;

import android.content.Context;
import com.mytaxi.android_demo.utils.storage.SharedPrefStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import static org.mockito.Mockito.mock;

@Module
public class TestSharedPrefStorageModule {

    private Context mContext;
    @Singleton
    @Provides
    SharedPrefStorage provideSharedPrefStorage() {
        return mock (SharedPrefStorage.class);
    }




}
