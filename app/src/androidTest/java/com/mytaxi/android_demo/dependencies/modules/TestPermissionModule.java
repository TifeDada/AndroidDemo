package com.mytaxi.android_demo.dependencies.modules;

import com.mytaxi.android_demo.utils.PermissionHelper;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import static org.mockito.Mockito.mock;

@Module

public class TestPermissionModule {


        @Singleton
        @Provides
        PermissionHelper providePermissionHelper() {
            return mock (PermissionHelper.class);
        }

    }


