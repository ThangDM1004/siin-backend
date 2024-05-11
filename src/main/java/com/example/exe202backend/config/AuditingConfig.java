//package com.example.exe202backend.config;
//
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//import java.util.Optional;
//
//@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorAware")
//public class AuditingConfig {
//    @Bean
//    public AuditorAware<String> auditorAware(){
//        return new AuditorAwareImpl();
//    }
//
//    public static class AuditorAwareImpl implements AuditorAware<String>{
//
//        @Override
//        public Optional<String> getCurrentAuditor() {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if(auth != null && auth.isAuthenticated()) return Optional.of(auth.getName());
//            return Optional.empty();
//        }
//    }
//
//}
