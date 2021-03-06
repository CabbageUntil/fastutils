package com.cabbage.controller;


import com.cabbage.bean.User;
import com.cabbage.service.UserService;
import com.cabbage.utils.FastDFSClientUtil;
import com.core.enums.ResultCode;
import com.core.result.Result;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileController {

    @Autowired
    private FastDFSClientUtil dfsClient;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String uploadPage() {
        return "Hello";
    }

    @PostMapping("/upload")
    public Result fdfsUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Result result = new Result();
        String fileUrl = null;
        try {
            fileUrl = dfsClient.uploadFile(file);
            result.setResultCode(ResultCode.SUCCESS);
            result.setData(fileUrl);
            request.setAttribute("msg", "成功上传文件，  '" + fileUrl + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        userService.addUser(new User());
        return result;
       // return new Result(ResultCode.REQUEST_NOT_FOUND.code(), "小伙子你有点调皮哦！(*^▽^*)");
    }

    /*
     * http://localhost/download?filePath=group1/M00/00/00/wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
     */
    @RequestMapping("/download")
    public void download(String filePath ,HttpServletRequest request ,HttpServletResponse response) throws IOException {

        //    group1/M00/00/00/wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
        String[] paths = filePath.split("/");
        String groupName = null ;
        for (String item : paths) {
            if (item.indexOf("group") != -1) {
                groupName = item;
                break ;
            }
        }
        String path = filePath.substring(filePath.indexOf(groupName) + groupName.length() + 1, filePath.length());
        InputStream input = dfsClient.download(groupName, path);

        //根据文件名获取 MIME 类型
        String fileName = paths[paths.length-1] ;
        System.out.println("fileName :" + fileName); // wKgIZVzZEF2ATP08ABC9j8AnNSs744.jpg
        String contentType = request.getServletContext().getMimeType(fileName);
        String contentDisposition = "attachment;filename=" + fileName;

        // 设置头
        response.setHeader("Content-Type",contentType);
        response.setHeader("Content-Disposition",contentDisposition);

        // 获取绑定了客户端的流
        ServletOutputStream output = response.getOutputStream();

        // 把输入流中的数据写入到输出流中
        IOUtils.copy(input,output);
        input.close();

    }

    /**
     * http://localhost/deleteFile?filePath=group1/M00/00/00/wKgIZVzZaRiAZemtAARpYjHP9j4930.jpg
     * @param filePath  group1/M00/00/00/wKgIZVzZaRiAZemtAARpYjHP9j4930.jpg
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/deleteFile")
    public String delFile(String filePath , HttpServletRequest request , HttpServletResponse response)  {

        try {
            dfsClient.delFile(filePath);
        } catch(Exception e) {
            // 文件不存在报异常 ： com.github.tobato.fastdfs.exception.FdfsServerException: 错误码：2，错误信息：找不到节点或文件
            // e.printStackTrace();
        }
        request.setAttribute("msg", "成功删除，'" + filePath);

        return "index";
    }

}
