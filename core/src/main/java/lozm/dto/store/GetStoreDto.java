package lozm.dto.store;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetStoreDto {

    private Long id;
    private String name;
    private String telNumber;
    private String info;

    public GetStoreDto(Long id, String name, String telNumber, String info) {
        this.id = id;
        this.name = name;
        this.telNumber = telNumber;
        this.info = info;
    }

    @Getter
    @Setter
    public static class Response {
        List<GetStoreDto> list = new ArrayList<>();
    }

}
