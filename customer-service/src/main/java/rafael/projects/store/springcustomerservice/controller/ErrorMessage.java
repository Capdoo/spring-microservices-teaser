package rafael.projects.store.springcustomerservice.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ErrorMessage {

    private String code;
    private List<Map<String, String>> messages;

}
