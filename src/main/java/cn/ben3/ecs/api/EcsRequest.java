package cn.ben3.ecs.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;

import org.apache.commons.codec.binary.Base64;

/*
 * 调用阿里云ECS API,代码大部分来自demo。
 */
public class EcsRequest {

    private static final String API_VERSION = "2013-01-10";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ENCODING = "UTF-8";
    private static final String RESPONSE_FORMAT = "JSON";

    // Endpoint请以"/"结尾
    private URI endpoint = URI.create("https://ecs.aliyuncs.com/");
    private String httpMethod = "GET";

    private String accessKeyId;
    private String accessKeySecret;

    myX509TrustManager xtm = new myX509TrustManager();
    myHostnameVerifier hnv = new myHostnameVerifier();

    public EcsRequest(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
            sslContext.init( null,
                    xtmArray,
                    new java.security.SecureRandom() );
        } catch( GeneralSecurityException gse ) {
        }
        if( sslContext != null ) {
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier( hnv );
    }

    public String execute(String action, Map<String, String> parameters)
            throws Exception {
        assert(action != null && action.length() > 0);

        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }

        // 加入公共请求参数
        addCommonParams(action, parameters);

        // 发送请求
        return sendRequest(parameters);
    }

    private void addCommonParams(String action, Map<String, String> parameters)
            throws Exception {
        parameters.put("Action", action);
        parameters.put("Version", API_VERSION);
        parameters.put("AccessKeyId", accessKeyId);
        parameters.put("TimeStamp", formatIso8601Date(new Date()));
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1");
        parameters.put("SignatureNonce", UUID.randomUUID().toString()); // 可以使用UUID作为SignatureNonce
        parameters.put("Format", RESPONSE_FORMAT);

        // 计算签名，并将签名结果加入请求参数中
        parameters.put("Signature", computeSignature(parameters));
    }

    private String computeSignature(Map<String, String> parameters) throws Exception {
        // 将参数Key按字典顺序排序
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        final String SEPARATOR = "&";

        // 生成规范化请求字符串
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for(String key : sortedKeys) {
            canonicalizedQueryString.append("&")
            .append(percentEncode(key)).append("=")
            .append(percentEncode(parameters.get(key)));
        }

        // 生成用于计算签名的字符串 stringToSign
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);
        stringToSign.append(percentEncode(
                canonicalizedQueryString.toString().substring(1)));

        // 注意accessKeySecret后面要加入一个字符"&"
        String signature = calculateSignature(accessKeySecret + "&",
                stringToSign.toString());
        return signature;
    }

    private String sendRequest(Map<String, String> parameters) throws Exception {
        // 生成请求URL
        String query = paramsToQueryString(parameters);
        URL url = new URL(endpoint.toString() + "?" + query);

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.connect();
        InputStream content = null;
        try {
            content = connection.getInputStream();
            return readContent(content);
        } catch (IOException e) {
            content = connection.getErrorStream();
            return (readContent(content));
        } finally {
            safeClose(content);
            connection.disconnect();
        }
    }

    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        // 注意使用GMT时间
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    private static String calculateSignature(String key, String stringToSign)
            throws Exception {
        // 使用HmacSHA1算法计算HMAC值
        final String ALGORITHM = "HmacSHA1";
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(
                 key.getBytes(ENCODING), ALGORITHM));
        byte[] signData = mac.doFinal(
                  stringToSign.getBytes(ENCODING));

        return new String(Base64.encodeBase64(signData));
    }

    private static String percentEncode(String value)
            throws UnsupportedEncodingException{
        // 使用URLEncoder.encode编码后，将"+","*","%7E"做替换即满足ECS API规定的编码规范
        return value != null ?
                URLEncoder.encode(value, ENCODING).replace("+", "%20")
                .replace("*", "%2A").replace("%7E", "~")
                : null;
    }

    private static String paramsToQueryString(Map<String, String> params)
            throws UnsupportedEncodingException{
        if (params == null || params.size() == 0){
            return null;
        }

        StringBuilder paramString = new StringBuilder();
        boolean first = true;
        for(Entry<String, String> p : params.entrySet()){
            String key = p.getKey();
            String val = p.getValue();

            if (!first){
                paramString.append("&");
            }

            paramString.append(URLEncoder.encode(key, ENCODING));

            if (val != null){
                paramString.append("=").append(
                        URLEncoder.encode(val, ENCODING));
            }

            first = false;
        }

        return paramString.toString();
    }

    private static String readContent(InputStream content)
            throws IOException {
        if (content == null)
            return "";

        Reader reader = null;
        Writer writer = new StringWriter();
        String result;

        char[] buffer = new char[1024];
        try{
            reader = new BufferedReader(
                    new InputStreamReader(content, ENCODING));

            int n;
            while((n = reader.read(buffer)) > 0){
                writer.write(buffer, 0, n);
            }

            result = writer.toString();
        } finally {
            content.close();
            if (reader != null){
                reader.close();
            }
            if (writer != null){
                writer.close();
            }
        }

        return result;
    }

    private static void safeClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) { }
        }
    }
}
class myX509TrustManager implements X509TrustManager {
    public myX509TrustManager(){}
    public void checkClientTrusted(X509Certificate[] chain,   String authType) {}
    public void checkServerTrusted(X509Certificate[] chain,String authType) {

    }
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}


class myHostnameVerifier implements HostnameVerifier {
    public myHostnameVerifier(){}
    public boolean verify(String hostname,SSLSession session) {
        return true;
    }
}
