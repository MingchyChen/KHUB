package com.claridy.facade;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.common.mechanism.dao.ISys_FileDAO;
import com.claridy.common.mechanism.dao.ISys_ParamDAO;
import com.claridy.common.mechanism.domain.FileUploadModel;
import com.claridy.common.mechanism.domain.Sys_File;
import com.claridy.common.mechanism.facase.FileUploadService;
import com.claridy.common.util.SystemProperties;

@Service
public class DataUploadService
{

    // 單內文預設下架日期(9999-01-01)
    final Timestamp _OFFLINETIME = new Timestamp(new Long("253370736000000"));

    @Autowired
    private ISys_ParamDAO sys_ParamDAO;

    @Autowired
    private ISys_FileDAO sys_FileDAO;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private SystemProperties SystemProperties;

    public Map<String, String> linkopentypeMap;

    public List<FileUploadModel> fileUploadModels;


    public void setFileUploadModels(List<FileUploadModel> fileUploadModels)
    {
        this.fileUploadModels = fileUploadModels;
    }


    // 把檔案資料FileUploadModel資訊塞至SYS_FILE
    public void saveUploadedFiles(String uuid,String objname,List<FileUploadModel> fileUploadModels) throws UnsupportedEncodingException
    {
        for (FileUploadModel fileUploadModel : fileUploadModels)
        {
            Sys_File file = new Sys_File();
            String disPlay_file_name = null;
            try
            {
                disPlay_file_name = URLDecoder.decode(
                    fileUploadModel.getName().substring(fileUploadModel.getName().indexOf("-") + 1,
                        fileUploadModel.getName().length()), "utf-8");
            }
            catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }
            file.setObj_pk(uuid);
            file.setObj_name(objname);
            file.setFile_name(URLDecoder.decode(fileUploadModel.getName(), "utf-8"));
            //file.setFile_pk(UUIDGenerator.getUUID());
            file.setDisplay_file_name(disPlay_file_name);
            file.setFile_type(getFileType(fileUploadModel.getName()));
            //file.setCreatoracct(sysUserService.getCurrentUserInfo().getUserID());
            file.setCreateddate(new Timestamp(System.currentTimeMillis()));
            sys_FileDAO.create(file);
        }
    }


    /**
     * 取出檔案的副檔名
     */
    public String getFileType(String fileName)
    {
        String fileType = "";
        try
        {
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return fileType.toLowerCase();
    }


    /**
     * 取出該文苑的附件
     */
    @SuppressWarnings ("unchecked")
    public List<Sys_File> getOpusCntFileList(String uuid)
    {
        StringBuffer str = new StringBuffer();
        str.append("from " + Sys_File.class.getSimpleName() + " ");
        str.append("where ");
        str.append(" obj_pk = '" + uuid + "' ");
        str.append("order by createddate desc");
        return (List<Sys_File>) sys_FileDAO.findByHQL(str.toString());
    }


    @SuppressWarnings ("unchecked")
    public List<Sys_File> getAllFileList(String cntid, String questId, String othercondition)
    {
        StringBuffer str = new StringBuffer();
        str.append("from " + Sys_File.class.getSimpleName() + " ");
        str.append("where ");
        if ((cntid != "2013" && !"2013".equals(cntid)) && (questId == null || "".equals(questId)))
        {
            str.append(" obj_pk = '" + cntid + "' and");
        }
        if ((cntid == "2013" || "2013".equals(cntid)) && (questId != null && !"".equals(questId)))
        {
            str.append(" obj_pk='" + questId + "' and");
        }
        if ((cntid != "2013" && !"2013".equals(cntid)) && (questId != null && !"".equals(questId)))
        {
            str.append(" obj_pk= '" + cntid + "' or obj_pk='" + questId + "' and");
        }
        str.append(" othercondition = '" + othercondition + "' ");
        str.append("order by createddate desc");
        return (List<Sys_File>) sys_FileDAO.findByHQL(str.toString());
    }


    /**
     * 更新附件的othercondition
     */
    public void updateSys_file(String cntid, String oldCondition, String newCondition)
    {
        try
        {
            StringBuffer str = new StringBuffer();
            str.append("update " + Sys_File.class.getSimpleName());
            str.append(" set othercondition = '" + newCondition + "' ");
            str.append("where obj_pk = '" + cntid + "' ");
            str.append(" and othercondition = '" + oldCondition + "' ");
            sys_FileDAO.updateByHQL(str.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 複製sys_file資訊及實體檔案
     */
    public void copySys_File(String path, String uuid, Timestamp createddate)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try
        {
            List<Sys_File> files = this.getOpusCntFileList(uuid);
            for (Sys_File sys_File : files)
            {

                // 複製實體檔案
                String file_name = fileUploadService.copyFile(path, sys_File.getFile_name(),
                    sys_File.getDisplay_file_name());

                // 存到sys_file
                if (file_name != null && !"".equals(file_name))
                {
                    Sys_File newFile = new Sys_File();
                    newFile.setObj_pk(sys_File.getObj_pk());
                    newFile.setObj_name(sys_File.getObj_name());
                    newFile.setFile_name(file_name);
                    newFile.setRemarks(sys_File.getRemarks());
                   // newFile.setCreatoracct(sysUserService.getCurrentUserInfo().getUserID());
                    newFile.setDisplay_file_name(sys_File.getDisplay_file_name());
                    newFile.setFile_type(sys_File.getFile_type());
                    newFile.setCreateddate(createddate);
                    newFile.setOthercondition(dateFormat.format(new Date(createddate.getTime())));
                    sys_FileDAO.create(newFile);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 刪除附件(當未儲存的內文按下取消時需刪除附件)
     */
    public void deleteFile(String uuid)
    {
        // 刪除實體檔案
        List<Sys_File> files = this.getOpusCntFileList(uuid);
        for (Sys_File sys_File : files)
        {
            try
            {
                fileUploadService.deleteFiletoServer(sys_File.getFile_name());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        // 刪除sys_file
        StringBuffer str = new StringBuffer();
        str.append(" delete " + Sys_File.class.getSimpleName());
        str.append(" where obj_pk = '" + uuid + "' ");
        sys_FileDAO.updateByHQL(str.toString());
    }

}