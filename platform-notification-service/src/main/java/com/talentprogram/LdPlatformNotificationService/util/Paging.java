package com.talentprogram.LdPlatformNotificationService.util;
import com.talentprogram.LdPlatformNotificationService.dto.PagedResponse;
import org.springframework.data.domain.Page;

public final class Paging {
  
  public static <T> PagedResponse<T> from(Page<T> page) {
    return new PagedResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast()
    );
  }
}
