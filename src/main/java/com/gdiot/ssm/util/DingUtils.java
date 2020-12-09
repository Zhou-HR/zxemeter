package com.gdiot.ssm.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;

/**
 * @Description:对接钉钉的工具类
 * @Authod:zhang_cq
 * @Date:2018/3/19 下午2:59
 */
public class DingUtils {
    //国动集团 test
//	public static Long AGENTID = 269962304L; // 自动分配微应用的ID
//	public static String APPKEY = "dingjnfheu6vhbcxvugm"; // 
//	public static String APPSECRET = "JIVA8V4KpbJZnRb2x2FCtpBonRjC22HUuP4eGVE3ogeRUUSsXBoO1Bq5ROU4hw_M"; // 

    //国动集团 正式
//	public static Long AGENTID = 274787266L; // 
//	public static String APPKEY = "dingmjyy7k9kc8xjsm1d"; // 自动分配微应用的ID
//	public static String APPSECRET = "WGjd37lRf8Z7sfQDljB8mfldHmCQ3aeHbEBkfnieQSeDzq3LhgYn1yiTm5cnnORW"; // 

    //国动信息技术 智能电表
//	public static Long AGENTID = 270133545L; // 自动分配微应用的ID
//	public static String APPKEY = "ding72oqzkdtqfhfqepu"; // 
//	public static String APPSECRET = "KMt5fM_R3F-HcndXY4iXiwxe7sEvkKL4nFwFH1Utj-nk9X9W_mJOZqT4UX40abm6"; // 

    public static String tokenURL = "https://oapi.dingtalk.com/gettoken";
    public static String sendMsgURL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    public static String getUserCountURL = "https://oapi.dingtalk.com/user/get_org_user_count";

    //国动信息技术 钉钉出差审批
    public static Long AGENTID = 341334746L; // 自动分配微应用的ID
    public static String APPKEY = "ding6hfazloykplfk2fy"; //
    public static String APPSECRET = "hhkzxS15gFvY3VI8Uolj88T3JaUiZJxtFMoZQau2U2875bVdnVIAzZsRCkE55Bgf"; //
    public static String processCode = "PROC-04F5093E-F179-4DE6-965B-4A5B3E04EE83";
    public static String processURL = "https://oapi.dingtalk.com/topapi/processinstance/listids?access_token=ACCESS_TOKEN";
    public static String processDetailURL = "https://oapi.dingtalk.com/topapi/processinstance/get?access_token=ACCESS_TOKEN";

    //国动物联网
//	public static Long AGENTID = 270131679L; // 
//	public static String APPKEY = "dinghdpce2zfpjaq9w2y"; // 自动分配微应用的ID
//	public static String APPSECRET = "gN7uhVov9Ozq9-caeqScsaCq-SOeWBsjID4VnmZgCTN2UzLRSagOFBvtYkmE4RBw"; // 

    public static String getToken() {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(tokenURL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(APPKEY);
            request.setAppsecret(APPSECRET);
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
//			System.out.println("getToken()-----"+response);
            return response.getAccessToken();
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static OapiMessageCorpconversationAsyncsendV2Response sendMessage(String userIdList, String textMsg) {
        try {
            String accessToken = getToken();
            System.out.println("AccessToken=" + accessToken);

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");

            OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            request.setUseridList(userIdList);//TODO 用户列表
            request.setAgentId(AGENTID);
            request.setToAllUser(false);

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("text");
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent(textMsg);
            request.setMsg(msg);

//			msg.setMsgtype("image");
//			msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
//			msg.getImage().setMediaId("@lADOdvRYes0CbM0CbA");
//			request.setMsg(msg);

//			msg.setMsgtype("file");
//			msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
//			msg.getFile().setMediaId("@lADOdvRYes0CbM0CbA");
//			request.setMsg(msg);

//			msg.setMsgtype("link");
//			msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
//			msg.getLink().setTitle("test");
//			msg.getLink().setText("test");
//			msg.getLink().setMessageUrl("test");
//			msg.getLink().setPicUrl("test");
//			request.setMsg(msg);

//			msg.setMsgtype("markdown");
//			msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
//			msg.getMarkdown().setText("##### text");
//			msg.getMarkdown().setTitle("### Title");
//			request.setMsg(msg);

//			msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
//			msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
//			msg.getOa().getHead().setText("head");
//			msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
//			msg.getOa().getBody().setContent("xxx");
//			msg.setMsgtype("oa");
//			request.setMsg(msg);

//			msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
//			msg.getActionCard().setTitle("xxx123411111");
//			msg.getActionCard().setMarkdown("### 测试123111");
//			msg.getActionCard().setSingleTitle("测试测试");
//			msg.getActionCard().setSingleUrl("https://www.baidu.com");
//			msg.setMsgtype("action_card");
//			request.setMsg(msg);

            OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, accessToken);
            System.out.println("getToken()-----" + response);
            return response;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        OapiMessageCorpconversationAsyncsendV2Response res = new OapiMessageCorpconversationAsyncsendV2Response();
        res.setCode("1");
        res.setBody("send msg error!");
        res.setErrcode(1L);
        res.setErrmsg("send msg error!");
        return res;
    }

    public static Long getUserCount() {
        try {
            String accessToken = getToken();
            System.out.println("AccessToken=" + accessToken);

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_org_user_count");
            OapiUserGetOrgUserCountRequest request = new OapiUserGetOrgUserCountRequest();
            request.setOnlyActive(1L);
            request.setHttpMethod("GET");
            OapiUserGetOrgUserCountResponse response = client.execute(request, accessToken);
            System.out.println("getToken()-----" + response);
            return response.getCount();
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0L;
    }

    public static OapiProcessinstanceListidsResponse getPorcessListId(long startTime, long endTime, String userId) {
        try {
            String accessToken = getToken();
            System.out.println("AccessToken=" + accessToken);

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
            OapiProcessinstanceListidsRequest req = new OapiProcessinstanceListidsRequest();
            req.setProcessCode(processCode);
            req.setStartTime(startTime);
            req.setEndTime(endTime);
            req.setSize(10L);
            req.setCursor(0L);
            req.setUseridList(userId);
            OapiProcessinstanceListidsResponse response = client.execute(req, accessToken);
            System.out.println("response-----" + response);
            return response;
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static OapiProcessinstanceGetResponse getPorcessInstance(String processId) {
        try {
            String accessToken = getToken();
            System.out.println("AccessToken=" + accessToken);

            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
            OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
            request.setProcessInstanceId(processId);
            OapiProcessinstanceGetResponse response = client.execute(request, accessToken);
            System.out.println("response-----" + response);
            return response;
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}


