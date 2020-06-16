package lozm.api.route;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lozm.object.dto.ApiResponseDto;
import lozm.object.dto.coupon.GetCouponDto;
import lozm.object.dto.coupon.GetCouponUserDto;
import lozm.object.dto.item.GetClothingDto;
import lozm.object.dto.store.GetStoreDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

import static lozm.object.code.SessionType.USER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RouteController {

    private final HttpSession httpSession;
    private final RouteService routeService;


    @GetMapping(value = "/home")
    public String home(ModelMap modelMap) {
        return "pages/home";
    }

    @GetMapping(value = "/manage/store")
    public String manageStore(ModelMap modelMap) {
        return "pages/store/store";
    }

    @GetMapping(value = "/manage/store/{storeId}")
    public String manageItem(ModelMap modelMap, @PathVariable(value = "storeId") Long storeId) throws Exception {
        log.debug("Store ID: "+storeId);

        GetStoreDto.Response getStoreDetail = routeService.getStoreDetail(storeId);
        modelMap.addAttribute("storeDetail", getStoreDetail);

        ApiResponseDto getOuter = routeService.getStoreClothing(storeId, "OUTER");
        GetClothingDto.Response outer = (GetClothingDto.Response) getOuter.getData();
        List<GetClothingDto> outerList = outer.getList();
        modelMap.addAttribute("outerList", outerList);

        ApiResponseDto getTop = routeService.getStoreClothing(storeId, "TOP");
        GetClothingDto.Response top = (GetClothingDto.Response) getTop.getData();
        List<GetClothingDto> topList = top.getList();
        modelMap.addAttribute("topList", topList);

        ApiResponseDto getBottom = routeService.getStoreClothing(storeId, "BOTTOM");
        GetClothingDto.Response bottom = (GetClothingDto.Response) getBottom.getData();
        List<GetClothingDto> bottomList = bottom.getList();
        modelMap.addAttribute("bottomList", bottomList);

        ApiResponseDto getShoes = routeService.getStoreClothing(storeId, "SHOES");
        GetClothingDto.Response shoes = (GetClothingDto.Response) getShoes.getData();
        List<GetClothingDto> shoesList = shoes.getList();
        modelMap.addAttribute("shoesList", shoesList);

        return "pages/store/storeDetail";
    }

    @GetMapping(value = "/manage/item")
    public String manageItem(ModelMap modelMap) {
        return "pages/item/item";
    }

    @GetMapping(value = "/manage/coupon")
    public String manageCoupon(ModelMap modelMap) {
        return "pages/coupon/coupon";
    }

    @GetMapping(value = "/manage/coupon/{couponId}")
    public String manageCoupon(ModelMap modelMap, @PathVariable(value = "couponId") Long couponId) throws Exception {

        ApiResponseDto getCouponDetail = routeService.getCouponDetail(couponId);
        GetCouponDto couponDetail = (GetCouponDto) getCouponDetail.getData();
        modelMap.addAttribute("couponDetail", couponDetail);

        ApiResponseDto getCouponUser = routeService.getCouponUser(couponId);
        GetCouponUserDto.Response couponUser = (GetCouponUserDto.Response) getCouponUser.getData();
        List<GetCouponUserDto> couponUserList = couponUser.getList();
        modelMap.addAttribute("couponUserList", couponUserList);

        return "pages/coupon/couponDetail";
    }

    @GetMapping(value = "/manage/delivery")
    public String manageDelivery(ModelMap modelMap) {
        return "pages/delivery/delivery";
    }

    @GetMapping(value = "/manage/orders")
    public String manageOrders(ModelMap modelMap) {
        return "pages/orders/orders";
    }

    @GetMapping(value = "/setting/user")
    public String settingUser(ModelMap modelMap) {
        return "pages/user/user";
    }

    @GetMapping(value = "/sign/in")
    public String signIn(ModelMap modelMap) {
        return "pages/sign/signIn";
    }

    @GetMapping(value = "/sign/out")
    public String signOut(ModelMap modelMap) {
        httpSession.removeAttribute(USER.name());
//        httpSession.removeAttribute(PREV_PAGE.name());

        return this.signIn(modelMap);
    }

}
