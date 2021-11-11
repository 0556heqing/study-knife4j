package com.heqing.knife4j.controller;

import com.heqing.knife4j.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @author heqing
 * @date 2021/8/17 11:36
 */
@Api(value = "示例 - 文件",tags = "文件管理")
@Controller
@RequestMapping("/api/upload")
public class FileController {

    @ApiOperation(value = "单文件上传")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file", value = "文件流对象", required = true, dataType = "__File"),
        @ApiImplicitParam(name = "filePath", value = "filePath", required = true, defaultValue = "D:/test/")
    })
    @RequestMapping(value ="/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResultUtil uploadFile(@RequestParam(value="file",required = true) MultipartFile file, @RequestParam(value = "filePath") String filePath) {
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);
        List<Map> uploadFiles= upload(fileList.toArray(new MultipartFile[]{}), filePath);
        return ResultUtil.buildSuccess(uploadFiles);
    }


    @ApiOperation(value = "多文件上传")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "file[]", value = "文件流对象,接收数组格式", required = true, dataType = "MultipartFile", allowMultiple = true),
        @ApiImplicitParam(name = "filePath", value = "filePath", required = true, defaultValue = "D:/test/")
    })
    @RequestMapping(value="/uploadMultipartFile", method = RequestMethod.POST)
    @ResponseBody
    public ResultUtil uploadMultipartFile(@RequestParam(value="file[]",required = true) MultipartFile[] files, @RequestParam(value = "filePath") String filePath) {
        List<Map> uploadFiles = upload(files, filePath);
        return ResultUtil.buildSuccess(uploadFiles);
    }

    /**
     * 上传文件
     * @param files 文件列表
     * @param filePath 上传文件地址
     * @return
     */
    private List<Map> upload(MultipartFile[] files, String filePath){
        File realFile=new File(filePath);
        if (!realFile.exists()){
            realFile.mkdirs();
        }

        List<Map> uploadFiles=new ArrayList<>();
        for (MultipartFile file : files) {
            File targetFile=new File(realFile, file.getOriginalFilename());
            FileOutputStream out=null;
            InputStream ins=null;
            try{
                out=new FileOutputStream(targetFile);
                int i=-1;
                byte[] bytes=new byte[1024*1024];
                ins=file.getInputStream();
                while ((i=ins.read(bytes))!=-1){
                    out.write(bytes,0,i);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if (ins!=null){
                    try {
                        ins.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out!=null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Map fileInfo= new HashMap(4);
            fileInfo.put("id", UUID.randomUUID().toString());
            fileInfo.put("url",targetFile.getPath());
            fileInfo.put("original_name",targetFile.getName());
            uploadFiles.add(fileInfo);
        }
        return uploadFiles;
    }


    @ApiOperation(value="图片预览")
    @GetMapping(value = "/preview",produces = "image/jpeg")
    public void preview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int width=65,height=20;
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(230, 255));
        g.fillRect(0, 0, 100, 25);
        // 设定字体
        g.setFont(new Font("Arial", Font.CENTER_BASELINE | Font.ITALIC, 18));
        // 产生0条干扰线，
        g.drawLine(0, 0, 0, 0);
        // 取随机产生的认证码(4位数字)
        int checkCodeNum = 4, num = 5;
        for (int i = 0; i < checkCodeNum; i++) {
            String rand = String.valueOf(random.nextInt(10));
            // 将认证码显示到图象中
            g.setColor(getRandColor(100, 150));
            g.drawString(rand, 15 * i + 6, 16);
        }
        for(int i=0;i<(random.nextInt(num)+num);i++){
            g.setColor(new Color(random.nextInt(255)+1,random.nextInt(255)+1,random.nextInt(255)+1));
            g.drawLine(random.nextInt(100),random.nextInt(30),random.nextInt(100),random.nextInt(30));
        }
        // 将验证码存入页面KEY值的SESSION里面
        // 图象生效
        g.dispose();
        ServletOutputStream responseOutputStream = response.getOutputStream();
        // 输出图象到页面
        ImageIO.write(image, "JPEG", responseOutputStream);
        // 以下关闭输入流！
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 获取随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        int maxNum = 255;
        Random random = new Random();
        if (fc > maxNum) {
            fc = maxNum;
        }
        if (bc > maxNum) {
            bc = maxNum;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    @ApiOperation(value = "下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType ="query", name = "filePath", value = "文件地址", required = true, dataType = "String", defaultValue = "D:/test/"),
            @ApiImplicitParam(paramType ="query", name = "fileName", value = "文件名", required = true, dataType = "String", defaultValue = "1.png")
    })
    @GetMapping(value = "/downloadFile1",produces = "application/octet-stream")
    public void postRequest2(HttpServletRequest request, HttpServletResponse response, @RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName){
        download(request, response, filePath, fileName);
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param filePath 文件地址
     * @param fileName 文件名
     */
    private void download(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) {
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        //用于记录以完成的下载的数据量，单位是byte
        try {
            //打开本地文件流
            InputStream inputStream = new FileInputStream(filePath+fileName);
            //激活下载操作
            OutputStream os = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
