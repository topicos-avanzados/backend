package com.topicos.backend.services;

import static java.time.temporal.ChronoUnit.DAYS;

import com.topicos.backend.dto.NotificationDTO;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.IndicatorValueRepository;
import com.topicos.backend.utils.Mappers;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

  private final IndicatorValueRepository indicatorValueRepository;

  public List<NotificationDTO> getAll(Long companyId) {
    List<IndicatorValue> listIndicatorValues = this.indicatorValueRepository.findAllByCompanyId_Id(companyId);
    Set<Indicator> indicators = listIndicatorValues
        .stream()
        .map(IndicatorValue::getIndicatorId)
        .collect(Collectors.toSet());

    return indicators
        .stream()
        .map(ind -> this.getNotification(companyId, ind))
        .collect(Collectors.toList());
  }

  private NotificationDTO getNotification(Long companyId, Indicator indicator) {
    Date lastIndicatorDate = this.indicatorValueRepository
        .findAllByIndicatorId_IdAndCompanyId_IdOrderByDateDesc(indicator.getId(), companyId)
        .get(0)
        .getDate();

    Long daysFromLast = (System.currentTimeMillis() - lastIndicatorDate.getTime()) / (24 * 3600 * 1000);

    Long frequency = indicator.getFrequency();

    NotificationDTO notification = NotificationDTO
        .builder()
        .indicator(Mappers.buildIndicatorDTO(indicator))
        .daysFromLast(daysFromLast)
        .id(indicator.getId())
        .build();
    if (frequency != null) {
      SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
      Integer currentYear = Integer.valueOf(getYearFormat.format(lastIndicatorDate));

      SimpleDateFormat getMonthFormat = new SimpleDateFormat("MM");
      Integer currentMonth = Integer.valueOf(getMonthFormat.format(lastIndicatorDate));

      SimpleDateFormat getDayFormat = new SimpleDateFormat("dd");
      Integer currentDay = Integer.valueOf(getDayFormat.format(lastIndicatorDate));

      LocalDate lastDate = LocalDate.of(currentYear, currentMonth, currentDay);
      LocalDate nextDate = lastDate.plusMonths(frequency);
      Long remainingDays = DAYS.between(LocalDate.now(), nextDate);

      notification.setRemainingDays(remainingDays);

      if (remainingDays < 0) {
        notification.setAlarm(true);
      }
    }
    return notification;

  }

}