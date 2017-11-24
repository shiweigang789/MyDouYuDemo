package com.swg.mydouyudemo.net.factory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by swg on 2017/11/23.
 */

public class ExGsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    public static ExGsonConverterFactory create() {
        return create(new Gson());
    }

    public static ExGsonConverterFactory create(Gson gson) {
        return new ExGsonConverterFactory(gson);
    }

    private ExGsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ExGsonRequestBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ExGsonResponseBodyConverter<>(gson, type);
    }
}
