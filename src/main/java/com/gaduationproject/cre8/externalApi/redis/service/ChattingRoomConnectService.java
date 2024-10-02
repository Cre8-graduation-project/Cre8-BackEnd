package com.gaduationproject.cre8.externalApi.redis.service;

import com.gaduationproject.cre8.common.response.error.ErrorCode;
import com.gaduationproject.cre8.common.response.error.exception.NotFoundException;
import com.gaduationproject.cre8.externalApi.redis.domain.ChattingRoomConnect;
import com.gaduationproject.cre8.externalApi.redis.repository.ChattingRoomConnectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingRoomConnectService {

    private final ChattingRoomConnectRepository chattingRoomConnectRepository;

    public void connectChattingRoom(Long chattingRoomId,String loginId) {

        ChattingRoomConnect chattingRoomConnect = ChattingRoomConnect.builder()
                        .chattingRoomId(chattingRoomId)
                        .loginId(loginId)
                        .build();

        chattingRoomConnectRepository.save(chattingRoomConnect);
    }

    public void disconnectChattingRoom(Long chattingRoomId,String loginId) {

        ChattingRoomConnect chattingRoomConnect =
                chattingRoomConnectRepository.findByChattingRoomIdAndLoginId(chattingRoomId, loginId)
                .orElseThrow(()->new NotFoundException(ErrorCode.CANT_FIND_CHATTING_ROOM));

        chattingRoomConnectRepository.delete(chattingRoomConnect);
    }

    public boolean isAllConnected(Long chattingRoomId) {
        List<ChattingRoomConnect> connectedList = chattingRoomConnectRepository.findByChattingRoomId(chattingRoomId);
        return connectedList.size() == 2;
    }


}
