package com.wf.ew.system.controller;

import com.wf.ew.common.JsonResult;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件服务器
 * Created by wangfan on 2018-12-24 下午 4:10.
 */
@Controller
@RequestMapping("/file")
public class FileController {
    private static final int UPLOAD_DIS_INDEX = 0;  // 上传到第几个磁盘下面
    private static final String UPLOAD_DIR = "/upload/";  // 上传的目录
    private static final boolean UUID_NAME = false;  // 是否用uuid命名

    @RequestMapping("/view")
    public String view() {
        return "system/file.html";
    }

    /**
     * 上传文件
     */
    @ResponseBody
    @PostMapping("/upload")
    public JsonResult upload(@RequestParam MultipartFile file) {
        String path;  // 文件路径
        // 文件原始名称
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);  // 获取文件后缀
        File outFile;
        if (UUID_NAME) {  // uuid命名
            path = getDate() + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            outFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + path);
        } else {  // 使用原名称，存在相同着加(1)
            String prefix = originalFileName.substring(0, originalFileName.lastIndexOf("."));  // 获取文件名称
            path = getDate() + originalFileName;
            outFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + path);
            int sameSize = 1;
            while (outFile.exists()) {
                path = getDate() + prefix + "(" + sameSize + ")." + suffix;
                outFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + path);
                sameSize++;
            }
        }
        try {
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            file.transferTo(outFile);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("上传失败").put("error", e.getMessage());
        }
        return JsonResult.ok("上传成功").put("url", path);
    }

    /**
     * 上传文件
     */
    @ResponseBody
    @PostMapping("/uploadBase64")
    public JsonResult uploadBase64(String base64) {
        if (base64 != null && !base64.trim().isEmpty()) {
            String suffix = base64.substring(11, base64.indexOf(";"));  // 获取文件格式
            String path = getDate() + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            File outFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + path);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            FileOutputStream out = null;
            try {
                byte[] bytes = Base64.getDecoder().decode(base64.substring(base64.indexOf(";") + 8).getBytes());
                out = new FileOutputStream(outFile);
                out.write(bytes);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.error("上传失败").put("error", e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return JsonResult.ok("上传成功").put("url", path);
        }
        return JsonResult.error("上传失败");
    }

    /**
     * 查看原文件
     */
    @GetMapping("/{y}/{m}/{d}/{file:.+}")
    public void file(@PathVariable("y") String y, @PathVariable("m") String m, @PathVariable("d") String d, @PathVariable("file") String filename, HttpServletResponse response) {
        String filePath = y + "/" + m + "/" + d + "/" + filename;
        outputFile(UPLOAD_DIR + filePath, response, true);
    }

    /**
     * 下载原文件
     */
    @GetMapping("/download/{y}/{m}/{d}/{file:.+}")
    public void downloadFile(@PathVariable("y") String y, @PathVariable("m") String m, @PathVariable("d") String d, @PathVariable("file") String filename, HttpServletResponse response) {
        String filePath = y + "/" + m + "/" + d + "/" + filename;
        outputFile(UPLOAD_DIR + filePath, response, false);
    }

    /**
     * 查看缩略图
     */
    @GetMapping("/sm/{y}/{m}/{d}/{file:.+}")
    public void smFile(@PathVariable("y") String y, @PathVariable("m") String m, @PathVariable("d") String d, @PathVariable("file") String filename, HttpServletResponse response) {
        String orgPath = y + "/" + m + "/" + d + "/" + filename;
        File orgFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + orgPath);
        if (orgFile.exists()) {
            String smPath = UPLOAD_DIR + "sm/" + orgPath;
            File smFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], smPath);
            if (!smFile.exists() && isImgFile(orgFile)) {  // 如果是图片文件并且缩略图不存在
                long fileSize = orgFile.length();
                if ((fileSize / 1024) > 100) {  // 大于100kb生成100kb的缩略图
                    smFile.getParentFile().mkdirs();
                    try {
                        Thumbnails.of(orgFile).scale(100f / (fileSize / 1024f)).toFile(smFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    smPath = UPLOAD_DIR + orgPath;
                }
            }
            outputFile(smPath, response, true);
        } else {
            outNotFund(response);
        }
    }

    /**
     * 按文件夹获取文件列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public JsonResult list(String dir, String exts) {
        String[] mExts = null;
        if (exts != null && !exts.trim().isEmpty()) {
            mExts = exts.trim().split(",");
        }
        if (dir == null) {
            dir = "";
        }
        List<Map> dataList = getFileList(dir, mExts);
        // 根据上传时间排序
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Long l1 = (long) o1.get("createTime");
                Long l2 = (long) o2.get("createTime");
                return l2.compareTo(l1);
            }
        });
        // 把文件夹排在前面
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Boolean l1 = (boolean) o1.get("isDir");
                Boolean l2 = (boolean) o2.get("isDir");
                return l2.compareTo(l1);
            }
        });
        return JsonResult.ok().put("data", dataList);
    }

    /**
     * 获取全部文件列表
     */
    @ResponseBody
    @RequestMapping("/listAll")
    public JsonResult listAll(String dir, String exts) {
        String[] mExts = null;
        if (exts != null && !exts.trim().isEmpty()) {
            mExts = exts.trim().split(",");
        }
        if (dir == null) {
            dir = "";
        }
        List<Map> dataList = getAllFileList(dir, mExts);
        // 根据上传时间排序
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Long l1 = (long) o1.get("createTime");
                Long l2 = (long) o2.get("createTime");
                return l2.compareTo(l1);
            }
        });
        // 把文件夹排在前面
        Collections.sort(dataList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Boolean l1 = (boolean) o1.get("isDir");
                Boolean l2 = (boolean) o2.get("isDir");
                return l2.compareTo(l1);
            }
        });
        return JsonResult.ok().put("data", dataList);
    }

    /**
     * 删除文件
     */
    @ResponseBody
    @RequestMapping("/del")
    public JsonResult del(String file) {
        if (file != null && !file.trim().isEmpty()) {
            File f = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + file);
            if (f.delete()) {
                File smF = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + "sm/" + file);
                smF.delete();
                return JsonResult.ok("删除成功");
            }
        }
        return JsonResult.error("删除失败");
    }

    /**
     * 输出文件流
     */
    private void outputFile(String file, HttpServletResponse response, boolean isPreview) {
        // 判断文件是否存在
        File inFile = new File(File.listRoots()[UPLOAD_DIS_INDEX], file);
        if (!inFile.exists()) {
            outNotFund(response);
            return;
        }
        if (isPreview) {
            String contentType = getFileType(inFile);  // 获取文件类型
            if (contentType != null) {
                response.setContentType(contentType);
            } else {
                setDownloadHeader(inFile.getName(), response);
            }
        } else {
            setDownloadHeader(inFile.getName(), response);
        }
        // 输出文件流
        FileInputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件类型
     */
    private String getFileType(File file) {
        String contentType = null;
        try {
            contentType = new Tika().detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }

    /**
     * 判断是否是图片类型
     */
    private boolean isImgFile(File file) {
        String contentType = getFileType(file);
        if (contentType != null && contentType.startsWith("image/")) {
            return true;
        }
        return false;
    }

    /**
     * 设置下载文件的header
     */
    private void setDownloadHeader(String fileName, HttpServletResponse response) {
        response.setContentType("application/force-download");
        String newName = fileName;
        try {
            newName = URLEncoder.encode(newName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
    }

    /**
     * 输出文件不存在
     */
    private void outNotFund(HttpServletResponse response) {
        PrintWriter writer;
        try {
            response.setContentType("text/html;charset=UTF-8");
            writer = response.getWriter();
            writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">Easy File Server</p>");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前日期
     */
    private String getDate() {
        return getDate("yyyy/MM/dd/");
    }

    private String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取某一目录下文件列表
     */
    private List<Map> getFileList(String dir, String[] mExts) {
        if (dir.startsWith("/")) {
            dir = dir.substring(1);
        }
        File file = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + dir);
        List<Map> dataList = new ArrayList<>();
        if (!file.exists()) {
            return dataList;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return dataList;
        }
        for (File f : listFiles) {
            Map m = getFileInfo(dir, f, mExts);
            if (m != null) {
                dataList.add(m);
            }
        }
        return dataList;
    }

    /**
     * 获取全部文件列表
     */
    private List<Map> getAllFileList(String dir, String[] mExts) {
        List<Map> dataList = new ArrayList<>();
        if (dir == null) {
            dir = "";
        }
        File file = new File(File.listRoots()[UPLOAD_DIS_INDEX], UPLOAD_DIR + dir);
        if (!file.exists()) {
            return dataList;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return dataList;
        }
        for (File f : listFiles) {
            if ("sm".equals(f.getName())) {  // 是否是缩略图目录
                continue;
            }
            if (f.isDirectory()) {
                dataList.addAll(getAllFileList(dir + "/" + f.getName(), mExts));
            } else {
                Map m = getFileInfo(dir, f, mExts);
                if (m != null) {
                    dataList.add(m);
                }
            }
        }
        return dataList;
    }

    /**
     * 获取某一文件信息
     */
    private Map getFileInfo(String dir, File f, String[] mExts) {
        boolean isDir = f.isDirectory();
        // 判断是否是缩略图目录
        if ("sm".equals(f.getName())) {
            return null;
        }
        // 判断格式是否符合要求
        boolean isOut = true;
        if (!isDir && mExts != null) {
            isOut = false;
            for (String ext : mExts) {
                String suffix = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                if (ext.equalsIgnoreCase(suffix)) {
                    isOut = true;
                    break;
                }
            }
        }
        if (!isOut) {
            return null;
        }
        // 读取文件信息
        Map<String, Object> m = new HashMap<>();
        m.put("isDir", isDir);  // 是否是目录
        m.put("name", f.getName());  // 文件名称
        m.put("updateTime", f.lastModified());  // 修改时间
        try {  // 读取创建时间
            BasicFileAttributeView basicview = Files.getFileAttributeView(f.toPath(), BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attributes = basicview.readAttributes();
            m.put("createTime", attributes.creationTime().toMillis());
        } catch (Exception e) {
            e.printStackTrace();
            m.put("createTime", f.lastModified());
        }
        if (!isDir) {
            String url = (dir.isEmpty() ? "" : (dir + "/")) + f.getName();
            m.put("url", url);  // 文件地址
            if (isImgFile(f)) {
                m.put("smUrl", "sm/" + url);  // 缩略图地址
            }
            m.put("size", f.length());  // 文件地址
        }
        return m;
    }
}
