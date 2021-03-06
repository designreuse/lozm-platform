package lozm.object.dto.coupon;

import lombok.Getter;
import lozm.object.dto.BaseUserDto;
import lozm.object.dto.user.GetUserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class PostCouponUserDto {

    @Getter
    public static class Request extends BaseUserDto {
        @Size(min = 1)
        private List<GetUserDto> userList = new ArrayList<>();

        @NotNull
        private Long couponId;

        @NotNull
        private Long couponUserQuantity;
    }

}
