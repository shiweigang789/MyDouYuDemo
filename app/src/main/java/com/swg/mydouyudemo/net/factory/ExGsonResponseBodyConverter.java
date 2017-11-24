package com.swg.mydouyudemo.net.factory;

import com.google.gson.Gson;
import com.swg.mydouyudemo.net.response.HttpResponse;
import com.swg.mydouyudemo.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 服务器返回数据转换
 * Created by swg on 2017/11/23.
 */

public class ExGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    ExGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 进行解析预处理
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String value = responseBody.string();
        LogUtil.json(value);
        HttpResponse httpResponse = new HttpResponse();
        try {
            JSONObject response = new JSONObject(value);
            int error = response.optInt("error");
            if (error != 0) {
                httpResponse.setErrror(error);
                httpResponse.setData(response.optString("data"));
                return (T) gson.fromJson(value, httpResponse.getClass());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gson.fromJson(value, type);
    }
}
