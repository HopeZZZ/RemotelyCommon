package com.zhongke.common.http.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.zhongke.common.utils.ZKRandomUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * author : wpt
 * date   : 2021/7/2816:23
 * desc   : 共用参数
 */
public class ZKCommonParamsInterceptor implements Interceptor {

    public Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                    new TypeToken<TreeMap<String, Object>>() {
                    }.getType(),
                    new JsonDeserializer<TreeMap<String, Object>>() {
                        @Override
                        public TreeMap<String, Object> deserialize(
                                JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

                            TreeMap<String, Object> treeMap = new TreeMap<>();
                            JsonObject jsonObject = json.getAsJsonObject();
                            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                            for (Map.Entry<String, JsonElement> entry : entrySet) {
                                Object ot = entry.getValue();
                                if (ot instanceof JsonPrimitive) {
                                    treeMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
                                } else {
                                    treeMap.put(entry.getKey(), ot);
                                }
                            }
                            return treeMap;
                        }
                    }).create();

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        request = addCommonParam(request);
        Request.Builder builder = request.newBuilder();
        return chain.proceed(builder.build());
    }

    /**
     * 添加共用参数  nonceStr,timestamp
     *  nonceStr:随机字符串,长度32位，包含字符0-9a-zA-Z
     *  timestamp:10位的秒级时间戳
     * @param request
     * @return
     */
    private Request addCommonParam(Request request) {
        RequestBody requestBody = request.body();
        TreeMap<Object, Object> rootMap = new TreeMap<>();
        // 把原来请求的和公共的参数进行组装
        String nonceStr = ZKRandomUtil.getStrByLength(32);
        rootMap.put("nonceStr", nonceStr);  // 干扰串	32位随机字符串，避免重复
        long timestamp = System.currentTimeMillis() / 1000;
        rootMap.put("timestamp", timestamp);  // 10位时间戳

        HttpUrl httpUrl = request.url();
        String method = request.method();
        boolean isGet = method.equals("GET");
        boolean isPost = method.equals("POST");
        boolean isPut = method.equals("PUT");
        boolean isDelete = method.equals("DELETE");
        if (isGet) {
            // 通过请求地址(最初始的请求地址)获取到参数列表
            Set<String> parameterNames = httpUrl.queryParameterNames();
            for (String key : parameterNames) {  // 循环参数列表
                rootMap.put(key, httpUrl.queryParameter(key));
            }
            String newJsonParams = paramToString(rootMap);  // 装换成json字符串

            String url = httpUrl.toString();

            int index = url.indexOf("?");
            if (index > 0) {
                url = url.substring(0, index);
            }
            url = url + "?" + newJsonParams;  // 拼接新的url
            request = request.newBuilder().url(url).build();  // 重新构建请求
        } else if (isPost || isPut || isDelete) {
            if (requestBody instanceof RequestBody) {
                // buffer流
                Buffer buffer = new Buffer();
                String newJsonParams = null;
                try {
                    requestBody.writeTo(buffer);
                    String oldParamsJson = buffer.readUtf8();
                    String[] splitAnd = oldParamsJson.split("&");
                    for (String str : splitAnd) {
                        String[] splitEqual = str.split("=");
                        for (int i = 0; i < splitEqual.length; i = i + 2) {
                            if (splitEqual.length > i + 1) {
                                rootMap.put(splitEqual[i], splitEqual[i + 1]);
                            }
                        }
                    }
                    newJsonParams = gson.toJson(rootMap);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newJsonParams);
                    request = isPost ? request.newBuilder().url(httpUrl).post(body).build()
                            : (isPut ? request.newBuilder().url(httpUrl).put(body).build()
                            : request.newBuilder().url(httpUrl).delete(body).build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request.body() instanceof FormBody) {
                for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                    rootMap.put(((FormBody) requestBody).encodedName(i), ((FormBody) requestBody).encodedValue(i));
                }
            }
//            }
        }

        return request;
    }

    public static String paramToString(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        // 删除最后一个"&"
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
