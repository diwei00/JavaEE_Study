package score;

import java.util.Arrays;

class Content {
    public String id;
    public String createDept;
    public String createDeptList;
    public String userDeptIdList;
    public String createBy;
    public String createByName;
    public String createTime;
    public String updateBy;
    public String updateByName;
    public String updateTime;
    public String remark;
    public String pageNum;
    public String pageSize;
    public String deptId;
    public String deptName;
    public String deptCode;
    public String majorId;
    public String majorName;
    public String majorCode ;
    public String classId;
    public String className;
    public String classNum;
    public String order;
    public String sort;
    public String sysUserId;
    public String aliasAndColumnName;
    public String stuCode;
    public String stuNumber;
    public String examNumber;
    public String telephone;
    public String phone;
    public String gender;
    public String name;
    public String idCard;
    public String grade;
    public String nation;
    public String politicsStatus;
    public String arrangement;
    public String degree;
    public String regStatus;
    public String regDate;
    public String feeStatus;
    public String feeDate;
    public String stuStatus;
    public String leader;
    public String roomName;
    public String local;
    public String bdate;
    public String edate;
    public String year;
    public String term;
    public String courseId;
    public String courseCode;
    public String courseName;
    public String score;
    public String isQualified;
    public String examStatus;
    public String schoolRollstatus;
    public String openId;
    public String nameScore;
    public String type;
    public String runningWater;
    public String years;
    public String feeTime;
    public String feeMoney;
    public String financialName;
    public String bbdate;
    public String eedate;
}
public class Score {
    public String msg;
    public String code;
    public Content[] content = new Content[9];
    public String totalElements;

    @Override
    public String toString() {
        return "Score{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", content=" + Arrays.toString(content) +
                ", totalElements='" + totalElements + '\'' +
                '}';
    }
}
