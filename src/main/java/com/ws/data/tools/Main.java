package com.ws.data.tools;

import com.ws.data.tools.annotations.FieldQualifier;
import com.ws.data.tools.img.CircleCutter;
import com.ws.data.tools.img.poster.DefaultPosterDrawer;
import com.ws.data.tools.utils.sheet.ExportUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author willis
 * @chapter
 * @section
 * @since 2018年10月21日 22:42
 */
public class Main {
    public static void main(String[] args) {
//        testExport();
        try {
            File srcFile = new File("E:\\image2020-1-17_17-53-21.png");
            BufferedImage portraitImage = ImageIO.read(new File("E:\\DSC_0183.JPG"));
            BufferedImage srImg = ImageIO.read(new File("E:\\sr.jpg"));

            DefaultPosterDrawer drawer = new DefaultPosterDrawer(srcFile, "E:\\result.png");
            Color c = new Color(100, 100, 100);
            Font f = new Font("微软雅黑", Font.PLAIN, 25);

            int hSpace = 50;
            int x = 120;
            int y = 450;
            drawer
                .writeText("亲爱的刘强，你在1月份", x, y += hSpace, c, f)
                .writeText("报备-200人", x, y += hSpace, c, f)
                .writeText("带看-15人", x, y += hSpace, c, f)
                .writeText("成交-1人", x, y += hSpace, c, f)
                // 头像
                .drawCircleImage(portraitImage, 205, 120, 86)
                .dramImage(srImg, 79, 800, 131, 131)
                .dramImage(srImg, 172, 268, 200, 40)
                .writeText("武汉加油", 172, 323, Color.RED, f)
                .make();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testExport() {
        List<Student> list = new ArrayList();
        for (int i = 0; i < 5; i ++) {
            Student student = new Student();
            student.setStudentNo(i + "");
            student.setStudentName("学生" + i);
            student.setStudentScore("" + (60 + i));
            student.setIdNo("12345678901234567" + i);
            list.add(student);
        }
        File file = ExportUtils.exportExcel(list, "E:\\export.xls");
    }
    private static class Student{
        @FieldQualifier(sequence = 1, alias = "学号")
        private String studentNo;
        @FieldQualifier(sequence = 2, alias = "姓名")
        private String studentName;
        @FieldQualifier(sequence = 3, alias = "分数")
        private String studentScore;
        @FieldQualifier(sequence = 4, alias = "身份证号", exclude = true)
        private String idNo;

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getStudentScore() {
            return studentScore;
        }

        public void setStudentScore(String studentScore) {
            this.studentScore = studentScore;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }
    }
}
