package com.challenge.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDTO {

    public static class CustomerRequest {
        // PROBLEMA Junior: sin @NotBlank
        private String name;

        // PROBLEMA Junior: sin @Email — cualquier string pasa como email
        private String email;

        private String phone;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }

    public static class CustomerResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private boolean active;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
}
