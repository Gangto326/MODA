package com.moda.moda_api.common.pagination;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SliceRequestDto {
    private Integer page = 1;
    private Integer size = 15;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

    /**
     * Spring Data JPA의 Pageable 객체로 변환
     * JPA의 페이지 번호는 0부터 시작하므로 조정 (page - 1)
     */
    public Pageable toPageable() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    }
}
