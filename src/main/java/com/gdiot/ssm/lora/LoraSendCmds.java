package com.gdiot.ssm.lora;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gdiot.ssm.util.LoraConfig;

import net.sf.json.JSONObject;


/**
 * @author ZhouHR
 */
public class LoraSendCmds {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public LoraSendCmds() {

    }

    public String sendMsg(String msg, String deveui, String request_id) {
        String result = "error";
        if (msg != null && msg != "" && msg.length() > 0) {
            result = sendMsgToWM(msg, deveui, request_id);
        }
        return result;
    }

    public String sendMsgToWM(String content, String deveui, String request_id) {
        String strResult = "error!";
//		if(msg != null){
//			msg = msg.replaceAll("\r\n", "");
//		}
        if (content != "") {
            String apiUrl = "";
            if (deveui != null && deveui != "" && deveui.length() > 0) {
                apiUrl = LoraConfig.baseUrl_http_node;
            } else {
                apiUrl = LoraConfig.baseUrl_http_cast;
            }

            String currentTime = String.valueOf(System.currentTimeMillis());
            String token = null;

            HttpPost post = new HttpPost(apiUrl);
            post.setHeader("accept", "application/json");
            post.setHeader("content-type", "application/json");
            post.setHeader("userId", LoraConfig.userID);
            post.setHeader("time", currentTime);

            JSONObject data = new JSONObject();
            JSONObject param = new JSONObject();
            if (deveui != null && deveui != "" && deveui.length() > 0) {
                data.put("devEUI", deveui);
                data.put("data", "\\x" + content);
                data.put("userSec", LoraConfig.userSec);
                data.put("type", LoraConfig.lora_send_type);
                data.put("request_id", request_id);
                param.put("params", data);
            } else {//群组发送，此处暂时没有用到，需在平台加入组播，生成组播ID
//				data.put("groupId", LoraConfig.groupid);
                data.put("data", "\\x" + content);
                param.put("params", data);
            }
            String bodyString = param.toString();
            System.out.println("sendMsgDo bodyString=" + bodyString);
            StringEntity entity = new StringEntity(bodyString, ContentType.create("plain/text", Consts.UTF_8));
            entity.setChunked(true);

            post.setEntity(entity);

            try {
                bodyString = EntityUtils.toString(entity);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String secret = LoraConfig.userID + currentTime + LoraConfig.userSec;
            try {
                SecretKey secretKey = new SecretKeySpec(secret.getBytes("US-ASCII"), "HmacSHA1");
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(secretKey);

                byte[] text = bodyString.getBytes("US-ASCII");
                byte[] finalText = mac.doFinal(text);
                token = Base64.getEncoder().encodeToString(finalText);
                post.setHeader("token", token);

                HttpClient httpClient = getHttpClient(apiUrl);

                HttpResponse httpResponse = httpClient.execute(post);

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                //if (statusCode == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());

                //}
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("sendMsgDo strResult=" + strResult);
        }
        return strResult;
    }

    private HttpClient getHttpClient(String apiUrl) {
        if (apiUrl.startsWith("https")) {
            try {
                return new SSLClient();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        } else {
            return new DefaultHttpClient();
        }

    }


    class SSLClient extends DefaultHttpClient {
        public SSLClient() throws Exception {
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }

}
