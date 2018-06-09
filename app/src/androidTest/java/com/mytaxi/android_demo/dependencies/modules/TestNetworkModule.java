package com.mytaxi.android_demo.dependencies.modules;

import com.mytaxi.android_demo.utils.network.HttpClient;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import static org.mockito.Mockito.mock;

@Module

public class TestNetworkModule {

    @Singleton
    @Provides
    HttpClient provideHttpClient() {
        return mock(HttpClient.class);


    }


}
