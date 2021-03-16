package com.shunyi.laboratory.xml.sax;

import java.util.List;

/**
 * @author esehhuc
 * @create 2021-03-16 14:53
 */
public class JavaReadXmlSaxEx {

    public static void main(String[] args) {
        MyRunner runner = new MyRunner();

        List<User> lines = runner.parseUsers();
        lines.forEach(System.out::println);
    }
}
