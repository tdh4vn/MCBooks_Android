package vn.mcbooks.mcbooks.network_api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hungtran on 6/1/16.
 */
public class ServiceFactory {
    private Retrofit retrofit;
    private static ServiceFactory ourInstance = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return ourInstance;
    }

    private ServiceFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(APIURL.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass){
        return retrofit.create(serviceClass);
    }
}
