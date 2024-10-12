package com.gaduationproject.cre8.externalApi.redis.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.externalApi.redis.domain.ChattingRoomConnect;
import com.gaduationproject.cre8.externalApi.redis.repository.ChattingRoomConnectRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingRoomConnectService {

    private final ChattingRoomConnectRepository chattingRoomConnectRepository;

    public void connectChattingRoom(Long chattingRoomId,String loginId,String sessionId) {

        ChattingRoomConnect chattingRoomConnect = ChattingRoomConnect.builder()
                        .chattingRoomId(chattingRoomId)
                        .loginId(loginId)
                        .sessionId(sessionId)
                        .build();

        chattingRoomConnectRepository.save(chattingRoomConnect);
    }

    public void disconnectChattingRoom(final String sessionId) {

        ChattingRoomConnect chattingRoomConnect =
                chattingRoomConnectRepository.findBySessionId(sessionId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));

        chattingRoomConnectRepository.delete(chattingRoomConnect);
    }

    public boolean isAllConnected(Long chattingRoomId) {
        Set<ChattingRoomConnect> connectedList = chattingRoomConnectRepository.findByChattingRoomId(chattingRoomId);
        return connectedList.size() == 2;
    }


}
