package com.gaduationproject.cre8.employmentpost.repository;

import static com.gaduationproject.cre8.employmentpost.domain.entity.QEmployerPost.employerPost;
import static com.gaduationproject.cre8.employmentpost.domain.entity.QEmployerPostWorkFieldChildTag.employerPostWorkFieldChildTag;
import static com.gaduationproject.cre8.workfieldtag.entity.QWorkFieldTag.workFieldTag;

import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPost;
import com.gaduationproject.cre8.employmentpost.domain.entity.EmployerPostWorkFieldChildTag;
import com.gaduationproject.cre8.employmentpost.dto.request.EmployerPostSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class EmployerPostCustomRepositoryImpl implements EmployerPostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EmployerPost> showEmployerPostListWithPage(EmployerPostSearch employerPostSearch,Pageable pageable){

        System.out.println(employerPostSearch.getWorkFieldChildTagId());
        System.out.println(employerPostSearch.getWorkFieldId());

      /*  List<EmployerPost> employerPostList = queryFactory
                .selectFrom(employerPost)
                .leftJoin(employerPost.basicPostContent.workFieldTag,workFieldTag).fetchJoin()
                .leftJoin(employerPost.employerPostWorkFieldChildTagList,employerPostWorkFieldChildTag)
                .where(workFieldChildTagEq(employerPostSearch.getWorkFieldChildTagId()))
                .fetch();*/


        List<Long> employerPostTmpList = queryFactory
                .select(employerPostWorkFieldChildTag.employerPost.id)
                .from(employerPostWorkFieldChildTag)
                .where(employerPostWorkFieldChildTag.workFieldChildTag.id.in(employerPostSearch.getWorkFieldChildTagId()))
                .groupBy(employerPostWorkFieldChildTag.employerPost.id)
                .having(employerPostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) employerPostSearch.getWorkFieldChildTagId().size()))
                .fetch();

        System.out.println(employerPostTmpList);

        List<EmployerPost> employerPostList = queryFactory
                .selectFrom(employerPost)
                .where(checkChildIdByEmployerPostId(employerPostTmpList),moreThanPay(employerPostSearch.getPay()),moreThanMinCareer(employerPostSearch.getMinCareer()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        System.out.println(employerPostList);



        return employerPostList;

    }

    private BooleanExpression workFieldChildTagEq(List<Long> childTagId){

        if(childTagId == null || childTagId.isEmpty()){
            return null;
        }

        return JPAExpressions.select(employerPostWorkFieldChildTag.employerPost.id)
                .from(employerPostWorkFieldChildTag)
                .where(employerPostWorkFieldChildTag.workFieldChildTag.id.in(childTagId))
                .groupBy(employerPostWorkFieldChildTag.employerPost.id)
                .having(employerPostWorkFieldChildTag.workFieldChildTag.id.countDistinct().eq((long) childTagId.size()))
                .exists();

       // return childTagId.isEmpty()||childTagId ==null?null:employerPostWorkFieldChildTag.workFieldChildTag.id.in(childTagId);
    }

    private BooleanExpression checkChildIdByEmployerPostId(List<Long> employerPostIdList){
        return employerPostIdList.isEmpty()?null:employerPost.id.in(employerPostIdList);
    }

    private BooleanExpression moreThanPay(Integer paymentAmount){
        return paymentAmount==null?null:employerPost.basicPostContent.payment.paymentAmount.goe(paymentAmount);
    }

    private BooleanExpression moreThanMinCareer(Integer minCareer){
        return minCareer==null?null:employerPost.minCareerYear.goe(minCareer);
    }


}
