package Activity;

/**
 * Created by Administrator on 25-07-2016.
 */
public class JsonParams {
    String AadhaarNo,NameAsAadhaar,Branchcd,ZoneCode,AccountNo,RecordNo,SourceType,UserCode;

   public JsonParams(String aadhaarNo, String nameAsAadhaar, String branchcd, String zoneCode, String accountNo, String recordNo, String sourceType, String userCode) {
        this.AadhaarNo = aadhaarNo;
        this.NameAsAadhaar = nameAsAadhaar;
        this.Branchcd = branchcd;
        this.ZoneCode = zoneCode;
        this.AccountNo = accountNo;
        this.RecordNo = recordNo;
        this.SourceType = sourceType;
        this.UserCode = userCode;
    }

    public String getAadhaarNo() {
        return AadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        AadhaarNo = aadhaarNo;
    }

    public String getNameAsAadhaar() {
        return NameAsAadhaar;
    }

    public void setNameAsAadhaar(String nameAsAadhaar) {
        NameAsAadhaar = nameAsAadhaar;
    }

    public String getBranchcd() {
        return Branchcd;
    }

    public void setBranchcd(String branchcd) {
        Branchcd = branchcd;
    }

    public String getZoneCode() {
        return ZoneCode;
    }

    public void setZoneCode(String zoneCode) {
        ZoneCode = zoneCode;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getRecordNo() {
        return RecordNo;
    }

    public void setRecordNo(String recordNo) {
        RecordNo = recordNo;
    }

    public String getSourceType() {
        return SourceType;
    }

    public void setSourceType(String sourceType) {
        SourceType = sourceType;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }
}
