package lozm.api.orders;

import lombok.RequiredArgsConstructor;
import lozm.object.dto.orders.GetOrdersDto;
import lozm.entity.delivery.Delivery;
import lozm.exception.APIException;
import lozm.repository.RepositorySupport;
import lozm.repository.delivery.DeliveryRepository;
import lozm.object.vo.orders.OrdersVo;
import lozm.entity.coupon.Coupon;
import lozm.entity.item.Item;
import lozm.entity.orders.Orders;
import lozm.entity.orders.OrdersItem;
import lozm.entity.user.User;
import lozm.repository.coupon.CouponRepository;
import lozm.repository.item.ItemRepository;
import lozm.repository.orders.OrdersRepository;
import lozm.repository.orders.OrdersItemRepository;
import lozm.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrdersItemRepository ordersItemRepository;
    private final CouponRepository couponRepository;
    private final RepositorySupport repositorySupport;
    private final DeliveryRepository deliveryRepository;

    public List<GetOrdersDto> getOrdersList() {
        List<Orders> ordersList = repositorySupport.selectOrdersList();
        List<GetOrdersDto> rtnList = new ArrayList<>();
        for (Orders o : ordersList) {
            GetOrdersDto dto = new GetOrdersDto(
                    o.getId(),
                    o.getOrderDt(),
                    o.getStatus(),
                    o.getDelivery().getId(),
                    o.getDelivery().getAddress().getCountry(),
                    o.getDelivery().getAddress().getZipCode(),
                    o.getDelivery().getAddress().getCity(),
                    o.getDelivery().getAddress().getStreet(),
                    o.getDelivery().getAddress().getEtc(),
                    o.getUser().getId(),
                    o.getUser().getName(),
                    o.getUser().getIdentifier(),
                    o.getUser().getType()
            );

            rtnList.add(dto);
        }

        return rtnList;
    }

    @Transactional
    public void save(OrdersVo ordersVo) throws Exception {
        Optional<User> findUser = userRepository.findById(ordersVo.getUserId());
        findUser.orElseThrow(() -> {
            throw new APIException("ORDERS_SAVE_USER", "User doesn't exist.");
        });

        Optional<Item> findItem = itemRepository.findById(ordersVo.getItemId());
        findItem.orElseThrow(() -> {
            throw new APIException("ORDERS_SAVE_ITEM", "Item doesn't exist.");
        });

        Optional<Delivery> findDelivery = deliveryRepository.findById(ordersVo.getDeliveryId());
        findItem.orElseThrow(() -> {
            throw new APIException("ORDERS_SAVE_DELIVERY", "Delivery doesn't exist.");
        });

        Long orderedPrice = findItem.get().getPrice();
        Optional<Coupon> findCoupon = null;
        if(ordersVo.getCouponId() != null) {
            findCoupon = couponRepository.findById(ordersVo.getCouponId());
            findCoupon.orElseThrow(() -> {
                throw new APIException("ORDERS_SAVE_COUPON", "Coupon doesn't exist.");
            });
            orderedPrice = findCoupon.get().calculateOrderedPrice(orderedPrice);
        }

        Orders orders = new Orders();
        orders.insertOrders(ordersVo, findUser.get(), findDelivery.get());
        ordersRepository.save(orders);

        //TODO 주문 고도화
        //1. 하나의 주문에 여러 상품 처리
        //2. 각 상품 별로 N 개의 쿠폰 적용가능하도록 처리
        OrdersItem ordersItem = new OrdersItem();
        ordersItem.insertOrdersItem(orderedPrice, ordersVo.getQuantity(), orders, findItem.get());

        ordersItemRepository.save(ordersItem);

        findItem.get().decreaseItemQuantity(ordersVo.getQuantity());
    }

    @Transactional
    public void update(OrdersVo ordersVo) throws Exception {
        Optional<Orders> findOrders = ordersRepository.findById(ordersVo.getId());
        findOrders.orElseThrow(() -> {
            throw new APIException("ORDERS_0002", "Order doesn't exist.");
        });

        findOrders.get().updateOrders(ordersVo);
    }

    @Transactional
    public void delete(OrdersVo ordersVo) {
        Optional<Orders> findOrders = ordersRepository.findById(ordersVo.getId());
        findOrders.orElseThrow(() -> {
            throw new APIException("ORDERS_0002", "Order doesn't exist.");
        });

        findOrders.get().deleteOrders(ordersVo);
    }

}