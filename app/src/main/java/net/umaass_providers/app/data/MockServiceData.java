package net.umaass_providers.app.data;



import net.umaass_providers.app.data.remote.service.ServiceApi;

import retrofit2.mock.BehaviorDelegate;

public class MockServiceData {

    public static final class MockService   {

        private final BehaviorDelegate<ServiceApi> delegate;

        public MockService(BehaviorDelegate<ServiceApi> delegate) {
            this.delegate = delegate;

        }



    }
}