package mimer29or40.productiontimer.common.model;

public enum ConnectionType
{
    SET("set", true),
    NOTCONTROLLER("notcontroller", false),
    NOTRELAY("notrelay", false),
    LINKED("linked", true),
    FAILED("failed", false),
    ALREADYLINKED("alreadylinked", false),
    SAMEBLOCK("sameblock", false),
    NEEDSID("needsid", false),
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
