package com.shunyi.laboratory.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @author esehhuc
 * @create 2021-03-15 13:45
 */
@Data
@AllArgsConstructor
public class CallbackInVO {
    private File tmpDirectory;
    private List<String> errorList;
}
