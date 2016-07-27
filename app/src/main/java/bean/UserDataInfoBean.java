package bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 22-07-2016.
 */
public class UserDataInfoBean implements Serializable {

    String RecordNo,CustomerId,AccountNo,CustomerName,AadhaarNo,MobileNo,BranchCode,BranchName,NewAadhaarNo,NewNameAsAadhaar,NewMobileNo;

    public UserDataInfoBean(String recordNo, String customerId, String accountNo, String customerName, String aadhaarNo, String mobileNo, String branchCode, String branchName, String newAadhaarNo, String newNameAsAadhaar, String newMobileNo) {
        RecordNo = recordNo;
        CustomerId = customerId;
        AccountNo = accountNo;
        CustomerName = customerName;
        AadhaarNo = aadhaarNo;
        MobileNo = mobileNo;
        BranchCode = branchCode;
        BranchName = branchName;
        NewAadhaarNo = newAadhaarNo;
        NewNameAsAadhaar = newNameAsAadhaar;
        NewMobileNo = newMobileNo;
    }

    public String getRecordNo() {
        return RecordNo;
    }

    public void setRecordNo(String recordNo) {
        RecordNo = recordNo;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAadhaarNo() {
        return AadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        AadhaarNo = aadhaarNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getNewAadhaarNo() {
        return NewAadhaarNo;
    }

    public void setNewAadhaarNo(String newAadhaarNo) {
        NewAadhaarNo = newAadhaarNo;
    }

    public String getNewNameAsAadhaar() {
        return NewNameAsAadhaar;
    }

    public void setNewNameAsAadhaar(String newNameAsAadhaar) {
        NewNameAsAadhaar = newNameAsAadhaar;
    }

    public String getNewMobileNo() {
        return NewMobileNo;
    }

    public void setNewMobileNo(String newMobileNo) {
        NewMobileNo = newMobileNo;
    }


}
