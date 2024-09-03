package com.gaduationproject.cre8.domain.employmentpost.repository;



import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePost.employeePost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePostWorkFieldChildTag.employeePostWorkFieldChildTag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.set;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostWorkFieldChildTagSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class EmployeePostCustomRepositoryImpl implements EmployeePostCustomRepository{

    private final JPAQueryFactory queryFactory;
    private static final String CREATED_AT="createdAt";
    private static final String CAREER_YEAR = "careerYear";

    @Override
    public Page<EmployeePost> showEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable){

        List<Long> employeePostTmpList = queryFactory
                .select(employeePostWorkFieldChildTag.employeePost.id)
                .from(employeePostWorkFieldChildTag)
                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        List<EmployeePost> content = queryFactory
                .selectFrom(employeePost)
                .leftJoin(employeePost.basicPostContent.workFieldTag).fetchJoin()
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable),employeePost.id.desc())
                .fetch();



        Long count = queryFactory
                .select(employeePost.count())
                .from(employeePost)
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .fetchOne();


        return new PageImpl<>(content,pageable,count);


    }

    @Override
    public Page<EmployeeSearchDBResponseDto> showEmployeePostDtoListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable){


        List<Long> employeePostContainsChild = queryFactory
                .select(employeePostWorkFieldChildTag.employeePost.id)
                .from(employeePostWorkFieldChildTag)
                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        List<Long> employeePostAfterWherePaging = queryFactory
                .select(employeePost.id)
                .from(employeePost)
                .where(checkChildIdByEmployeePostId(employeePostContainsChild,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable),employeePost.id.desc())
                .fetch();


                List<EmployeeSearchDBResponseDto> content = queryFactory
                .selectFrom(employeePost)
                .leftJoin(employeePost.employeePostWorkFieldChildTagList,employeePostWorkFieldChildTag)
                        .where(employeePost.id.in(employeePostAfterWherePaging))
                        .orderBy(employeePostSort(pageable),employeePost.id.desc())
                .transform(groupBy(employeePost.id).list(Projections.constructor(
                        EmployeeSearchDBResponseDto.class,
                        employeePost.id,employeePost.basicPostContent.title,
                        employeePost.basicPostContent.workFieldTag
                        ,employeePost.basicPostContent.member.name
                        ,employeePost.basicPostContent.accessUrl
                        ,employeePost.basicPostContent.member.sex
                        ,employeePost.basicPostContent.member.birthDay
                        ,list(Projections.constructor(
                                EmployeePostWorkFieldChildTagSearchDBResponseDto.class,
                                employeePostWorkFieldChildTag.id,employeePostWorkFieldChildTag.workFieldChildTag.name))
                )));



        Long count = queryFactory
                .select(employeePost.count())
                .from(employeePost)
                .where(checkChildIdByEmployeePostId(employeePostContainsChild,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .fetchOne();


        return new PageImpl<>(content,pageable,count);

    }


    private BooleanExpression checkChildIdByEmployeePostId(final List<Long> employeePostIdList,final List<Long> beforeWorkFieldChildTag){

        if(beforeWorkFieldChildTag.isEmpty()){
            return null;
        }

        return employeePost.id.in(employeePostIdList);
    }




    private BooleanExpression greaterThanMinCareer(final Integer minCareer){

        return minCareer==null?null:employeePost.careerYear.goe(minCareer);
    }

    private BooleanExpression lowerThanMaxCareer(final Integer maxCareer){

        return maxCareer==null?null:employeePost.careerYear.loe(maxCareer);
    }

    private BooleanExpression workFieldIdEqWithEmployeePostTmpList(final Long workFieldTagId){

        return workFieldTagId==null?null:employeePost.basicPostContent.workFieldTag.id.eq(workFieldTagId);

    }

    private OrderSpecifier<?> employeePostSort(final Pageable pageable){
        if(!pageable.getSort().isEmpty()){

            for(Sort.Order order: pageable.getSort()){
                Order direction  = order.getDirection().isAscending()? Order.ASC:Order.DESC;

                switch (order.getProperty()){

                    case CREATED_AT:
                        return new OrderSpecifier(direction,employeePost.createdAt);

                    case CAREER_YEAR:
                        return new OrderSpecifier(direction,employeePost.careerYear);

                }
            }
        }

        return new OrderSpecifier(Order.DESC,employeePost.id);
    }


}
