package com.gaduationproject.cre8.domain.employmentpost.repository;



import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePost.employeePost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployeePostWorkFieldChildTag.employeePostWorkFieldChildTag;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployeePost;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EmployeePostSearch;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
                .having(employeePostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) employeePostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        List<EmployeePost> content = queryFactory
                .selectFrom(employeePost)
                .leftJoin(employeePost.basicPostContent.workFieldTag).fetchJoin()
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
