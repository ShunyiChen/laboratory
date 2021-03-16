package com.shunyi.laboratory.xml.simplexml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * @author esehhuc
 * @create 2021-03-16 14:19
 */
@Root
public class ExampleForSimpleXML {

    @Element
    private String text;

    @Attribute
    private int index;

    public ExampleForSimpleXML() {
        super();
    }

    public ExampleForSimpleXML(String text, int index) {
        this.text = text;
        this.index = index;
    }

    public String getMessage() {
        return text;
    }

    public int getId() {
        return index;
    }


    public static void main(String[] args) {
        //生成xml文件
//        Serializer serializer = new Persister();
//        Example example = new Example("Example message", 123);
//        File result = new File("example.xml");
//
//        try {
//            serializer.write(example, result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //解析xml
        Serializer serializer = new Persister();
        File source = new File("example.xml");

        try {
            ExampleForSimpleXML example = serializer.read(ExampleForSimpleXML.class, source);
            System.out.println(example.getId()+","+example.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
