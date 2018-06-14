package com.xkd.controller;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *创建人 巫建辉
 * 副件上传功能
 */

@Api(description = "附件上传等----通用")
@Controller
@RequestMapping("/attachement")
@Transactional
public class AttachmentController extends BaseController {


    /**
     *接收附件字节，并写入指定文件夹中，将对应的附件路径返回
     * @param files
     * @param req
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/uploadAttachment", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "上传附件", response = ResponseDbCenter.class, notes = "")
    public ResponseDbCenter uploadAttachment(@RequestParam(value = "files", required = false) MultipartFile[] files,
                                             HttpServletRequest req) throws Exception {

        if (files == null) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "attachement";
        String httpPath = PropertiesUtil.FILE_HTTP_PATH + "attachement";

        List<String> fileList = FileUtil.fileUpload(files, uploadPath, httpPath);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(fileList);

        return responseDbCenter;
    }

}
