package com.gaduationproject.cre8.domain.community.repository;

import static com.gaduationproject.cre8.domain.community.entity.QCommunityPost.communityPost;

import com.gaduationproject.cre8.domain.community.dto.CommunityPostSearchDBResponseDto;
import com.gaduationproject.cre8.domain.community.entity.CommunityPost;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CommunityPostCustomRepositoryImpl implements CommunityPostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<CommunityPostSearchDBResponseDto> showCommunityPostWithNoOffSet(final Long lastCommunityPostId,final Long communityBoardId,final Pageable pageable){

        System.out.println("hi");

        List<CommunityPostSearchDBResponseDto> results = queryFactory.select(
                        Projections.constructor(CommunityPostSearchDBResponseDto.class,
                                communityPost.id, communityPost.title,communityPost.writer.nickName,communityPost.createdAt))
                .from(communityPost)
                .join(communityPost.writer)
                .where(ltStoreId(lastCommunityPostId),
                        findByCommunityBoardId(communityBoardId))
                .orderBy(communityPost.id.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();

        System.out.println("여기영");


        return checkLastPage(pageable,results);



    }


    private BooleanExpression ltStoreId(final Long lastCommunityPostId) {
        if (lastCommunityPostId == null) {
            return null;
        }

        return communityPost.id.lt(lastCommunityPostId);
    }

    private BooleanExpression findByCommunityBoardId(final Long communityBoardId){

        return communityPost.communityBoard.id.eq(communityBoardId);

    }

    private Slice<CommunityPostSearchDBResponseDto> checkLastPage(final Pageable pageable, final List<CommunityPostSearchDBResponseDto> results) {

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }






}
