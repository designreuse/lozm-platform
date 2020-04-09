package lozm.domain.entity.embedded;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Clothing {

    private String contents;
    private String size;

    public void setClothing(String contents, String size) {
        this.contents = contents;
        this.size = size;
    }

}
