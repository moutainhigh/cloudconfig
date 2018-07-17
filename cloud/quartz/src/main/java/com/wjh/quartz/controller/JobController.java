package com.wjh.quartz.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.quartz.service.TaskService;
import com.wjh.quartzmodel.model.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "任务相关接口")
@RestController
@RequestMapping("/job")
public class JobController {
    Logger logger = LogManager.getLogger();

    @Autowired(required = false)
    private TaskService taskService;


    @ApiOperation(value = "查询任务列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list() {
        try {

            List<TaskVo> infos = taskService.list();
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(infos);
            responseModel.setTotalRows(infos.size());
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
    }

    @ApiOperation(value = "保存任务")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@ApiParam(value = "任务名称", required = true) @RequestParam(required = true) String jobName,
                              @ApiParam(value = "任务组名称", required = true) @RequestParam(required = true) String jobGroup,
                              @ApiParam(value = "任务描述", required = true) @RequestParam(required = true) String jobDescription,
                              @ApiParam(value = "cron表达式", required = true) @RequestParam(required = true) String cronExpression) {
        try {
            TaskVo taskInfo = new TaskVo();
            taskInfo.setJobName(jobName);
            taskInfo.setJobGroup(jobGroup);
            taskInfo.setJobDescription(jobDescription);
            taskInfo.setCronExpression(cronExpression);
            taskService.addJob(taskInfo);
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
        return new ResponseModel();
    }

    @ApiOperation(value = "修改任务")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "任务ID", required = true) @RequestParam(required = true) Integer id,
                                @ApiParam(value = "任务名称", required = true) @RequestParam(required = true) String jobName,
                                @ApiParam(value = "任务组名称", required = true) @RequestParam(required = true) String jobGroup,
                                @ApiParam(value = "任务描述", required = true) @RequestParam(required = true) String jobDescription,
                                @ApiParam(value = "cron表达式", required = true) @RequestParam(required = true) String cronExpression) {
        try {

            TaskVo taskInfo = new TaskVo();
            taskInfo.setId(id);
            taskInfo.setJobName(jobName);
            taskInfo.setJobGroup(jobGroup);
            taskInfo.setJobDescription(jobDescription);
            taskInfo.setCronExpression(cronExpression);
            taskService.edit(taskInfo);
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
        return new ResponseModel();
    }

    @ApiOperation(value = "删除任务")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@ApiParam(value = "任务名称", required = true) @RequestParam(required = true) String jobName,
                                @ApiParam(value = "任务组名称", required = true) @RequestParam(required = true) String jobGroup) {
        try {
            taskService.delete(jobName, jobGroup);
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
        return new ResponseModel();
    }

    @ApiOperation(value = "暂停任务")
    @RequestMapping(value = "/pause", method = RequestMethod.PUT)
    public ResponseModel pause(@ApiParam(value = "任务名称", required = true) @RequestParam(required = true) String jobName,
                               @ApiParam(value = "任务组名称", required = true) @RequestParam(required = true) String jobGroup) {
        try {
            taskService.pause(jobName, jobGroup);
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
        return new ResponseModel();
    }

    @ApiOperation(value = "重新开始任务")
    @RequestMapping(value = "/resume", method = RequestMethod.PUT)
    public ResponseModel resume(@ApiParam(value = "任务名称", required = true) @RequestParam(required = true) String jobName,
                                @ApiParam(value = "任务组名称", required = true) @RequestParam(required = true) String jobGroup) {
        try {
            taskService.resume(jobName, jobGroup);
        } catch (Exception e) {
            logger.error(ServiceIdConstant.quartz, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }
        return new ResponseModel();
    }
}
