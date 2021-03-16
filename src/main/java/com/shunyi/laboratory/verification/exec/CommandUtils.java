package com.shunyi.laboratory.verification.exec;

/**
 * @author esehhuc
 * @create 2021-03-10 9:34
 */
public class CommandUtils {

    public static String COMMAND = "yang2dsdl -b ericsson -x -j -v %s ericsson-*.yang";

    public static String COMMAND_FOR_NODE = "python node_verification.py %s";
}
