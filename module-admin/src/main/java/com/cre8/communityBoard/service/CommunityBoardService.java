package com.cre8.communityBoard.service;


import com.cre8.community.entity.CommunityBoard;
import com.cre8.community.repository.CommunityBoardRepository;
import com.cre8.communityBoard.dto.request.CommunityBoardSaveRequestDto;
import com.cre8.member.entity.Member;
import com.cre8.member.repository.MemberRepository;
import com.cre8.response.error.ErrorCode;
import com.cre8.response.error.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommunityBoardService {

    private final CommunityBoardRepository communityBoardRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void saveCommunityBoard(final CommunityBoardSaveRequestDto communityBoardSaveRequestDto,final String loginId){

        CommunityBoard communityBoard = CommunityBoard.builder()
                .manager(getLoginMember(loginId))
                .name(communityBoardSaveRequestDto.getName())
                .build();

        communityBoardRepository.save(communityBoard);
    }

    @Transactional
    public void deleteCommunityBoard(final Long communityBoardId){

        CommunityBoard communityBoard  = communityBoardRepository.findById(communityBoardId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_COMMUNITY_BOARD));

        communityBoardRepository.delete(communityBoard);
    }




    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }



}
