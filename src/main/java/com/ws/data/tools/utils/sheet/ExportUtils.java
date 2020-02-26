package com.ws.data.tools.utils.sheet;

import com.ws.data.tools.annotations.FieldQualifier;
import com.ws.data.tools.exceptions.DTException;
import com.ws.data.tools.utils.ClassUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author willis
 * @chapter 文件导出工具类
 * @section
 * @since 2018年10月21日 22:26
 */
@Deprecated
public class ExportUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);

    public static <T> File exportExcel(List<T> datas, String filePath) {
        try {
            return exportExcel(datas, filePath, 65535);
        } catch (Exception e) {
            throw new DTException(400, e.getLocalizedMessage());
        }
    }
    /**
     * @param datas    要导出的数据
     * @param filePath 导出的文件路径,绝对路径，包含文件名
     * @param maxRowCount 一个sheet最多展示多少行
     * @return
     * @throws Exception
     */
    public static <T> File exportExcel(List<T> datas, String filePath, Integer maxRowCount) throws Exception{
        if (datas == null || datas.isEmpty()) {
            logger.error("无需要导出的数据.");
            throw new DTException(-2, "无需要导出的数据");
        }
        if (filePath == null) {
            logger.error("请给出文件导出位置");
            throw new DTException(-1, "请给出文件导出位置");
        }
        Class clazz = datas.get(0).getClass();
        List<Field> fieldsTmp = ClassUtil.getAllField(clazz);
        List<Field> fields = new ArrayList<Field>();
        for (Field field : fieldsTmp) {
            FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
            if (annotation == null) {
                fields.add(field);
            } else {
                if (!annotation.exclude()) {
                    fields.add(field);
                }
            }
        }
        sort(fields);

        int columnCounts = fields.size();


        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            int dataSize = datas.size();
            Integer sheetCount = dataSize / maxRowCount;

            //表头样式
            CellStyle cellStyleTitle = wb.createCellStyle();
            Font fontTitle = wb.createFont();
            fontTitle.setFontName("微软雅黑");
            fontTitle.setColor(IndexedColors.WHITE.getIndex());
            fontTitle.setBold(true);
            cellStyleTitle.setFont(fontTitle);
            cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyleTitle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

            //内容样式
            CellStyle cellStyleData = wb.createCellStyle();
            Font fontData = wb.createFont();
            fontData.setFontName("微软雅黑");
            cellStyleData = wb.createCellStyle();
            cellStyleData.setFont(fontData);

            for (int i = 0; i <= sheetCount; i++) {
                //创建一个sheet
                HSSFSheet s = wb.createSheet();

                //写表头
                HSSFRow r = s.createRow(0);
                r.setHeight((short) 256);
                for (int i_ = 0; i_ < columnCounts; i_++) {
                    HSSFCell c = r.createCell(i_);
                    c.setCellStyle(cellStyleTitle);
                    Field field = fields.get(i_);
                    FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                    if (annotation == null) {
                        c.setCellValue(field.getName());
                    } else {
                        String alias = annotation.alias();
                        c.setCellValue(alias);
                    }
                    //字段别名
                    s.setColumnWidth(i_, 25 * 256);
                }
                //写数据
                for (int j = maxRowCount * i + 1; j <= maxRowCount * (i + 1) && j <= dataSize; j++) {
                    T data = datas.get(j - 1);
                    //新建一行
                    HSSFRow dataRow = s.createRow(j - maxRowCount * i);
                    //统一字体

                    for (int j_ = 0; j_ < columnCounts; j_++) {//列
                        //新建一格
                        HSSFCell c = dataRow.createCell(j_);
                        c.setCellStyle(cellStyleData);
//                        FieldEntry fieldEntry = fields.get(j_);
                        Field field = fields.get(j_);
                        FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                        Class z = data.getClass();
                        Class fz = z.getSuperclass();

                        Field f = null;
                        try {
                            f = z.getDeclaredField(field.getName());
                        } catch (NoSuchFieldException e) {
                            f = fz.getDeclaredField(field.getName());
                        }
                        f.setAccessible(true);
                        //取值
                        Object cellValue = f.get(data);
                        if (cellValue instanceof Date) {
                            String dateFmt = annotation.dateFmt();
                            SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
                            cellValue = sdf.format((Date) cellValue);
                        }
                        c.setCellValue(cellValue == null ? "" : cellValue.toString());
                    }
                }
            }

            File desFile = new File(filePath);
            logger.info("导出文件路径：{}", desFile.getPath());
            if (!desFile.exists()) {
                desFile.createNewFile();
            }
            wb.write(new FileOutputStream(desFile));
            return desFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            wb.close();
        }
        return null;
    }


    /**
     * 导出文件
     * @param datas
     * @param filePath
     * @param append 是否追加
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> File exportExcel(List<T> datas, String filePath, boolean append) throws Exception {
        if (datas == null || datas.isEmpty()) {
            throw new DTException(-2, "无需要导出的数据");
        }
        if (filePath == null) {
            throw new DTException(-1, "请给出文件导出位置");
        }
        Class clazz = datas.get(0).getClass();
        List<Field> fieldsTmp = getAllField(clazz);
        List<Field> fields = new ArrayList<Field>();
        for (Field field : fieldsTmp) {
            FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
            if (annotation == null) {
                fields.add(field);
            } else {
                if (!annotation.exclude()) {
                    fields.add(field);
                }
            }
        }
        // 排序字段
        sort(fields);
        int columnCounts = fields.size();
        HSSFWorkbook wb = null;
        // 是否在文档后面追加
        if (append) {
            // 拿到已经存在的文件,如果不存在就创建一个
            FileWrapper fileWrapper = getOrCreateFile(filePath);
            if (fileWrapper.getIsNew()) {
                wb = new HSSFWorkbook();
            } else {
                POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(fileWrapper.getFile()));
                wb = new HSSFWorkbook(poifsFileSystem);
            }
        } else {
            // 创建一个新的表格
            wb = new HSSFWorkbook();
        }

        try {
            CellStyle cellStyleData = cellStyleData(wb);
            CellStyle cellStyleTitle = cellStyleTitle(wb);
            // sheet 页数量
            int numberOfSheets = wb.getNumberOfSheets();
            HSSFSheet s = null;
            if (numberOfSheets == 0) {
                s = wb.createSheet();
            } else {
                // 取得当前sheet页
                s= wb.getSheetAt(numberOfSheets - 1);
            }
            // 最后一行
            int rowNum = s.getLastRowNum();

            for (T data : datas) {
                if (rowNum >= 65535) {
                    s = wb.createSheet();
                    rowNum = 0;
                }
                //第一行用于写表头
                if (rowNum == 0) {
                    HSSFRow r = s.createRow(0);
                    r.setHeight((short) 256);
                    for (int i_ = 0; i_ < columnCounts; i_++) {
                        HSSFCell c = r.createCell(i_);
                        c.setCellStyle(cellStyleTitle);
                        Field field = fields.get(i_);
                        FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                        if (annotation == null) {
                            c.setCellValue(field.getName());
                        } else {
                            String alias = annotation.alias();
                            c.setCellValue(alias);
                        }
                        //字段别名
                        s.setColumnWidth(i_, 25 * 256);
                    }
                }
                //新建一行
                HSSFRow dataRow = s.createRow(++ rowNum);
                //统一字体
                for (int j_ = 0; j_ < columnCounts; j_++) {//列
                    //新建一格
                    HSSFCell c = dataRow.createCell(j_);
                    c.setCellStyle(cellStyleData);
//                        FieldEntry fieldEntry = fields.get(j_);
                    Field field = fields.get(j_);
                    FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                    Class z = data.getClass();
                    Class fz = z.getSuperclass();

                    Field f = null;
                    try {
                        f = z.getDeclaredField(field.getName());
                    } catch (NoSuchFieldException e) {
                        f = fz.getDeclaredField(field.getName());
                    }
                    f.setAccessible(true);
                    //取值
                    Object cellValue = f.get(data);
                    if (cellValue instanceof Date) {
                        String dateFmt = annotation.dateFmt();
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
                        cellValue = sdf.format((Date) cellValue);
                    }
                    c.setCellValue(cellValue == null ? "" : cellValue.toString());
                }
            }

            File desFile = new File(filePath);
            File parentFile = desFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            if (!desFile.exists()) {
                desFile.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(desFile);
            wb.write(fileOutputStream);
            fileOutputStream.close();
            return desFile;
        } catch (Exception e) {
        } finally {
            wb.close();
        }
        return null;


    }

    /**
     * 所有字段
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<Field> getAllField(final Class<T> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<Field> fields = new ArrayList<>();
        Class<?> crtClazz = clazz;
        while(crtClazz != null) {
            Field[] fieldsTmp = crtClazz.getDeclaredFields();
            if (fieldsTmp != null && fieldsTmp.length > 0) {
                for (Field field : fieldsTmp) {
                    fields.add(field);
                }
            }
            crtClazz = crtClazz.getSuperclass();
        }
        return fields;
    }
    /**
     * 字段排序
     * @param fields
     */
    private static void sort(List<Field> fields) {
        if (fields == null || fields.isEmpty()) {
            return ;
        }
        Collections.sort(fields, (f1, f2) -> {
            FieldQualifier annotation1 = f1.getAnnotation(FieldQualifier.class);
            FieldQualifier annotation2 = f2.getAnnotation(FieldQualifier.class);
            int seq1 = 0;
            int seq2 = 0;
            if (annotation1 != null) {
                seq1 = annotation1.sequence();
            }
            if (annotation2 != null) {
                seq2 = annotation2.sequence();
            }
            return seq1 - seq2;
        });
    }

    private static CellStyle cellStyleData(HSSFWorkbook wb) {
        if (wb != null) {
            //内容样式
            CellStyle cellStyleData = wb.createCellStyle();
            Font fontData = wb.createFont();
            fontData.setFontName("微软雅黑");
            cellStyleData = wb.createCellStyle();
            cellStyleData.setFont(fontData);
            return cellStyleData;
        }
        return null;
    }

    private static FileWrapper getOrCreateFile(String filePath) {
        FileWrapper result = new FileWrapper();
        File desFile = new File(filePath);
        File parentFile = desFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                result.setIsNew(true);
            }
        } catch (Exception e) {
        }
        result.setFile(desFile);
        return result;
    }

    private static class FileWrapper{
        private File file;
        private boolean isNew;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public boolean getIsNew() {
            return isNew;
        }

        public void setIsNew(boolean isNew) {
            this.isNew = isNew;
        }
    }
    private static CellStyle cellStyleTitle(HSSFWorkbook wb) {
        if (wb != null) {
            //表头样式
            CellStyle cellStyleTitle = wb.createCellStyle();
            Font fontTitle = wb.createFont();
            fontTitle.setFontName("微软雅黑");
            fontTitle.setColor(IndexedColors.WHITE.getIndex());
            fontTitle.setBold(true);
            cellStyleTitle.setFont(fontTitle);
            cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyleTitle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            return cellStyleTitle;
        }
        return null;
    }

}
