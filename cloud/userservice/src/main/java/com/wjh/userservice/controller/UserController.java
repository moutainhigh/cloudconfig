package com.wjh.userservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.userservice.service.UserService;
import com.wjh.userservicemodel.model.UserDto;
import com.wjh.userservicemodel.model.UserPo;
import com.wjh.userservicemodel.model.UserVo;
import com.wjh.utils.redis.RedisCacheUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.wjh.common.model.ResponseConstant.*;

@Api(description = "用户相关接口")
@RefreshScope
@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LogManager.getLogger();
    @Autowired
    private UserService userService;


    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @ApiOperation(value = "查询用户")
    @RequestMapping(value = "/detailByMobile", method = RequestMethod.GET)
    public ResponseModel detailByMobile(@ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile) {


        logger.debug("parameter mobile{}",mobile);

        try {
            UserVo user = userService.detailByUser(mobile);
            ResponseModel<UserVo> responseModel = new ResponseModel<UserVo>();
            responseModel.setResModel(user);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseModel register(
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "密码，MD5加密", required = true) @RequestParam(required = true) String password,
            @ApiParam(value = "重复密码，MD5加密", required = true) @RequestParam(required = true) String repeatPassword) {
        logger.debug("parameter mobile{},password{},repeatPassword{}",mobile,password,repeatPassword);

        if (!password.equals(repeatPassword)) {
            return USERSERVICE_PASSWORD_NOT_EQUAL;
        }


        UserVo userInDb = userService.detailByUser(mobile);
        if (null != userInDb) {
            return USERSERVICE_MOBILE_EXISTS;
        }

        UserPo user = new UserPo();
        user.setMobile(mobile);
        user.setPassword(password);
        UserPo userR = userService.insert(user);
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResModel(userR);
        return responseModel;

    }


    @ApiOperation(value = "更新")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "用户") @RequestBody(required = true) UserDto user) {
        try {
            logger.debug("parameter user{}",user);

            UserPo userPo=new UserPo();
            BeanUtils.copyProperties(userPo,user);

            UserPo userR = userService.update(userPo);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(userR);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userservice, e);
            return SYSTEM_EXCEPTION;
        }
    }


    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@ApiParam(value = "id") @RequestParam(required = true) String id) {
        logger.debug("parameter id{}",id);

        long idLong = Long.valueOf(id);
        userService.delete(idLong);
        ResponseModel responseModel = new ResponseModel();
        return responseModel;
    }


    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseModel login(
            @ApiParam(value = "唯一标识 /user/authcode 的response的Header中的uniqueKey", required = true) @RequestParam(required = true) String uniqueKey,
            @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
            @ApiParam(value = "密码,MD5加密", required = true) @RequestParam(required = true) String password,
            @ApiParam(value = "验证码", required = true) @RequestParam(required = true) String authCode) {
        try {

            logger.debug("parameter uniqueKey{},mobile{},password{},authCode{}",uniqueKey,mobile,password,authCode);

            /**
             * 验证验证码是否输入正确
             */

            String authCodeInRedis= (String) redisCacheUtil.getCacheObject(uniqueKey);
            if (!authCode.equals(authCodeInRedis)){
                return USERSERVICE_LOGIN_AUTHCODE_ERROR;
            }


            UserVo user = userService.selectByLogin(mobile, password);
            if (null == user) {
                return USERSERVICE_LOGIN_FAIL;
            }
            String token = UUID.randomUUID().toString();


            //查看旧有的token-->userId,  userId-->token关系
            String oldToken = (String) redisCacheUtil.getCacheObject(user.getId() + "");
            if (null != oldToken) {
                redisCacheUtil.delete(oldToken);
            }
            redisCacheUtil.delete(user.getId() + "");


            int expireSeconds=60 * 60 * 2;
            //加入新的token-->userId,  userId-->token关系
            redisCacheUtil.setCacheObject(token, user.getId() + "", expireSeconds);
            redisCacheUtil.setCacheObject(user.getId() + "", token,expireSeconds);

            ResponseModel responseModel = new ResponseModel();
            Map<String,Object> map=new HashMap(1);
            map.put("token",token);
            responseModel.setResModel(map);
            return responseModel;

        } catch (Exception e) {
            logger.error(ServiceIdConstant.userservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
    }



    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/authcode",method = { RequestMethod.GET})
    public void authcode(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int width = 63;
        int height = 37;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman",0,28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for(int i=0;i<40;i++){
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        //绘制字符
        String strCode = "";
        for(int i=0;i<4;i++){
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(rand, 13*i+6, 28);
        }

        String uniqueKey=UUID.randomUUID().toString().toUpperCase();


        //将字符保存到redis中用于前端的验证,5分钟后过期
        redisCacheUtil.setCacheObject(uniqueKey,strCode,60*5);
        g.dispose();

        response.setHeader("uniqueKey",uniqueKey);
        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();

    }
    Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255)
            fc = 255;
        if(bc>255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }




}
