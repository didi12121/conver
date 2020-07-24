package swager.getdata;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Getdata {

    public static List<ApiModule> getapimodulelist(String tagname) throws IOException {
        List<ApiModule> apiModules = new ArrayList<>();
        JsonObject jsonObject = getjsonobjbygson();
        JsonObject paths1 = jsonObject.getAsJsonObject("paths");
        JsonObject definitions = jsonObject.getAsJsonObject("definitions");
        Set<String> paths = paths1.keySet();
        for (String path : paths) {
            JsonObject pathJsonObject = paths1.getAsJsonObject(path);
            Set<String> pathJsonObjectSkey = pathJsonObject.keySet();
            for (String x : pathJsonObjectSkey) {
                JsonObject strjsonObject = pathJsonObject.getAsJsonObject(x);
                String tags = strjsonObject.get("tags").toString();
                if (tags.equals("[\""+tagname+"\"]")) {
                    ApiModule apiModule = new ApiModule();
                    apiModule.setUrl(path);
                    apiModule.setType(x.toUpperCase());
                    apiModule.setName(strjsonObject.get("summary").getAsString());
                    apiModule.setDes(strjsonObject.get("description")!=null?strjsonObject.get("description").getAsString():"");
                    apiModule.setRe(getrespoenstr(definitions,strjsonObject));
                    apiModule.setParmas(getParmas(strjsonObject));
                    apiModules.add(apiModule);
                }
            }
        }
//        apiModules.forEach(System.out::println);
        return apiModules;
    }

    private static List<Parma> getParmas(JsonObject strjsonObject) {
        String memberName="parameters";
        if (strjsonObject.has(memberName)) {
            String parameters = strjsonObject.getAsJsonArray(memberName).toString();
            List<Parma> parmas = JSONObject.parseArray(parameters).toJavaList(Parma.class);
            return parmas;
        }else {

        }
        return null;
    }

    public static JsonObject getjsonobjbygson() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.1.147:8698/homebay/v2/api-docs")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        JsonElement ele = new JsonParser().parse(string);
        JsonObject asJsonObject = ele.getAsJsonObject();
        return asJsonObject;
    }
    private static String getrespoenstr(JsonObject definitions, JsonObject strjsonObject) {
        String asString = strjsonObject.getAsJsonObject("responses").getAsJsonObject("200").getAsJsonObject("schema").get("$ref").getAsString();
        //写死长度2
        String name = asString.split("/")[2];
        JsonObject asJsonObject = definitions.getAsJsonObject(name);
        JsonObject properties = asJsonObject.getAsJsonObject("properties");
        Set<String> strings = properties.keySet();
        //获得返回结果的属性（result类)
        StringBuilder stringBuilder=new StringBuilder("{\n");
        //魔法变量
        String splix="                      *";
        String tab="        ";
        for (String string : strings) {
            //判断是否存在data
            if (!string.equals("data")) {
                //老千层饼了
                String description = properties.getAsJsonObject(string).get("description").getAsString();
                String one = tab+string + splix + description;
                stringBuilder.append(one + "\n");
            }else {
                JsonObject data = properties.getAsJsonObject("data");
                //是否存在第二层
                if (data.has("$ref")){
                    stringBuilder.append(tab+"data"+splix+"响应数据\n\n");
                    String ref = data.get("$ref").getAsString().split("/")[2];
                    JsonObject dataproperties = definitions.getAsJsonObject(ref).getAsJsonObject("properties");
                    //响应类封装的类
                    for (String valuename : dataproperties.keySet()) {
                        JsonObject asJsonObject1 = dataproperties.getAsJsonObject(valuename);
                        JsonElement description1 = asJsonObject1.get("description");
                        String description = " ";
                        if (description1!=null){
                            description = description1.getAsString();
                        }
                        String two = tab+tab+valuename + splix + description;
                        stringBuilder.append(two+"\n");
                    }
                }

            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }}
