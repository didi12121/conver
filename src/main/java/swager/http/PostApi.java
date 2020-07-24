package swager.http;

import okhttp3.*;
import swager.getdata.ApiModule;
import swager.getdata.Getdata;
import swager.getdata.Parma;

import java.io.IOException;
import java.util.List;

public class PostApi {
    static String cookie="PHPSESSID=9n29gms6jtfak9t0mgieearrq2";
    static int tag = 82;
    int pid = 8;
    public static void main(String[] args) throws IOException {
        List<ApiModule> getapimodulelist = Getdata.getapimodulelist("户型相关接口");

        for (int i = 0; i < getapimodulelist.size(); i++) {
            postaApi(getapimodulelist.get(i),i);
        }
    }
    public static void postaApi(ApiModule module,int z) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        FormBody.Builder builder = new FormBody.Builder();
        MultipartBody.Builder multiBuilder=new MultipartBody.Builder();
        multiBuilder.setType(MultipartBody.FORM);
        multiBuilder.addFormDataPart("num", z+1+"");
        multiBuilder.addFormDataPart("name", module.getName());
        multiBuilder.addFormDataPart("url", module.getUrl());
        multiBuilder.addFormDataPart("type", module.getType());
        multiBuilder.addFormDataPart("des", module.getDes());
        multiBuilder.addFormDataPart("re", module.getRe());
        List<Parma> list = module.getParmas();
        for (int i = 0; i < list.size(); i++) {
            Parma parma = list.get(i);
            multiBuilder.addFormDataPart("p[name]["+i+"]", parma.getName());
            multiBuilder.addFormDataPart("p[paramType]["+i+"]", parma.getType()!=null?parma.getType():"");
            multiBuilder.addFormDataPart("p[type]["+i+"]",parma.getIsneed());
            multiBuilder.addFormDataPart("p[default]["+i+"]",parma.getDefaultstr());
            multiBuilder.addFormDataPart("p[des]["+i+"]",parma.getDescription());
        }
        RequestBody body=multiBuilder.build();
        Request request = new Request.Builder()
                .url("http://192.168.1.232:8081/index.php?act=api&tag="+tag+"&type=do&op=add&pid=")
                .method("POST", body)
                .addHeader("Cookie", cookie)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("添加接口:" +module.getName()+"  成功,接口地址:"+module.getUrl()+"==================");

    }
}
