package mimer29or40.productiontimer.common.model;

public enum ConnectionType
{
    SET("set", true),
    NOT_CONTROLLER("not_controller", false),
    NOT_RELAY("not_relay", false),
    LINKED("linked", true),
    FAILED("failed", false),
    RELAY_OVERWRITTEN("relay_overwritten", true),
    SAME_BLOCK("same_block", false),
    NEEDS_ID("needs_id", false),
    ;

    private String  message;
    private boolean isSuccess;

    ConnectionType(String message, boolean isSuccess)
    {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public String getMessage()
    {
        return "productiontimer.connector." + message;
    }

    public boolean isSuccess()
    {
        return isSuccess;
    }

    public boolean isFailure()
    {
        return !isSuccess;
    }
}
