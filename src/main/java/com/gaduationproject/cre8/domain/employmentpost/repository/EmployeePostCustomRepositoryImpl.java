package com.gaduationproject.cre8.domain.employmentpost.repository;



import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePost.employeePost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePostWorkFieldChildTag.employeePostWorkFieldChildTag;
import static com.gaduationproject.cre8.domain.workfieldtag.entity.QWorkFieldChildTag.workFieldChildTag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.set;

import com.gaduationproject.cre8.app.employmentpost.dto.response.EmployeePostSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeePostWorkFieldChildTagSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto2;
import com.gaduationproject.cre8.domain.employmentpost.dto.EmployeeSearchResponseDto3;
import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.domain.employmentpost.search.EmployeePostSearch;
import com.gaduationproject.cre8.domain.workfieldtag.entity.WorkFieldChildTag;
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
               // .leftJoin(employeePost.basicPostContent.workFieldTag).fetchJoin()
                .join(employeePost.basicPostContent.member).fetchJoin()
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable))
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
    public Page<EmployeeSearchResponseDto> testShowEmployeePostListWithPage(final EmployeePostSearch employeePostSearch,final Pageable pageable){

//        List<Long> employeePostTmpList = queryFactory
//                .select(employeePostWorkFieldChildTag.employeePost.id)
//                .from(employeePostWorkFieldChildTag)
//                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
//                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
//                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
//                .fetch();
//
//        List<EmployeeSearchResponseDto> content = queryFactory
//                .selectFrom(employeePost)
//                .leftJoin(employeePost.basicPostContent.workFieldTag)
//                .join(employeePost.basicPostContent.member)
//                .leftJoin(employeePost.employeePostWorkFieldChildTagList,employeePostWorkFieldChildTag)
//                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
//                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
//                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(employeePostSort(pageable))
//                .transform(groupBy(employeePost.id).list(Projections.constructor(EmployeeSearchResponseDto.class,
//                        employeePost.id,employeePost.basicPostContent.title,
//                        employeePost.basicPostContent.workFieldTag
//                        ,employeePost.basicPostContent.member.name
//                        ,employeePost.basicPostContent.accessUrl
//                        ,employeePost.basicPostContent.member.sex
//                        ,employeePost.basicPostContent.member.birthDay
//                        ,list(Projections.constructor(
//                                EmployeePostWorkFieldChildTagSearchResponseDto.class,
//                                employeePostWorkFieldChildTag.id,employeePostWorkFieldChildTag.workFieldChildTag.name))
//                        ,employeePost.createdAt
//                )));
//
//        Long count = queryFactory
//                .select(employeePost.count())
//                .from(employeePost)
//                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
//                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
//                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
//                .fetchOne();
//
//
//        return new PageImpl<>(content,pageable,count);


        List<Long> employeePostTmpList = queryFactory
                .select(employeePostWorkFieldChildTag.employeePost.id)
                .from(employeePostWorkFieldChildTag)
                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        List<Long> employeePostTmpTmpList = queryFactory
                .select(employeePost.id)
                .from(employeePost)
               // .leftJoin(employeePost.basicPostContent.workFieldTag).fetchJoin()
                //   .join(employeePost.basicPostContent.member).fetchJoin()
              //  .leftJoin(employeePost.basicPostContent.workFieldTag)
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable))
                .fetch();

//        List<EmployeeSearchResponseDto> content = queryFactory
//                .select(Projections.constructor(EmployeeSearchResponseDto.class,employeePost.id,employeePost.basicPostContent.title,
//                        employeePost.basicPostContent.workFieldTag,employeePost.basicPostContent.member.name,
//                        employeePost.basicPostContent.accessUrl,
//                        employeePost.basicPostContent.member.sex,
//                        employeePost.basicPostContent.member.birthDay))
//                .from(employeePost)
//             //   .leftJoin(employeePost.basicPostContent.workFieldTag)
//                .join(employeePost.basicPostContent.member)
//                .where(employeePost.id.in(employeePostTmpTmpList))
//                .fetch();


                List<EmployeeSearchResponseDto> content2 = queryFactory
                .selectFrom(employeePost)
//                .leftJoin(employeePost.basicPostContent.workFieldTag)
//                .join(employeePost.basicPostContent.member)
                .leftJoin(employeePost.employeePostWorkFieldChildTagList,employeePostWorkFieldChildTag)
                        .where(employeePost.id.in(employeePostTmpTmpList))
                .transform(groupBy(employeePost.id).list(Projections.constructor(EmployeeSearchResponseDto.class,
                        employeePost.id,employeePost.basicPostContent.title,
                        employeePost.basicPostContent.workFieldTag
                        ,employeePost.basicPostContent.member.name
                        ,employeePost.basicPostContent.accessUrl
                        ,employeePost.basicPostContent.member.sex
                        ,employeePost.basicPostContent.member.birthDay
                        ,list(Projections.constructor(
                                EmployeePostWorkFieldChildTagSearchResponseDto.class,
                                employeePostWorkFieldChildTag.id,employeePostWorkFieldChildTag.workFieldChildTag.name))
                )));

        Long count = queryFactory
                .select(employeePost.count())
                .from(employeePost)
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .fetchOne();


        return new PageImpl<>(content2,pageable,count);

    }

    @Override
    public Page<EmployeeSearchResponseDto2> testShowEmployeePostListWithPage2(final EmployeePostSearch employeePostSearch,final Pageable pageable){

        List<Long> employeePostTmpList = queryFactory
                .select(employeePostWorkFieldChildTag.employeePost.id)
                .from(employeePostWorkFieldChildTag)
                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();




        List<EmployeeSearchResponseDto2> content = queryFactory
                .select(Projections.constructor(EmployeeSearchResponseDto2.class,employeePost.id,employeePost.basicPostContent.title,
                        employeePost.basicPostContent.workFieldTag,
                        employeePost.basicPostContent.member.name,employeePost.basicPostContent.accessUrl,employeePost.basicPostContent.member.sex,employeePost.basicPostContent.member
                                .birthDay))
                .from(employeePost)
                .leftJoin(employeePost.basicPostContent.workFieldTag)
                .join(employeePost.basicPostContent.member)
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable))
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
    public Page<EmployeeSearchResponseDto3> testShowEmployeePostListWithPage3(final EmployeePostSearch employeePostSearch,final Pageable pageable){

        List<Long> employeePostTmpList = queryFactory
                .select(employeePostWorkFieldChildTag.employeePost.id)
                .from(employeePostWorkFieldChildTag)
                .where(employeePostWorkFieldChildTag.workFieldChildTag.id.in(employeePostSearch.getWorkFieldChildTagId()))
                .groupBy(employeePostWorkFieldChildTag.employeePost.id)
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.count().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();




        List<EmployeeSearchResponseDto3> content = queryFactory
                .select(Projections.constructor(EmployeeSearchResponseDto3.class,employeePost,
                        employeePost.basicPostContent.member.name,employeePost.basicPostContent.member.sex,
                        employeePost.basicPostContent.member.birthDay))
                .from(employeePost)
                .join(employeePost.basicPostContent.member)
                .where(checkChildIdByEmployeePostId(employeePostTmpList,employeePostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employeePostSearch.getMinCareer()),lowerThanMaxCareer(employeePostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployeePostTmpList(employeePostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employeePostSort(pageable))
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

        return new OrderSpecifier(Order.ASC,employeePost.id);
    }


}
