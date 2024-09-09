package com.gaduationproject.cre8.app.community.service;

import com.gaduationproject.cre8.app.community.dto.request.CommunityBoardSaveRequestDto;
import com.gaduationproject.cre8.app.community.dto.response.CommunityBoardResponseDto;
import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.domain.community.entity.CommunityBoard;
import com.gaduationproject.cre8.domain.community.repository.CommunityBoardRepository;
import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.member.repository.MemberRepository;
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

    public List<CommunityBoardResponseDto> findAllCommunityBoard(){

        return communityBoardRepository.findAll().stream().map(CommunityBoardResponseDto::from).collect(
                Collectors.toList());

    }


    private Member getLoginMember(final String loginId){

        return memberRepository.findMemberByLoginId(loginId).orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_MEMBER));
    }



}
