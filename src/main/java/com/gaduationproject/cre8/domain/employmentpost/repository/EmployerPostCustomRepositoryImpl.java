package com.gaduationproject.cre8.domain.employmentpost.repository;



import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployerPost.employerPost;
import static com.gaduationproject.cre8.domain.employmentpost.entity.QEmployerPostWorkFieldChildTag.employerPostWorkFieldChildTag;

import com.gaduationproject.cre8.domain.employmentpost.entity.EmployerPost;
import com.gaduationproject.cre8.app.employmentpost.dto.request.EmployerPostSearch;
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
public class EmployerPostCustomRepositoryImpl implements EmployerPostCustomRepository{

    private final JPAQueryFactory queryFactory;
    private static final String CREATED_AT="createdAt";
    private static final String DEAD_LINE="deadLine";
    private static final String HOPE_CAREER = "hopeCareer";

    @Override
    public Page<EmployerPost> showEmployerPostListWithPage(final EmployerPostSearch employerPostSearch,final Pageable pageable){

        List<Long> employerPostTmpList = queryFactory
                .select(employerPostWorkFieldChildTag.employerPost.id)
                .from(employerPostWorkFieldChildTag)
                .where(employerPostWorkFieldChildTag.workFieldChildTag.id.in(employerPostSearch.getWorkFieldChildTagId()))
                .groupBy(employerPostWorkFieldChildTag.employerPost.id)
                .having(employerPostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) employerPostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        List<EmployerPost> content = queryFactory
                .selectFrom(employerPost)
                .leftJoin(employerPost.basicPostContent.workFieldTag).fetchJoin()
                .where(checkChildIdByEmployerPostId(employerPostTmpList,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostTmpList(employerPostSearch.getWorkFieldId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(employerPostSort(pageable))
                .fetch();

        Long count = queryFactory
                .select(employerPost.count())
                .from(employerPost)
                .where(checkChildIdByEmployerPostId(employerPostTmpList,employerPostSearch.getWorkFieldChildTagId())
                        ,greaterThanMinCareer(employerPostSearch.getMinCareer()),lowerThanMaxCareer(employerPostSearch.getMaxCareer())
                        ,workFieldIdEqWithEmployerPostTmpList(employerPostSearch.getWorkFieldId()))
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

    private BooleanExpression workFieldIdEqWithEmployerPostTmpList(final Long workFieldTagId){

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

                    case HOPE_CAREER:
                        return new OrderSpecifier(direction,employerPost.hopeCareerYear);

                }
            }
        }

        return new OrderSpecifier(Order.ASC,employerPost.id);
    }






}
