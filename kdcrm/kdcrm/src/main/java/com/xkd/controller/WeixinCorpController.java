package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.WeixinCorpUtil;
import com.xkd.weixincorp.MessageUtil;
import com.xkd.weixincorp.WXBizMsgCrypt;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@Transactional
@RequestMapping("/weixinCorp")
public class WeixinCorpController {

    /**
     *
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/messageReceive",method = {RequestMethod.POST,RequestMethod.GET})
    public String messageReceive(HttpServletRequest req, HttpServletResponse rsp) throws Exception {

        String sToken = "kzSyyZKMqJ";
        String sCorpID = "ww60583bfe4e343c0d";
        String sEncodingAESKey = "8BbvBmuzFVMF1zQSvPf6hXuDy84zCFq9LmN3QHL7Cgw";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		/*
		------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业微信会向验证url发送一个get请求
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及企业微信推送过来的随机加密字符串(echostr),
		这一步注意作URL解码。
		2.验证消息体签名的正确性
		3. 解密出echostr原文，将原文当作Get请求的response，返回给企业微信
		第2，3步可以用企业微信提供的库函数VerifyURL来实现。

		*/
        // 解析出url上的参数值如下：
        String sVerifyMsgSig = req.getParameter("msg_signature");
        String sVerifyTimeStamp = req.getParameter("timestamp");
        String sVerifyNonce = req.getParameter("nonce");
        //第一次会带上这个参数，后续的消息不会带上这个参数
        String sVerifyEchoStr = req.getParameter("echostr");
        String sEchoStr = null; //需要返回的明文

        PrintWriter pw = null;
        PrintWriter out = null;

        try {

            ////第一次会验证url后续的不会验证url
            if(StringUtils.isNotBlank(sVerifyEchoStr)){
                System.out.println("===msg_signature:"+sVerifyMsgSig+"=====timestamp:"+sVerifyTimeStamp+"============nonce:"+sVerifyNonce+"=====echostr"+sVerifyEchoStr);

                sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
                        sVerifyNonce, sVerifyEchoStr);
                System.out.println("verifyurl echostr: " + sEchoStr);
                // 验证URL成功，将sEchoStr返回

                out = rsp.getWriter();
                out.write(sEchoStr);

                out.close();
                out = null;

            }else{

                InputStream inputStream = req.getInputStream();
                // 读取输入流
                SAXReader reader = new SAXReader();
                Document document = reader.read(inputStream);
                String reqData = document.asXML();


              /*  Map requestMap = MessageUtil.parseXml(req);
                System.out.println("xml map=====================: " + requestMap.toString());
                // 发送方帐号
                String fromUserName = (String) requestMap.get("FromUserName");
                // 开发者微信号
                String toUserName = (String)requestMap.get("ToUserName");
                // 消息类型
                String msgType = (String)requestMap.get("MsgType");
                String agentID = (String)requestMap.get("AgentID");*/


              /*  BufferedReader br =  new BufferedReader(new InputStreamReader( req.getInputStream()));
                StringBuffer reqData = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null){
                    reqData.append(line);
                }*/
                System.out.println("xml==================== : " + reqData);
                String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, reqData);
                System.out.println("after decrypt msg=================: " + sMsg);
                // TODO: 解析出明文xml标签的内容进行处理
                // For example:


                String userId = sMsg.substring(sMsg.indexOf("<FromUserName><![CDATA[")+"<FromUserName><![CDATA[".length(),sMsg.indexOf("]]></FromUserName>"));
                String corpID = sMsg.substring(sMsg.indexOf("<ToUserName><![CDATA[")+"<ToUserName><![CDATA[".length(),sMsg.indexOf("]]></ToUserName>"));
                String agentID = sMsg.substring(sMsg.indexOf("<AgentID>")+"<AgentID>".length(),sMsg.indexOf("</AgentID>"));
                String msgType = sMsg.substring(sMsg.indexOf("<MsgType><![CDATA[")+"<MsgType><![CDATA[".length(),sMsg.indexOf("]]></MsgType>"));
                String content = sMsg.substring(sMsg.indexOf("<Content><![CDATA[")+"<Content><![CDATA[".length(),sMsg.indexOf("]]></Content>"));


                //对信息判断，回复信息
                if(MessageUtil.REQ_MESSAGE_TYPE_TEXT.equalsIgnoreCase(msgType) && "test".equalsIgnoreCase(content)){

                    String sReqTimeStamp = new Date().getTime()+"";
                    String sReqNonce = new Random().nextInt(899999999) + 100000000+"";

                    String sRespData = "<xml><ToUserName><![CDATA["+userId+"]]></ToUserName>" +
                            "<FromUserName><![CDATA["+corpID+"]]></FromUserName>" +
                            "<CreateTime>"+sReqTimeStamp+"</CreateTime>" +
                            "<MsgType><![CDATA[text]]></MsgType>" +
                            "<Content><![CDATA[this is a test]]></Content>" +
                            "<MsgId>1234567890123456</MsgId>" +
                            "<AgentID>"+agentID+"</AgentID></xml>";

                    System.out.println("responsedxml: " + sRespData);

                    String sEncryptMsg = wxcpt.EncryptMsg(sRespData, sReqTimeStamp, sReqNonce);
                    System.out.println("after encrypt sEncrytMsg: " + sEncryptMsg);
                    // 加密成功
                    // TODO:
                    //HttpUtils.SetResponse(sEncryptMsg);
                    pw = rsp.getWriter();
                    pw.write(sEncryptMsg);

                    pw.close();
                    pw=null;

                }
            }
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
            if(out != null){
                out.close();
                out = null;
            }
            if(pw != null){
                pw.close();
                pw = null;
            }
        }
/*
		*//*
		------------使用示例二：对用户回复的消息解密---------------
		用户回复消息或者点击事件响应时，企业会收到回调消息，此消息是经过企业微信加密之后的密文以post形式发送给企业，密文格式请参考官方文档
		假设企业收到企业微信的回调消息如下：
		POST /cgi-bin/wxpush? msg_signature=477715d11cdb4164915debcba66cb864d751f3e6&timestamp=1409659813&nonce=1372623149 HTTP/1.1
		Host: qy.weixin.qq.com
		Content-Length: 613
		<xml>		<ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt>
		<AgentID><![CDATA[218]]></AgentID>
		</xml>

		企业收到post请求之后应该		1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
		2.验证消息体签名的正确性。
		3.将post请求的数据进行xml解析，并将<Encrypt>标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档
		第2，3步可以用企业微信提供的库函数DecryptMsg来实现。
		*//*
        // String sReqMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sReqMsgSig = "477715d11cdb4164915debcba66cb864d751f3e6";
        // String sReqTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sReqTimeStamp = "1409659813";
        // String sReqNonce = HttpUtils.ParseUrl("nonce");
        String sReqNonce = "1372623149";
        // post请求的密文数据
        // sReqData = HttpUtils.PostData();
        String sRespData = "<xml><ToUserName><![CDATA[mycreate]]></ToUserName><FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId><AgentID>128</AgentID></xml>";

        try {
            String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sRespData);
            System.out.println("after decrypt msg: " + sMsg);
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Content");
            String Content = nodelist1.item(0).getTextContent();
            System.out.println("Content：" + Content);

        } catch (Exception e) {
            // TODO
            // 解密失败，失败原因请查看异常
            e.printStackTrace();
        }

	*//*
		------------使用示例三：企业回复用户消息的加密---------------
		企业被动回复用户的消息也需要进行加密，并且拼接成密文格式的xml串。
		假设企业需要回复用户的明文如下：
		<xml>
		<ToUserName><![CDATA[mycreate]]></ToUserName>
		<FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName>
		<CreateTime>1348831860</CreateTime>
		<MsgType><![CDATA[text]]></MsgType>
		<Content><![CDATA[this is a test]]></Content>
		<MsgId>1234567890123456</MsgId>
		<AgentID>128</AgentID>
		</xml>

		为了将此段明文回复给用户，企业应：			1.自己生成时间时间戳(timestamp),随机数字串(nonce)以便生成消息体签名，也可以直接用从企业微信的post url上解析出的对应值。
		2.将明文加密得到密文。	3.用密文，步骤1生成的timestamp,nonce和企业在企业微信设定的token生成消息体签名。			4.将密文，消息体签名，时间戳，随机数字串拼接成xml格式的字符串，发送给企业。
		以上2，3，4步可以用企业微信提供的库函数EncryptMsg来实现。
		*//*
        sRespData = "<xml><ToUserName><![CDATA[mycreate]]></ToUserName><FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId><AgentID>128</AgentID></xml>";
        try{
            String sEncryptMsg = wxcpt.EncryptMsg(sRespData, sReqTimeStamp, sReqNonce);
            System.out.println("after encrypt sEncrytMsg: " + sEncryptMsg);
            // 加密成功
            // TODO:
            //HttpUtils.SetResponse(sEncryptMsg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
        }*/

        return null;
    }

}
