package com.topicos.backend.services;

import com.topicos.backend.dto.LogDTO;
import com.topicos.backend.persistence.model.Log;
import com.topicos.backend.persistence.repository.LogRepository;
import com.topicos.backend.utils.Mappers;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class LogService {

  private final LogRepository logRepository;

  public LogDTO addLog(LogDTO log) {
    Log newLog = this.logRepository.save(Mappers.buildLog(log));
    return Mappers.buildLogDTO(newLog);
  }


  public List<LogDTO> getAllLogs() {
    List<Log> listLogs = this.logRepository.findAll();

    return listLogs
        .stream()
        .map(Mappers::buildLogDTO)
        .collect(Collectors.toList());
  }

}
