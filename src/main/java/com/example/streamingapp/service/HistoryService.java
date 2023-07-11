package com.example.streamingapp.service;

import com.example.streamingapp.domain.History;
import com.example.streamingapp.domain.HistoryPK;
import com.example.streamingapp.dto.HistoryDto;
import com.example.streamingapp.dto.UserCustom;
import com.example.streamingapp.repository.HistoryRepository;
import com.example.streamingapp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public void saveHistory(Integer videoId){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);

        History history = History.builder()
                .historyPK(HistoryPK.builder()
                        .memberCode(memberCode)
                        .videoId(videoId)
                        .build())
                .watchDt(formatter.format(today))
                .build();

        historyRepository.save(history);
    }

    public List<HistoryDto> getHistories(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        List<History> histories = historyRepository.findAllByHistoryPKMemberCodeOrderByWatchDtDesc(memberCode);

        return histories.stream().map(h -> new HistoryDto(h)).collect(Collectors.toList());
    }

    public void deleteHistory(Integer videoId){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserCustom userDetails = (UserCustom)principal;
        Integer memberCode = userDetails.getMemberCode();

        historyRepository.deleteById(HistoryPK.builder().memberCode(memberCode).videoId(videoId).build());
    }
}
