package com.mytaxi.android_demo.dependencies.component;

import com.mytaxi.android_demo.activities.AuthenticationActivityTest;
import com.mytaxi.android_demo.activities.DriverProfileActivityTest;
import com.mytaxi.android_demo.activities.MainActivityTest;
import com.mytaxi.android_demo.dependencies.modules.TestNetworkModule;
import com.mytaxi.android_demo.dependencies.modules.TestPermissionModule;
import com.mytaxi.android_demo.dependencies.modules.TestSharedPrefStorageModule;
import javax.inject.Singleton;
import dagger.Component;

@Singleton

@Component(modules = {TestNetworkModule.class, TestPermissionModule.class, TestSharedPrefStorageModule.class})

public interface TestAppComponent extends AppComponent {


    void inject(AuthenticationActivityTest activity);
    void inject(MainActivityTest mainActivityTest);
    void inject(DriverProfileActivityTest driverProfileActivityTest);

}
