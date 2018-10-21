package com.ws.data.tools;

import com.ws.data.tools.annotations.FieldQualifier;
import com.ws.data.tools.utils.sheet.ExportUtils;

import java.io.File;
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
