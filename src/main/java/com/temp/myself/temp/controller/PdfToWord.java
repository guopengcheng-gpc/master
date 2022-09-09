package com.temp.myself.temp.controller;

import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.drawing.Path;
import com.alibaba.fastjson.JSONArray;
import com.aspose.pdf.License;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.temp.myself.until.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class PdfToWord {

    private static List listZong;

    @RequestMapping("/pdf")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView("pdfToword.html");

        return modelAndView;
    }
    @RequestMapping("/change")
    public Map pdfToWordUntil(@RequestParam("file")MultipartFile file, HttpServletResponse response){
        Map map = new HashMap();
        try {
            //使用aspose转
            /*getLicense();
            InputStream in = file.getInputStream();
            Document pdf = new Document(in);
            String path = FileSystemView.getFileSystemView() .getHomeDirectory().getAbsolutePath();
            File doc = new File(path + "/"+file.getOriginalFilename().split("[.]")[0]+".doc");
            if (!doc.exists()){
                doc.createNewFile();
            }

            DocSaveOptions saveOptions = new DocSaveOptions();

            // Specify the output format as DOC
            saveOptions.setFormat(DocSaveOptions.DocFormat.Doc);
            // Set the recognition mode as Flow
            saveOptions.setMode(DocSaveOptions.RecognitionMode.Flow);

            // Set the Horizontal proximity as 2.5
            saveOptions.setRelativeHorizontalProximity(2.5f);

            // Enable the value to recognize bullets during conversion process
            saveOptions.setRecognizeBullets(true);
            pdf.save(doc.getPath(), saveOptions);*/


            //使用spire转
            PdfDocument pdf  = new PdfDocument();
            String path = FileSystemView.getFileSystemView() .getHomeDirectory().getAbsolutePath();
            InputStream in = file.getInputStream();
            pdf.loadFromStream(in);
            File doc = new File(path + "/"+file.getOriginalFilename().split("[.]")[0]+".doc");
            if (!doc.exists()){
                doc.createNewFile();
            }

            pdf.saveToFile(path + "/"+file.getOriginalFilename().split("[.]")[0]+".doc", FileFormat.DOC);


            map.put("message","转换成功");
            map.put("path",file.getOriginalFilename().split("[.]")[0]+".doc");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message","转换失败");
        }

        return map;
    }

    /**
     * 下载文件
     * @param path
     * @param response
     */
    @RequestMapping("/wordChu")
    public void pdfToWordUntil(@RequestParam("path")String path, HttpServletResponse response){
        File file = new File("/root/" +path);//windos系统在桌面C:\Users\epule\Desktop/
        //File file = new File("C:/Users/epule/Desktop/" +path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(buffer);
            file.delete();
            outputStream.flush();
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析excel
     * @param file
     * @param response
     */
    @RequestMapping("/excel")
    public List excelUntil(@RequestParam("file")MultipartFile file, HttpServletResponse response){
        List list = new ArrayList();
        try {
            list = ExcelUtils.excelToShopIdList(file.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 拼接excel字段
     * @param sqlName
     * @param jsonList
     * @return
     */
    @RequestMapping("/pinSqlExcel")
    public List pinSqlExcel(@RequestParam("sqlName")String sqlName,@RequestParam(value = "jsonList")String jsonList){
        List list = new ArrayList();
        List list1 = JSONArray.parseArray(jsonList);
        Map<String,Integer> map = new HashMap();
        for (int i = 1;i < 11;i++){
            if (sqlName.contains("^{"+i+"}")){
                map.put("^{"+i+"}",i);
            }
        }
        Set<String> set = map.keySet();
        for (int i=0;i<list1.size();i++){
            String huan = sqlName;
            for (String ti1:set){
                if (((JSONArray)list1.get(i)).get(map.get(ti1)-1) == null){
                    huan = huan.replace(ti1,"");
                }else{
                    huan = huan.replace(ti1,((JSONArray)list1.get(i)).get(map.get(ti1)-1).toString());
                }
            }
            String[] ttt = new String[1];
            ttt[0] = huan;
            list.add(ttt);
        }
        return list;
    }

    /**
     * 筛选数据
     * @param han 包含还是不包含的
     * @param wei 筛选位置
     * @param lie 第几列
     * @param contract  内容
     * @param jsonList
     * @return
     */
    @RequestMapping("/selectExcel")
    public List selectExcel(@RequestParam("han")String han,@RequestParam("wei")String wei,@RequestParam("lie")String lie,@RequestParam("contract")String contract,@RequestParam(value = "jsonList")String jsonList){
        List listHan = new ArrayList();
        List listNotHan = new ArrayList();
        List list1 = JSONArray.parseArray(jsonList);
        for (int i=0;i<list1.size();i++){
            List cell = (JSONArray)list1.get(i);
            int flag = 0;
            for (int j=0;j<cell.size();j++){
                String cell1 = "";
                if (cell.get(j) != null){
                    cell1 = cell.get(j).toString();
                }
                if (lie == null ||"".equals(lie)){
                    if ("中间".equals(wei)){
                        if (cell1.contains(contract)){
                            flag = 1;
                        }
                    }else if("前缀".equals(wei)){
                        if (cell1.startsWith(contract)){
                            flag = 1;
                        }
                    }else if("后缀".equals(wei)){
                        if (cell1.endsWith(contract)){
                            flag = 1;
                        }
                    }
                }else{
                    if (Integer.parseInt(lie)-1 == j){
                        if ("中间".equals(wei)){
                            if (cell1.contains(contract)){
                                flag = 1;
                            }
                        }else if("前缀".equals(wei)){
                            if (cell1.startsWith(contract)){
                                flag = 1;
                            }
                        }else if("后缀".equals(wei)){
                            if (cell1.endsWith(contract)){
                                flag = 1;
                            }
                        }
                    }
                }
            }
            if (flag == 1){
                listHan.add(list1.get(i));
            }else{
                listNotHan.add(list1.get(i));
            }
        }

        if ("包含".equals(han)){
            return listHan;
        }else{
            return listNotHan;
        }
    }

    /**
     * 导出txt,excel
     * @param jsonList
     */
    @RequestMapping("/exportTxt")
    public Map exportTxt(@RequestParam(value = "jsonList")String jsonList){
        File file = null;
        BufferedWriter bw = null;
        Map map = new HashMap();
        try {
            List list = JSONArray.parseArray(jsonList);
            int len = ((JSONArray)list.stream().max(Comparator.comparingInt(vo->((JSONArray)vo).size())).get()).size();
            if (len > 1){
                file = File.createTempFile("转换Excel","xls");
                HSSFWorkbook wb=new HSSFWorkbook();
                HSSFSheet sheet=wb.createSheet("转换结果");
                //设置默认列宽
                sheet.setDefaultColumnWidth(10);
                for (int i=0;i<list.size();i++){
                    HSSFRow row = sheet.createRow(i);
                    for (int j =0;j<((JSONArray)list.get(i)).size();j++){
                        if (((JSONArray)list.get(i)).get(j) != null){
                            HSSFCell cell = row.createCell(j);
                            cell.setCellValue(((JSONArray)list.get(i)).get(j).toString());
                        }
                    }
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                wb.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }else{
                file = File.createTempFile("转换Excel","txt");
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

                for (int i=0;i<list.size();i++){
                    for (int j =0;j<((JSONArray)list.get(i)).size();j++){
                        if (((JSONArray)list.get(i)).get(j) != null){
                            bw.write(((JSONArray)list.get(i)).get(j).toString());
                        }
                    }
                    bw.newLine();
                }
                bw.close();
            }


            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            map.put("file", Base64Utils.encodeToString(buffer));
            if (len > 1){
                map.put("filename","转换Excel.xls");
            }else{
                map.put("filename","转换Excel.txt");
            }
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 验证aspose
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = PdfToWord.class.getClassLoader().getResourceAsStream("license.xml"); //  license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
