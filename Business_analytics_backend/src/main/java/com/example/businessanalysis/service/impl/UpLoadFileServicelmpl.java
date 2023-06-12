//package com.example.businessanalysis.service.impl;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.ObjectUtil;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.businessanalysis.domain.UpLoadFile;
//import com.example.businessanalysis.domain.User;
//import com.example.businessanalysis.mapper.UpLoadFileMapper;
//import com.example.businessanalysis.mapper.UserMapper;
//import com.example.businessanalysis.service.UpLoadFileService;
//import com.example.businessanalysis.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
///**
// * @ClassName UpLoadFileServicelmpl
// * @Description 实现
// * @Author 74707
// * @Date 2022/11/9 16:56
// * @Version V1.0
// */
//@Service
//@RequiredArgsConstructor
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
//public class UpLoadFileServicelmpl extends ServiceImpl<UpLoadFileMapper,UpLoadFile>
//        implements UpLoadFileService {
//    private final UpLoadFileMapper uploadFileMapper;
//
//    private String path;
//
//    private long maxSize;
//
//    @Override
//    public UpLoadFile upload(String uploader, String realName, MultipartFile multipartFile) {
//        //检查文件大小
//        if (multipartFile.getSize() > maxSize * 1024*1024) {
//            throw new RuntimeException("超出文件上传大小限制" + maxSize + "MB");
//        }
//        //获取上传文件的主文件名与扩展名
//        String primaryName = FileUtil.mainName(multipartFile.getOriginalFilename());
//        String extension = FileUtil.extName(multipartFile.getOriginalFilename());
//        //根据文件扩展名得到文件类型
//        String type = getFileType(extension);
//        //给上传的文件加上时间戳
//        LocalDateTime date = LocalDateTime.now();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddhhmmssS");
//        String nowStr = "-" + date.format(format);
//        String fileName = primaryName + nowStr + "." + extension;
//
//        try {
//            String filePath = path + type + File.separator + fileName;
//            File dest = new File(filePath).getCanonicalFile();
//            if (!dest.getParentFile().exists()) {
//                if (ObjectUtil.isNull(dest.getParentFile().mkdirs())) {
//                    throw new RuntimeException("上传文件失败：建立目录错误");
//                }
//            }
//            multipartFile.transferTo(dest);
//            if (ObjectUtil.isNull(dest)) {
//                throw new RuntimeException("上传文件失败");
//            }
//
//            UpLoadFile uploadFile = new UpLoadFile(null, realName, fileName, primaryName,
//                    extension, dest.getPath(), type, multipartFile.getSize(),
//                    uploader, Timestamp.valueOf(LocalDateTime.now()));
//            if (uploadFileMapper.insert(uploadFile) > 0) {
//                return uploadFile;
//            }
//            throw new RuntimeException("上传文件失败");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
//
//    /**
//     * 根据文件扩展名给文件类型
//     *
//     * @param extension 文件扩展名
//     * @return 文件类型
//     */
//    private static String getFileType(String extension) {
//        String document = "txt doc pdf ppt pps xlsx xls docx csv";
//        String music = "mp3 wav wma mpa ram ra aac aif m4a";
//        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
//        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
//        if (image.contains(extension)) {
//            return "image";
//        } else if (document.contains(extension)) {
//            return "document";
//        } else if (music.contains(extension)) {
//            return "music";
//        } else if (video.contains(extension)) {
//            return "video";
//        } else {
//            return "other";
//        }
//    }
//
//}
