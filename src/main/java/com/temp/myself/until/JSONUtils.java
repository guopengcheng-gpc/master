package com.temp.myself.until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.temp.myself.temp.entiy.Person;
import com.temp.myself.temp.entiy.Picture;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 测试
 */
public class JSONUtils {
    public static <T> T getObjectByJson(String str,Class<?> T) throws Exception{
        Field[] fields = T.getDeclaredFields();
        T C = (T)T.newInstance();
        Map<String,String> map = new HashMap();
        try {
            str = str.substring(1,str.length()-1);
            String[] strs = str.split(",");
            for (String className :strs){
                String[] cl = className.split(":");
                map.put(cl[0].replaceAll("\"","").trim(),cl[1].replaceAll("\"","").trim());
            }
        } catch (Exception e) {
            System.out.println("json字符串错误");
        }
        for (Field field:fields){
            String name = field.getName();
            String typeName = field.getGenericType().getTypeName();
            Method setMethod;
            if ("java.lang.String".equals(typeName)){
                setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1),String.class);
                setMethod.invoke(C,map.get(name));
            }else if ("java.lang.Double".equals(typeName) || "double".equals(typeName)){
                setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1),Double.class);
                setMethod.invoke(C,Double.parseDouble(map.get(name)));
            }else if ("java.util.Date".equals(typeName)){
                setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1), Date.class);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                setMethod.invoke(C,simpleDateFormat.parse(map.get(name)));
            }else if ("java.lang.Integer".equals(typeName) || "int".equals(typeName)){
                setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1), Integer.class);
                setMethod.invoke(C,Integer.parseInt(map.get(name)));
            }else if ("java.lang.Boolean".equals(typeName) || "boolean".equals(typeName)){
                setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1), Boolean.class);
                if ("true".equals(map.get(name))){
                    setMethod.invoke(C,true);
                }else if ("false".equals(map.get(name))){
                    setMethod.invoke(C,false);
                }
            }
        }
        return C;
    }

    public static<T> List<T> getListByJson(String str, Class<?> T) throws Exception{
        List<T> list = new ArrayList();
        String[] strs = new String[0];
        try {
            str = str.substring(2,str.length()-2);
            strs = str.split("\\},\\{");
        } catch (Exception e) {
            System.out.println("json解析异常");
        }
        for (String obj: strs){
            list.add(JSONUtils.getObjectByJson(obj,T));
        }
        return list;
    }

    /**
     * 类拷贝
     * @param T 结果类型
     * @param o 初始对象
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getCopyObject(Class<?> T,Object o) throws Exception{
        T s = (T)T.newInstance();
        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        Map<String,String> map = new HashMap<>();
        for (Field field:fields){
            String name = field.getName();
            String typeName = field.getGenericType().getTypeName();
            map.put(name,typeName);
        }

        Field[] fields1 = T.getDeclaredFields();
        for (Field field:fields1){
            String name = field.getName();
            String typeName = field.getGenericType().getTypeName();
            if (typeName.equals(map.get(name))){//必须使用共有方法或属性
                Method setMethod = T.getMethod("set"+name.substring(0,1).toUpperCase()+name.substring(1),Class.forName(typeName));
                Method setMethod1 = c.getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1));
                setMethod.invoke(s,setMethod1.invoke(o));
                /*field.setAccessible(true);
                Object val = field.get(o);
                field.set(s,val);*/
            }
        }
        return s;
    }

    public static void main(String[] args){
        try {
            /*Picture picture = JSONUtils.getObjectByJson("{\"name\":\"测试\",\"path\":路径,\"scale\":23}", Picture.class);
            System.out.println(picture.getName()+picture.getScale()+picture.getPath());

            List<Picture> list = JSONUtils.getListByJson("[{\"name\":\"测试\",\"path\":路径,\"scale\":23},{\"name\":\"测试1\",\"path\":路径1,\"scale\":231},{\"name\":\"测试\",\"path\":路径3,\"scale\":23}]", Picture.class);
            System.out.println(list.get(0).getName()+list.get(1).getName()+list.get(2).getName());

            Picture picture1 = new Picture();
            picture1.setName("拷贝");
            picture1.setPath("ceq1");
            Person person = JSONUtils.getCopyObject(Person.class,picture1);
            System.out.println(person.getAddress()+"-"+person.getName()+"-"+person.getPhone());*/

            File file = new File("D://Picture/测测测1.png");
            File file1 = new File("D://Picture/测.txt");
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(file1);
            byte[] bytes = new byte[1024];
            int i = 0;
            List<Byte> list = new ArrayList<>();
            while ((i = inputStream.read(bytes)) !=-1){
                for (int m=0;m<i;m++){
                    list.add(bytes[m]);
                }
            }
            List<Byte> list1 = getYaBytes(list);
            byte[] bytes1 = new byte[list1.size()];
            for (int j=0;j<list1.size();j++){
                bytes1[j] = list1.get(j);
            }
            outputStream.write(bytes1);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * byte转二进制String
     * @param b 需要转换成String的字节
     * @return String值
     */
    public static String byteArrayToInt(byte b) {
        if (b>0){
            return "0"+Integer.toBinaryString(b);
        }else{
            return "1"+Integer.toBinaryString(-b);
        }
    }

    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * 将8位二进制字符串转为十进制数  带符号为
     * @param s
     * @return
     */
    public static int StringToInt(String s) {
        if (s.length() ==8){
            if ("1".equals(s.substring(0,1))){
                return -Integer.parseInt(s.substring(1),2);
            }else{
                return Integer.parseInt(s.substring(1),2);
            }
        }else{
            return Integer.parseInt(s,2);
        }
    }

    /**
     * 压缩一
     * @param stringByte
     * @return
     */
    public static StringBuilder getYaStringBuilder(StringBuilder stringByte){
        StringBuilder stringYa = new StringBuilder();
        int s =1;
        int index = 2;
        for (int j=0;j<stringByte.length();j++){
            if (j ==0){
                s = Integer.parseInt(stringByte.substring(0,1));
                index = 2;
            }else{
                if (Integer.parseInt(stringByte.substring(j,j+1)) == s){
                    index = index+1;
                    if (j == stringByte.length()-1){
                        stringYa.append(""+index+s);
                    }
                }else{
                    stringYa.append(""+index+s);
                    s = s == 1 ? 0:1;
                    index = 2;
                    if (j == stringByte.length()-1){
                        stringYa.append(""+index+s);
                    }
                }
            }
        }
        return stringYa;
    }

    /**
     * 压缩二
     * @param stringByte
     * @return
     */
    public static StringBuilder getYaStringBuilder1(StringBuilder stringByte){
        StringBuilder stringYa = new StringBuilder();
        String s ="A";
        int index = 0;
        for (int j=0;j<stringByte.length();j=j+2){
            if (j ==0){
                s = getCharToString(stringByte.substring(0,2));
                index = 1;
            }else{
                if (s.equals(getCharToString(stringByte.substring(j,j+2)))){
                    index = index+1;
                    if (j == stringByte.length()-2){
                        stringYa.append(index+s);
                    }
                }else{
                    stringYa.append(index+s);
                    s = getCharToString(stringByte.substring(j,j+2));
                    index = 1;
                    if (j == stringByte.length()-2){
                        stringYa.append(index+s);
                    }
                }
            }
        }
        return stringYa;
    }
    private static String getCharToString(String s){
        if ("11".equals(s)){
            return "A";
        }else  if("10".equals(s)){
            return "B";
        }else if("01".equals(s)){
            return "C";
        }else{
            return "D";
        }
    }

    private static byte[] getbyteToString(String s){
        byte[] bytes = new byte[s.length()/2];
        String bs;
        for (int i=0;i<s.length()/2;i= i+2){
            bs = s.substring(i,i+2);
            bytes[i] = (byte)Integer.parseInt(bs,16);
        }
        return bytes;
    }


    /**
     * 压缩三
     * @param bytes
     * @return
     */
    public static List<Byte> getYaBytes(List<Byte> bytes){//最多1G的文件字节
        List<Byte> list1 = new ArrayList<>();
        Map<Byte,Integer> map = new HashMap();
        for (Byte b:bytes){
            map.put(b,(map.get(b)==null?0:map.get(b))+1);
        }
        Set<Byte> s = map.keySet();
        Byte byt = new Byte((byte)0);
        int index = map.get(byt);
        Byte by = 0;//出现频率最小的字节作为标志位
        for (Byte b:s){
            if (map.get(b)<index){
                by = b;
            }
        }
        list1.add(by);

        int num = 0;
        Byte byLin = 0;
        for (int i=0;i<bytes.size();i++){
            if (i==0){
                byLin = bytes.get(0);
                num = num+1;
            }else{
                if (bytes.get(i).equals(byLin)) {//当前byte是否和上一个相等
                    num = num+1;
                }else {
                    if (by.equals(byLin)) {//上一位是不是标志位
                        list1.add(by);
                        list1.add((byte) num);
                        list1.add(byLin);
                    } else {
                        if (num > 3) {//不是标志位数量大于三
                            list1.add(by);
                            list1.add((byte) num);
                            list1.add(byLin);
                        } else {
                            for (int j = 0; j < num; j++) {
                                list1.add(byLin);
                            }
                        }

                    }
                }
                if (i==bytes.size()-1){
                    if (bytes.get(i).equals(byLin)) {//上一字节和当前字节是否相等
                        if(bytes.get(i).equals(by)){
                            list1.add(by);
                            list1.add((byte) num);
                            list1.add(byLin);
                        }else{
                            if (num > 3) {//不是标志位数量大于三
                                list1.add(by);
                                list1.add((byte) num);
                                list1.add(bytes.get(i));
                            } else {
                                for (int j = 0; j < num; j++) {
                                    list1.add(bytes.get(i));
                                }
                            }
                        }
                    }else{
                        if (bytes.get(i).equals(by)) {
                            list1.add(by);
                            list1.add((byte) 1);
                            list1.add(by);
                        }else{
                            list1.add(bytes.get(i));
                        }
                    }
                }
            }


        }
        return list1;
    }

}
