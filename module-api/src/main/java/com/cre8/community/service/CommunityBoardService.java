package com.cre8.community.service;

import com.cre8.community.dto.response.CommunityBoardResponseDto;
import com.cre8.community.repository.CommunityBoardRepository;
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

    public List<CommunityBoardResponseDto> findAllCommunityBoard(){

        return communityBoardRepository.findAll().stream().map(CommunityBoardResponseDto::from).collect(
                Collectors.toList());

    }

}
