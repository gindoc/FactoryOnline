package com.online.factory.factoryonline.dagger.modules;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.online.factory.factoryonline.BuildConfig;
import com.online.factory.factoryonline.R;
import com.online.factory.factoryonline.data.local.LocalApi;
import com.online.factory.factoryonline.data.remote.FactoryApi;
import com.online.factory.factoryonline.modules.download.DownloadProgressResponseBody;
import com.online.factory.factoryonline.modules.login.LogOutState;
import com.online.factory.factoryonline.modules.login.LoginContext;
import com.online.factory.factoryonline.utils.ComponentHolder;
import com.online.factory.factoryonline.utils.DBManager;
import com.online.factory.factoryonline.utils.Saver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;


/**
 * Created by louiszgm-pc on 2016/9/22.
 */
@Module
public class DataManagerModule {

    @Provides
    @Named("api_url")
    HttpUrl providesApiUrl(Resources resources) {
        return HttpUrl.parse(resources.getString(R.string.api));
    }

    @Provides
    Converter.Factory providesLoganSquareConverter() {
        return LoganSquareConverterFactory.create();
    }

    @Provides
    @Named("httpLogger")
    public HttpLoggingInterceptor providesHttpLogger() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level basic = BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.HEADERS :
                HttpLoggingInterceptor.Level.NONE;
        interceptor.setLevel(basic);
        return interceptor;
    }


    private String createResponseBody(Interceptor.Chain chain) {
        HttpUrl uri = chain.request().url();
        String path = uri.url().getPath();
//        String query = uri.url().getQuery();
        StringBuffer response = new StringBuffer();
        BufferedReader reader;
        AssetManager assetManager = ComponentHolder.getAppComponent().getContext().getAssets();
        try {
            String fileName;
            if (path.matches("^(/scrollMsgs)$")) {      //匹配/scrollMsgs
                fileName = "ScrollMsgs.json";
            } else if (path.matches("^(/recommendPriceCats)")) {
                fileName = "RecommendPriceCats.json";
            } else if (path.matches("^(/recommendAreaCats)")) {
                fileName = "RecomendAreaCats.json";
            }else if (path.matches("^(/factorypoi/[1-9]\\d*)")) {
                fileName = "FactoryPois.json";
            } else if (path.matches("^(/cities)")){
                fileName = "Cities.json";
            } else if (path.matches("^(/areas)")){
                fileName = "Areas.json";
            } else{
                fileName = "SlideUrl.json";
            }
            reader = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    @Provides
    @Named("localdata")
    public Interceptor provideLocalDataInterceptor(@Named("downloadSubject") final BehaviorSubject subject, final LoginContext loginContext) {
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = null;
                String responseString = createResponseBody(chain);
                Request request = chain.request();
                Request realRequest = null;
                Timber.d("requestBody : %s", bodyToString(request.body()));
                Response intercepterResponse = null;
                if (request.url().toString().contains("users")
                        || request.url().toString().contains("user")
                        || request.url().toString().contains("qiniutokens")
                        || request.url().toString().contains("images")
                        || request.url().toString().contains("wantedmessages")
                        || request.url().toString().contains("factorypois/district/")
                        || request.url().toString().contains("smses")
                        || request.url().toString().contains("promediums")
                        || request.url().toString().contains("search")
                        || request.url().toString().contains("neededmessages")
                        || request.url().toString().contains("promediummessages")
                        || request.url().toString().contains("branches")
                        || request.url().toString().contains("apps")
                        || request.url().toString().contains("feedbacks")) {
                    realRequest = request.newBuilder().build();
                    originalResponse = chain.proceed(realRequest);
                    if (originalResponse.message().contains("Unauthorized")/*||originalResponse.body().string().contains("305")*/){
                        Saver.logout();
                        loginContext.setmState(new LogOutState());
                    }
                } else if (request.url().toString().contains("http://olpkux7qo.bkt.clouddn.com/")){
                    originalResponse = chain.proceed(chain.request());
                    originalResponse.newBuilder()
                            .body(new DownloadProgressResponseBody(originalResponse.body(), subject))
                            .build();

                }else {
                    intercepterResponse = new Response.Builder()
                            .code(200)
                            .message(responseString)
                            .request(request)
                            .protocol(Protocol.HTTP_1_0)
                            .body(ResponseBody.create(MediaType.parse("application/json"), responseString
                                    .getBytes()))
                            .addHeader("content-type", "application/json")
                            .build();
                }
                if (intercepterResponse == null) {
                    return originalResponse;
                }else {
                    return intercepterResponse;
                }
            }
        };
        return BuildConfig.DEBUG ? interceptor : null;
    }

    @Provides
    @Singleton
    @Named("downloadSubject")
    public BehaviorSubject getDownloadSubject() {
        return BehaviorSubject.create();
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Provides
    public OkHttpClient provideHttpClient(@Named("httpLogger") HttpLoggingInterceptor loggingInterceptor,
                                          @Named("localdata") Interceptor localDataInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        if (localDataInterceptor != null) {
            builder.addInterceptor(localDataInterceptor);
        }
        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
    }

    @Provides
    public Retrofit providesRetrofit(Converter.Factory converterFactory, @Named("api_url") HttpUrl url,
                                     OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(converterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    public FactoryApi providesApi(Retrofit retrofit) {
        return retrofit.create(FactoryApi.class);
    }

    @Provides
    public DBManager provideDBManager(Context context) {
        return new DBManager(context);
    }
    @Provides
    public LocalApi provideLocalApi(DBManager dbManager) {
        return new LocalApi(dbManager);
    }
}
