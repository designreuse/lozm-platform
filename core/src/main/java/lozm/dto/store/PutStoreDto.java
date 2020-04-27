package lozm.dto.store;

import lombok.Getter;

import javax.validation.constraints.NotNull;

public class PutStoreDto {

    @Getter
    public static class Request {
        @NotNull
        private Long id;

        private String name;

        private Long price;

        private Long quantity;

        private String contents;

        private String size;

        private int flag;
    }

}
