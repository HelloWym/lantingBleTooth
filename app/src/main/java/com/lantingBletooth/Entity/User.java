package com.lantingBletooth.Entity;
import java.util.Date;

/**
 * 账户信息
 * @author yjg
 *
 */
public class User extends IdEntity{

	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 用户编号-唯一
	 */
	private String usercode;
	
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 上次登录IP
	 */
	private String lastIP;
	/**
	 * 上传登录时间
	 */
	private Date lastDate;	
	
	/**
	 * 一共登录次数
	 */
	private int loginCount;
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 部门ID
	 */
	private long departmentId;
	/**
	 * 部门名称
	 */
	private String department;
	/**
	 * 单位类型
	 */
	private String departmentType;
	//所属公司记录id
	private long companyId;
	//所属公司名称
	private String companyName; 
	/**
	 * 排序权重
	 */
	private long orderNo;
	
	//不同角色间用英文逗号分隔
	private String roleIds;
	/**
	 * 角色名称
	 */
	private String rolenames;
	
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 注册日期
	 */
	private Date regDate;
	
	/**
	 * 用户状态
	 */
	private int status;
	/**
	 * 用户状态文字信息
	 */
	private String statusInfo;
	/**
	 * 家庭成员用户id
	 */
	private String familyIds;
	/**
	 * 家庭成员用户名字
	 */
	private String familyNames;
	/**
	 * 用户头像存放路径
	 */
	private String picUrl;
	//2018.1.23
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 配偶状态 0无1阴性配偶2双阳性配偶
	 */
	private int mateStatus;
	/**
	 * 配偶状态文字信息
	 */
	private String mateStatusInfo;
	/**
	 * 感染途径
	 */
	private String infectWay;
	/**
	 * 是否同性感染
	 */
	private Boolean isTheSame;
	/**
	 * 所属区域
	 */
	private String area;
	/**
	 * 获取 用户编号-唯一
	 * @return usercode用户编号-唯一
	 */
	public String getUsercode() {
		return usercode;
	}
	/**
	 * 设置 用户编号-唯一
	 */
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	/**
	 * 获取 用户名
	 * @return username用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置 用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取 密码
	 * @return password密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取 上次登录IP
	 * @return lastIP上次登录IP
	 */
	public String getLastIP() {
		return lastIP;
	}
	/**
	 * 设置 上次登录IP
	 */
	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}
	/**
	 * 获取 上传登录时间
	 * @return lastDate上传登录时间
	 */
	public Date getLastDate() {
		return lastDate;
	}
	/**
	 * 设置 上传登录时间
	 */
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	/**
	 * 获取 一共登录次数
	 * @return loginCount一共登录次数
	 */
	public int getLoginCount() {
		return loginCount;
	}
	/**
	 * 设置 一共登录次数
	 */
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
	/**
	 * 获取 手机号码
	 * @return phone手机号码
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置 手机号码
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取 注册日期
	 * @return regDate注册日期
	 */
	public Date getRegDate() {
		return regDate;
	}
	/**
	 * 设置 注册日期
	 */
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	/**
	 * 获取 邮箱
	 * @return email邮箱
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 设置 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取 部门ID
	 * @return departmentId部门ID
	 */
	public long getDepartmentId() {
		return departmentId;
	}
	/**
	 * 设置 部门ID
	 */
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * 获取 部门名称
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置 部门名称
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
		
	/**
	 * 获取  所属公司记录id
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}
	
	/**
	 * 设置  所属公司记录id
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
	/**
	 * 获取  所属公司名称
	 * @return companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * 设置  所属公司名称
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * 获取  不同角色间用英文逗号分隔
	 * @return roleIds
	 */
	public String getRoleIds() {
		return roleIds;
	}
	
	/**
	 * 设置  不同角色间用英文逗号分隔
	 * @param roleIds
	 */
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	/**
	 * 获取  bare_field_comment
	 * @return rolenames
	 */
	public String getRolenames() {
		return rolenames;
	}
	
	/**
	 * 设置  bare_field_comment
	 * @param rolenames
	 */
	public void setRolenames(String rolenames) {
		this.rolenames = rolenames;
	}
	public String getDepartmentType() {
		return departmentType;
	}
	public void setDepartmentType(String departmentType) {
		this.departmentType = departmentType;
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getStatusInfo() {
		return statusInfo;
	}
	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
	public String getFamilyIds() {
		return familyIds;
	}
	public void setFamilyIds(String familyIds) {
		this.familyIds = familyIds;
	}
	
	public String getFamilyNames() {
		return familyNames;
	}
	public void setFamilyNames(String familyNames) {
		this.familyNames = familyNames;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getMateStatus() {
		return mateStatus;
	}
	public void setMateStatus(int mateStatus) {
		this.mateStatus = mateStatus;
	}
	public String getMateStatusInfo() {
		return mateStatusInfo;
	}
	public void setMateStatusInfo(String mateStatusInfo) {
		this.mateStatusInfo = mateStatusInfo;
	}
	public String getInfectWay() {
		return infectWay;
	}
	public void setInfectWay(String infectWay) {
		this.infectWay = infectWay;
	}
	public Boolean getIsTheSame() {
		return isTheSame;
	}
	public void setIsTheSame(Boolean isTheSame) {
		this.isTheSame = isTheSame;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
}
