package com.shunyi.laboratory;

import com.shunyi.laboratory.schema.NfValidator;

public class LabApp {

    public static void main(String[] args) {
        NfValidator nfValidator = new NfValidator(Integer.parseInt(args[1]), Long.parseLong(args[2]));
        nfValidator.validateSchema(args[0]);
    }


//    private void validateNode(String nodeId) {
//        NodeValidator nodeValidator = new NodeValidator();
//        String cdPath = "/home/omts/workspace/equxyid/omts-script/engine";
//        try {
//            nodeValidator.validate(nodeId, cdPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("****************************\\");
//    }
}
