package mtaxi;

import mtaxi.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderApproved_ConfirmOrder(@Payload OrderApproved orderApproved){
        System.out.println("##### test orderApproved 1 " + orderApproved.toJson());
        if(orderApproved.isMe()){
            System.out.println("##### test orderApproved 2 ");
            System.out.println("##### listener ConfirmOrder : " + orderApproved.toJson());

            orderRepository.findById(Long.valueOf(orderApproved.getOrderId())).ifPresent((Order) -> {
                System.out.println("##### test orderApproved 3 ");
                Order.setDriverId(orderApproved.getDriverId());
                Order.setOrderId(orderApproved.getOrderId());
                Order.setStatus("Confirmed");

                orderRepository.save(Order);
            });

        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderDenied_DenyOrder(@Payload OrderDenied orderDenied){

        System.out.println("##### test orderdenied 1 " + orderDenied.toJson());
        if(orderDenied.isMe()){
            System.out.println("##### test orderdenied 2 ");
            System.out.println("##### listener DenyOrder : " + orderDenied.toJson());

            orderRepository.findById(Long.valueOf(orderDenied.getOrderId())).ifPresent((Order)->{
                System.out.println("##### test orderdenied 3 ");
                Order.setDriverId(orderDenied.getDriverId());
                Order.setOrderId(orderDenied.getOrderId());
                Order.setStatus("Denied");
                orderRepository.save(Order);
            });
        }
    }

}
