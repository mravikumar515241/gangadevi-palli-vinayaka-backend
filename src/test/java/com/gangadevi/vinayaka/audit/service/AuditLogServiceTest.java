package com.gangadevi.vinayaka.audit.service;

import com.gangadevi.vinayaka.audit.entity.AuditAction;
import com.gangadevi.vinayaka.audit.entity.AuditLog;
import com.gangadevi.vinayaka.audit.repository.AuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository repository;

    @InjectMocks
    private AuditLogService service;

    @Test
    void recordShouldPersistAuditDetails() {
        service.record("admin", AuditAction.CREATE, "GALLERY_IMAGE", 5L,
                "Uploaded gallery image", "req-123", "127.0.0.1");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());

        AuditLog saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("admin");
        assertThat(saved.getAction()).isEqualTo(AuditAction.CREATE);
        assertThat(saved.getEntityType()).isEqualTo("GALLERY_IMAGE");
        assertThat(saved.getEntityId()).isEqualTo(5L);
        assertThat(saved.getDescription()).isEqualTo("Uploaded gallery image");
        assertThat(saved.getRequestId()).isEqualTo("req-123");
        assertThat(saved.getIpAddress()).isEqualTo("127.0.0.1");
    }

    @Test
    void recordShouldUseSystemWhenUsernameIsBlank() {
        service.record(" ", AuditAction.DELETE, "DONATION", 10L,
                "Deleted donation", null, null);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("SYSTEM");
    }
}
