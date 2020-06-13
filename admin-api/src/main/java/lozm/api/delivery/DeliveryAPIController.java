package lozm.api.delivery;

import lombok.RequiredArgsConstructor;
import lozm.object.dto.APIResponseDto;

import lozm.object.dto.delivery.DeleteDeliveryDto;
import lozm.object.dto.delivery.GetDeliveryDto;
import lozm.object.dto.delivery.PostDeliveryDto;
import lozm.object.dto.delivery.PutDeliveryDto;
import lozm.object.vo.delivery.DeliveryVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/api/delivery")
@RestController
@RequiredArgsConstructor
public class DeliveryAPIController {

    private final DeliveryService deliveryService;


    @GetMapping
    public APIResponseDto getDelivery() {
        APIResponseDto resDto = new APIResponseDto<>();

        try {
            List<GetDeliveryDto> result = deliveryService.getDeliveryList();
            GetDeliveryDto.Response deliveryResDto = new GetDeliveryDto.Response();
            deliveryResDto.setList(result);

            resDto.setSuccess(true);
            resDto.setData(deliveryResDto);
        } catch (Exception e) {
            resDto.setSuccess(false);
            resDto.setMessage(e.getMessage());
        }

        return resDto;
    }

    @PostMapping
    public APIResponseDto postDelivery(@RequestBody @Valid PostDeliveryDto.Request reqDto) {
        APIResponseDto resDto = new APIResponseDto<>();

        try {
            DeliveryVo deliveryVo = DeliveryVo.builder()
                    .country(reqDto.getCountry())
                    .zipCode(reqDto.getZipCode())
                    .city(reqDto.getCity())
                    .street(reqDto.getStreet())
                    .etc(reqDto.getEtc())
                    .status(reqDto.getStatus())
                    .build();

            deliveryService.save(deliveryVo);
            resDto.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            resDto.setSuccess(false);
            resDto.setMessage(e.getMessage());
        }

        return resDto;
    }

    @PutMapping
    public APIResponseDto putDelivery(@RequestBody @Valid PutDeliveryDto.Request reqDto) {
        APIResponseDto resDto = new APIResponseDto<>();

        try {
            DeliveryVo deliveryVo = DeliveryVo.builder()
                    .id(reqDto.getId())
                    .country(reqDto.getCountry())
                    .zipCode(reqDto.getZipCode())
                    .city(reqDto.getCity())
                    .street(reqDto.getStreet())
                    .etc(reqDto.getEtc())
                    .status(reqDto.getStatus())
                    .flag(1)
                    .build();

            deliveryService.update(deliveryVo);
            resDto.setSuccess(true);
        } catch (Exception e) {
            resDto.setSuccess(false);
            resDto.setMessage(e.getMessage());
        }

        return resDto;
    }

    @DeleteMapping
    public APIResponseDto deleteDelivery(@RequestBody @Valid DeleteDeliveryDto.Request reqDto) {
        APIResponseDto resDto = new APIResponseDto<>();

        try {
            for (DeleteDeliveryDto dto : reqDto.getList()) {
                DeliveryVo deliveryVo = DeliveryVo.builder()
                        .id(dto.getId())
                        .flag(0)
                        .build();

                deliveryService.delete(deliveryVo);
            }

            resDto.setSuccess(true);
        } catch (Exception e) {
            resDto.setSuccess(false);
            resDto.setMessage(e.getMessage());
        }

        return resDto;
    }

}