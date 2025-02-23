package com.loy124.myapp.member.entity;

import com.loy124.myapp.core.util.common.Role;
import com.loy124.myapp.core.util.common.entity.BaseEntity;
import com.loy124.myapp.member.dto.AdminUpdateRequestDto;
import com.loy124.myapp.member.dto.UpdateManagerRequestDto;
import com.loy124.myapp.member.dto.UpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table (name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql =" UPDATE Member SET is_deleted = true, deleted_date = now() where member_id = ?")
@Where(clause = "is_deleted = false")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "employee_number")
	private Integer employeeNumber;

	//소속 코드
	@Column(name = "affiliated_code")
	private String affiliatedCode;

	@Column(name = "password")
	private String password;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "language")
	private String language;

	@Column(name = "goal")
	private String goal;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@Column(name = "is_deleted")
	private final Boolean isDeleted = Boolean.FALSE;

	private LocalDateTime deletedDate;

	public void encodedPassword(BCryptPasswordEncoder bCryptPasswordEncoder){
		this.password = bCryptPasswordEncoder.encode(password);

	}

	public void updateMember(AdminUpdateRequestDto adminUpdateRequestDto){

		this.name = adminUpdateRequestDto.getName();
		this.affiliatedCode = adminUpdateRequestDto.getAffiliatedCode();
		this.employeeNumber = adminUpdateRequestDto.getEmployeeNumber();
		this.phoneNumber = adminUpdateRequestDto.getPhoneNumber();
		this.role = adminUpdateRequestDto.getRole();


	}

	public void updateLastLogin(){
		this.lastLogin = LocalDateTime.now();
	}

	public void updateRoleEmployee(){
		this.role = Role.EMPLOYEE;
	}





	@Builder
	public Member(Long id, String email, String name, Integer employeeNumber, String affiliatedCode, String password, Role role, String language, String goal, String phoneNumber, LocalDateTime lastLogin, Boolean isDeleted, LocalDateTime deletedDate) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.employeeNumber = employeeNumber;
		this.affiliatedCode = affiliatedCode;
		this.password = password;
		this.role = role;
		this.language = language;
		this.goal = goal;
		this.phoneNumber = phoneNumber;

	}

    public void updateMember(UpdateManagerRequestDto updateManagerRequestDto) {
		this.email = updateManagerRequestDto.getEmail();
		this.name = updateManagerRequestDto.getName();
		this.phoneNumber = updateManagerRequestDto.getPhoneNumber();
		this.role = updateManagerRequestDto.getRole();
    }

	public void updateMember(UpdateRequestDto updateRequestDto) {
		this.goal = updateRequestDto.getGoal();
		this.name = updateRequestDto.getName();
		this.affiliatedCode = updateRequestDto.getAffiliatedCode();
		this.employeeNumber = updateRequestDto.getEmployeeNumber();
		this.phoneNumber = updateRequestDto.getPhoneNumber();
	}

	public void updatePassword(String password){
		this.password = password;
	}

}










