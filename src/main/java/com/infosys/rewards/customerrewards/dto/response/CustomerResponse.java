package com.infosys.rewards.customerrewards.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;

    private String name;

    private String email;

    private LocalDateTime createdAt;
}