package edu.pjwstk.demo.solver;

public class ExecutionResult {

    public final ExecutionStatus status;
    public final String message;

    public ExecutionResult(boolean isSuccess, String summary) {
        this.message = summary;
        this.status = isSuccess ? ExecutionStatus.SUCCESS : ExecutionStatus.FAILURE;
    }
}
