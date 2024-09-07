package com.gaduationproject.cre8.domain.employmentpost.repository;



import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePost.employeePost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePostWorkFieldChildTag.employeePostWorkFieldChildTag;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployerPost.employerPost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployerPostWorkFieldChildTag.employerPostWorkFieldChildTag;
import static com.gaduationproject.cre8.domain.workfieldtag.entity.QWorkFieldChildTag.workFieldChildTag;
import static com.gaduationproject.cre8.domain.workfieldtag.entity.QWorkFieldTag.workFieldTag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostWorkFieldChildTagSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerPostWorkFieldChildTagSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployerSearchDBResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployerPostSearch;
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
public class EmployerPostCustomRepositoryImpl implements EmployerPostCustomRepository{

    private final JPAQueryFactory queryFactory;
    private static final String CREATED_AT="createdAt";
    private static final String DEAD_LINE="deadLine";
    private static final String HOPE_CAREER_YEAR = "hopeCareerYear";

    @Override
    public Page<EmployerPost> showEmployerPostListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable){

        List<Long> employerPostContainsChild = queryFactory
                .select(employerPostWorkFieldChildTag.employerPost.id)
                .from(employerPostWorkFieldChildTag)
                .where(employerPostWorkFieldChildTag.workFieldChildTag.id.in(employerPostSearch.getWorkFieldChildTagId()))
                .groupBy(employerPostWorkFieldChildTag.employerPost.id)
                .having(employerPostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) employerPostSearch.getWorkFieldChildTagId().size()))
                .fetch();


        List<EmployerPost> content = queryFactory
                .selectFrom(employerPost)
                .leftJoin(employerPost.basicPostContent.workFieldTag).fetchJoin()
                .where(checkChildIdByEmployerPostId(employerPostContainsChild,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostContainsChild(employerPostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employerPostSort(pageable),employerPost.id.desc())
                .fetch();

        Long count = queryFactory
                .select(employerPost.count())
                .from(employerPost)
                .where(checkChildIdByEmployerPostId(employerPostContainsChild,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostContainsChild(employerPostSearch.getWorkFieldId()))
                .fetchOne();


        return new PageImpl<>(content,pageable,count);


    }

    @Override
    public Page<EmployerSearchDBResponseDto> showEmployerPostDtoListWithPage(final EmployerPostSearch employerPostSearch,
                                                                             final Pageable pageable) {

        List<Long> employerPostContainsChild = queryFactory
                .select(employerPostWorkFieldChildTag.employerPost.id)
                .from(employerPostWorkFieldChildTag)
                .where(employerPostWorkFieldChildTag.workFieldChildTag.id.in(employerPostSearch.getWorkFieldChildTagId()))
                .groupBy(employerPostWorkFieldChildTag.employerPost.id)
                .having(employerPostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) employerPostSearch.getWorkFieldChildTagId().size()))
                .fetch();


        List<Long> employerPostAfterWherePaging = queryFactory
                .select(employerPost.id)
                .from(employerPost)
                .where(checkChildIdByEmployerPostId(employerPostContainsChild,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostContainsChild(employerPostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employerPostSort(pageable),employerPost.id.desc())
                .fetch();


        List<EmployerSearchDBResponseDto> content = queryFactory
                .selectFrom(employerPost)
                .leftJoin(employerPost.employerPostWorkFieldChildTagList,employerPostWorkFieldChildTag)
                .leftJoin(employerPostWorkFieldChildTag.workFieldChildTag,workFieldChildTag)
                .leftJoin(employerPost.basicPostContent.workFieldTag,workFieldTag)
                .where(employerPost.id.in(employerPostAfterWherePaging))
                .orderBy(employerPostSort(pageable),employerPost.id.desc())
                .transform(groupBy(employerPost.id).list(Projections.constructor(
                        EmployerSearchDBResponseDto.class,
                        employerPost.id,employerPost.basicPostContent.title,
                        employerPost.companyName,
                        employerPost.basicPostContent.workFieldTag,
                        employerPost.enrollDurationType,
                        employerPost.basicPostContent.accessUrl,
                        list(Projections.constructor(
                                EmployerPostWorkFieldChildTagSearchDBResponseDto.class,
                                employerPostWorkFieldChildTag.id,employerPostWorkFieldChildTag.workFieldChildTag.name))
                )));



        Long count = queryFactory
                .select(employerPost.count())
                .from(employerPost)
                .where(checkChildIdByEmployerPostId(employerPostContainsChild,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostContainsChild(employerPostSearch.getWorkFieldId()))
                .fetchOne();


        return new PageImpl<>(content,pageable,count);

    }

    private BooleanExpression checkChildIdByEmployerPostId(final List<Long> employerPostIdList,final List<Long> beforeWorkFieldChildTag){

        if(beforeWorkFieldChildTag.isEmpty()){
            return null;
        }

        return employerPost.id.in(employerPostIdList);
    }




    private BooleanExpression greaterThanMinCareer(final Integer minCareer){

        return minCareer==null?null:employerPost.hopeCareerYear.goe(minCareer);
    }

    private BooleanExpression lowerThanMaxCareer(final Integer maxCareer){

        return maxCareer==null?null:employerPost.hopeCareerYear.loe(maxCareer);
    }

    private BooleanExpression workFieldIdEqWithEmployerPostContainsChild(final Long workFieldTagId){

        return workFieldTagId==null?null:employerPost.basicPostContent.workFieldTag.id.eq(workFieldTagId);

    }

    private OrderSpecifier<?> employerPostSort(final Pageable pageable){
        if(!pageable.getSort().isEmpty()){

            for(Sort.Order order: pageable.getSort()){
                Order direction  = order.getDirection().isAscending()? Order.ASC:Order.DESC;

                switch (order.getProperty()){

                    case CREATED_AT:
                        return new OrderSpecifier(direction,employerPost.createdAt);

                    case DEAD_LINE:
                        return new OrderSpecifier(direction,employerPost.deadLine);

                    case HOPE_CAREER_YEAR:
                        return new OrderSpecifier(direction,employerPost.hopeCareerYear);

                }
            }
        }

        return new OrderSpecifier(Order.DESC,employerPost.id);
    }






}
