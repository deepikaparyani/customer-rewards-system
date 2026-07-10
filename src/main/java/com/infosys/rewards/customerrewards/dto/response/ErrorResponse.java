package com.infosys.rewards.customerrewards.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private String path;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}