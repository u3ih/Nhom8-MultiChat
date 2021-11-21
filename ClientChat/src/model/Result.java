package model;

public class Result 
{
    public String mActionType;
    public String mResultCode;
    public String mContent;
    public DataFile file;

    public Result(String mActionType, String mResultCode, String mContent) {
        this.mActionType = mActionType;
        this.mResultCode = mResultCode;
        this.mContent = mContent;
        this.file = null;
    }
    public Result(String mActionType, String mResultCode, String mContent, DataFile file) {
        this.mActionType = mActionType;
        this.mResultCode = mResultCode;
        this.mContent = mContent;
        this.file = file;
    }
}
