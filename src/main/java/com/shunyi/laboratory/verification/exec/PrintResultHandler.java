package com.shunyi.laboratory.verification.exec;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;

/**
 * @author esehhuc
 * @create 2021-03-02 15:03
 */
public class PrintResultHandler extends DefaultExecuteResultHandler {

    private ExecuteWatchdog watchdog;

    public PrintResultHandler(final ExecuteWatchdog watchdog)
    {
        this.watchdog = watchdog;
    }

    public PrintResultHandler(final int exitValue) {
        super.onProcessComplete(exitValue);
    }

    @Override
    public void onProcessComplete(final int exitValue) {
        super.onProcessComplete(exitValue);
        System.out.println("Success("+exitValue+")");
        System.out.println("[resultHandler] The document was successfully printed ...");
    }

    @Override
    public void onProcessFailed(final ExecuteException e) {
        super.onProcessFailed(e);
        System.out.println("Failed("+e.getExitValue()+")");
        if (watchdog != null && watchdog.killedProcess()) {
            System.err.println("[resultHandler] The print process timed out");
        }
        else {
            System.err.println("[resultHandler] The print process failed to do : " + e.getMessage());
        }
    }
}
