package com.taotao.test.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestFreeMarker {
    @Test
    public void testFreeMarker() throws IOException, TemplateException {
        //创建一个模板文件;
        //创建模板文件的Configuration对象,并指定模板文件名
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板文件的路径名
        configuration.setDirectoryForTemplateLoading(new File("E:/MyStudyFile/java/TaoTaoMall/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
        //设置模板文件的默认字符集
        configuration.setDefaultEncoding("utf-8");
        //生成模板,指定模板文件名
        Template template = configuration.getTemplate("student.ftl");
        //创建数据集，可以是pojo，也可以是map，推荐使用map
        Map<String,Object> data = new HashMap<>();
        //添加数据
        data.put("hello", "hello freemark");
        //创建Student对象
        Student student = new Student(1,"张三",18,"广州天河");
        data.put("student",student);
        //创建list，放入list
        List<Student> stuList = new ArrayList<>();
        stuList.add(new Student(2,"李四",19,"北京"));
        stuList.add(new Student(3,"李四2",20,"北京"));
        stuList.add(new Student(4,"李四3",21,"北京"));
        stuList.add(new Student(5,"李四4",22,"北京"));
        stuList.add(new Student(6,"李四5",23,"北京"));
        stuList.add(new Student(7,"李四6",24,"北京"));
        stuList.add(new Student(8,"李四7",25,"北京"));
        data.put("stuList", stuList);
        //添加日期
        data.put("date", new Date());
        data.put("val", "hello");
        //创建Writer流对象,指定输出文件的路径和文件名
        Writer writer = new FileWriter("C:/Users/Administrator/Desktop/student.html");
        //输出到目标文件
        template.process(data, writer);
        //关闭流资源
        writer.close();
    }
}
