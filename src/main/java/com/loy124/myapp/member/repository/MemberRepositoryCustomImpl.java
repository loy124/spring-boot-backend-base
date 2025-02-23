package com.loy124.myapp.member.repository;

import com.loy124.myapp.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import com.loy124.myapp.core.util.common.Role;

import com.loy124.myapp.member.dto.AdminGetMemberTotalCountRequestDto;
import com.loy124.myapp.member.dto.AdminManagerPageRequestDto;
import com.loy124.myapp.member.dto.AdminMemberListRequestDto;
import com.loy124.myapp.member.entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Member>> adminMemberList(AdminMemberListRequestDto adminMemberListRequestDto) {

        //BooleanExpression <- 함수로 분리 주로 해당 방식을 쓸 예정이다.

        List<Member> memberList = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(
                        emailStartWith(adminMemberListRequestDto.getEmail()),
                        nameStartWith(adminMemberListRequestDto.getName()),
                        employeeStartWith(adminMemberListRequestDto.getEmployeeNumber()),
                        roleEq(adminMemberListRequestDto.getRole())
                )
                .fetch();


        return Optional.ofNullable(memberList);
    }

    @Override
    public Page<Member> adminMemberListPage(AdminMemberListRequestDto adminMemberListRequestDto, Pageable pageable) {
        List<Member> memberList = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(
                        emailOrNameStartWith(adminMemberListRequestDto.getSearchText()),
                      roleEq(adminMemberListRequestDto.getRole()),
                        eqDomainOptionName(adminMemberListRequestDto.getDomainName())
                )
                .fetchJoin()
                .orderBy(QMember.member.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(QMember.member.count())
                .from(QMember.member)
                .where(
                        emailOrNameStartWith(adminMemberListRequestDto.getSearchText()),
                        roleEq(adminMemberListRequestDto.getRole()),
                        eqDomainOptionName(adminMemberListRequestDto.getDomainName())
                );

        return PageableExecutionUtils.getPage(memberList, pageable, countQuery::fetchOne);


    }

    @Override
    public Optional<Member> adminFindByIdFetchJoin(Long memberId) {
        return Optional.empty();
    }

    @Override
    public Page<Member> adminManagerListPage(AdminManagerPageRequestDto adminManagerPageRequestDto, Pageable pageable) {

        List<Member> memberList = queryFactory.select(QMember.member)
                .from(QMember.member)
                .where(
                        emailOrNameOrEmployeeStartWith(adminManagerPageRequestDto.getSearchText()),

                        roleEqManagerOrAdmin(),
                        eqDomainOptionName(adminManagerPageRequestDto.getDomainName())

                )
                .orderBy(QMember.member.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(QMember.member.count())
                .from(QMember.member)
                .where(
                        emailOrNameOrEmployeeStartWith(adminManagerPageRequestDto.getSearchText()),

                        roleEqManagerOrAdmin(),
                        eqDomainOptionName(adminManagerPageRequestDto.getDomainName())
                );

        return PageableExecutionUtils.getPage(memberList, pageable, countQuery::fetchOne);
    }

    @Override
    public Long getAdminMemberCount(AdminGetMemberTotalCountRequestDto adminGetMemberTotalCountRequestDto) {
        return null;
    }


    private BooleanExpression emailOrNameStartWith(String searchText){

        return StringUtils.isNullOrEmpty(searchText) ? null :
                Objects.requireNonNull(emailStartWith(searchText))
                        .or(nameStartWith(searchText));

    }


    private BooleanExpression eqDomainOptionName(String domainName){
        return domainName == null ? null : QMember.member.role.ne(Role.ADMIN);
    }


    private BooleanExpression emailOrNameOrEmployeeStartWith(String searchText){

        return StringUtils.isNullOrEmpty(searchText) ? null :
                Objects.requireNonNull(emailStartWith(searchText))
                        .or(nameStartWith(searchText))
                .or(employeeNumberStartWith(searchText));


    }
    private BooleanExpression emailStartWith(String email){
        return StringUtils.isNullOrEmpty(email) ? null : QMember.member.email.startsWith(email);
    }

    private BooleanExpression employeeNumberStartWith(String employeeNumber){
        return StringUtils.isNullOrEmpty(employeeNumber) ? null : QMember.member.employeeNumber.stringValue().startsWith(employeeNumber);
    }


    private BooleanExpression nameStartWith(String name){
        return StringUtils.isNullOrEmpty(name) ? null : QMember.member.name.startsWith(name);
    }

    private BooleanExpression employeeStartWith(Integer employeeNumber){
        return employeeNumber == null ? null : QMember.member.employeeNumber.stringValue().startsWith(employeeNumber.toString());
    }

    private BooleanExpression roleEq(Role role){
        return role == null ? null : QMember.member.role.eq(role);
    }


    private BooleanExpression roleEqManagerOrAdmin(){
        return QMember.member.role.eq(Role.ADMIN).or(QMember.member.role.eq(Role.MANAGER));
    }


}
