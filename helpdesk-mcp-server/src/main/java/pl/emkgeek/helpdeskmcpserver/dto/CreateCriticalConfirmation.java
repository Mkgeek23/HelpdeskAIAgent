package pl.emkgeek.helpdeskmcpserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCriticalConfirmation(
        @Schema(
                title = "Confirm",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        boolean confirm,
        @Schema(
                title = "Affected Users",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        int affectedUsers
) {
}
