package org.pg.ldap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ldap")
public interface CoreServiceClient {
    @GetMapping("/health")
    String checkCoreServiceHealth();
}
