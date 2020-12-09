package com.gdiot.ssm.cmds;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdiot.ssm.http.yd.BasicResponse;
import com.gdiot.ssm.http.yd.CmdsResponse;
import com.gdiot.ssm.util.DateUtil;
import com.gdiot.ssm.util.HttpClientUtil;
import com.gdiot.ssm.util.LoraConfig;
import com.gdiot.ssm.util.YDConfig;


/**
 * @author ZhouHR
 */
public class SendCmds {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SendCmds() {
    }

    public JSONObject sendDownCmd(Map<String, String> map, String request_id) {
//		logger.info("--------------sendDownCmd------------");
        String module_type = map.get("module_type");
        JSONObject result = null;
        logger.info("--------------module_type==" + module_type);
        if ("gdyd_2g".equals(module_type)) {
            String url = "http://47.103.102.210:6081/emgdyd/tcp_send_cmds";
            logger.info("--------------getCmdsInfo--gdyd_2g---url==" + url);
            result = SendMsgGDYD2G(url, map);
            logger.info("gdyd_2g-----result=" + result.toJSONString());
        } else {
            String content = map.get("content").toString();
            String imei = map.get("imei").toString();
            if (content != null && content != "" && content.length() > 0) {
                if ("nb".equals(module_type)) {
                    String time = map.get("time") != null ? map.get("time") : String.valueOf(System.currentTimeMillis());
                    String url = YDConfig.YD_EXECUTE_URL + "imei=" + imei + "&obj_id=" + YDConfig.obj_id
                            + "&obj_inst_id=" + YDConfig.obj_inst_id + "&res_id=" + YDConfig.res_id;
                    //				logger.info("--------------getCmdsInfo---url=="+ url);
                    String api_key = YDConfig.ZX_XB_EM_API_KEY;
                    result = SendMsgNB(url, content, time, api_key);
                    logger.info("nb----------result=" + result.toString());
                } else if ("lora_em".equals(module_type)) {
                    String result0 = sendMsgLora(content, imei, request_id);
                    result = JSONObject.parseObject(result0);
                    logger.info("lora--------result=" + result.toString());
                } else if ("mqtt_2g".equals(module_type)) {
                    String url = "http://47.103.102.210:8081/ssm/mqtt_2g/send_cmd";
                    //				logger.info("--------------getCmdsInfo---url=="+ url);
                    String time = DateUtil.milliSecond2Date(String.valueOf(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
                    result = SendMsgGprs(url, imei, content, time);
                    logger.info("mqtt_2g-----result=" + result.toJSONString());
                } else {
                    result = new JSONObject();
                    result.put("error", 100);
                }
            }
        }

        return result;
    }

    public JSONObject SendMsgNB(String url, String content, String time, String api_key) {
        NBSendCmds mSendCmds = new NBSendCmds(url, content, time, api_key);
        BasicResponse<CmdsResponse> response = mSendCmds.executeApi();

        JSONObject jo = JSONObject.parseObject(response.getJson().toString());
        String errno = String.valueOf(jo.get("errno"));
        String error = String.valueOf(jo.get("error"));
        logger.info("send_cmd--errno=" + errno + ",error=" + error + ",data:" + jo.get("data"));
        logger.info("result=" + jo.toString());
        return jo;
    }

    public JSONObject SendMsgGprs(String url, String imei, String msg, String time) {//url,imei,content,time
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Content-Type", "application/json");

        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("imei", imei);
        bodys.put("content", msg);
        bodys.put("time", time);
        JSONObject responseBody = null;
        try {
            String result = HttpClientUtil.postJson(url, bodys, "utf8");
            if (result != null) {
                responseBody = JSON.parseObject(result);//new JSONObject(result);
            } else {
                responseBody = new JSONObject();
                responseBody.put("errno", 1);
                responseBody.put("error", "fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    public JSONObject SendMsgGDYD2G(String url, Map<String, String> map) {
        JSONObject responseBody = null;
        if (map == null || map.isEmpty()) {
            return responseBody;
        }
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Content-Type", "application/json");

        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("dev_id", map.get("imei"));
        bodys.put("operate_type", map.get("operate_type"));
        bodys.put("data_type", map.get("type"));
        bodys.put("value", map.get("value"));

        try {
            String result = HttpClientUtil.postJson(url, bodys, "utf8");
            if (result != null) {
                responseBody = JSON.parseObject(result);//new JSONObject(result);
                logger.info("--------------gdyd_2g---responseBody==" + responseBody);
            } else {
                responseBody = new JSONObject();
                responseBody.put("statusCode", 111);
                responseBody.put("result", "result null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    private String sendMsgLora(String content, String deveui, String request_id) {
        String strResult = "error!";
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

//					JSONObject resultJson = JSONObject.parseObject(strResult0);
//					logger.info("lora--------result="+resultJson.toString());
//					//{"result":{"task_id":"1577325797.178_rmuOjMCWV3PBb3IqkppTbdzBSDmrIsCh",
//					//"request_id":"0000000000009276_1577325797122"},"error":0}
//					String resultStr = resultJson.getString("result");
//					JSONObject resultJsonSub = JSONObject.parseObject(resultStr);
//					int error = resultJsonSub.getInteger("error");
//					String request_id_back = resultJsonSub.getString("request_id");
//					JSONObject result = new JSONObject();
//					if(request_id.equals(request_id_back) && error == 0) {
//						result.put("errno", error);
//						result.put("error", "succ");
//					}else {
//						result.put("errno", 1);
//						result.put("error", "fail");
//					}
//					strResult = strResult0;//result.toJSONString();
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
