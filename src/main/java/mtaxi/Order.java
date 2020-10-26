package mtaxi;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long orderId;
    private Long driverId;
    private String customerName;
    private String location;
    private String status;

    @PostPersist
    public void onPostPersist(){

        System.out.println("##### 11. order onPostPersist #####");
        if(null == this.getStatus()||"".equals(this.getStatus())){
            System.out.println("##### status is null #####");
            return;
        }
        if("Ordered".equals(this.getStatus())){
            System.out.println("##### status is Ordered #####");
            Ordered ordered = new Ordered();
            ordered.setOrderId(this.getOrderId());
            ordered.setDriverId(this.getDriverId());
            ordered.setCustomerName(this.getCustomerName());
            ordered.setLocation(this.getLocation());

            BeanUtils.copyProperties(this, ordered);
            ordered.publishAfterCommit();
        }

    }

    @PostUpdate
    public void onPostUpdate(){
        System.out.println("##### 1. order onPostUpdate #####");
        System.out.println("##### 1. order onPostUpdate getStatus#####: " + this.getStatus() + ">");
        if(this.getStatus().equals("Denied")) {
            OrderDenied orderDenied = new OrderDenied();
            System.out.println("##### 통과 DENIED #####");
            BeanUtils.copyProperties(this, orderDenied);
            orderDenied.publishAfterCommit();
        }
        else if(this.getStatus().equals("Confirmed"))
        {
            OrderApproved orderApproved = new OrderApproved();
            System.out.println("##### 통과 Confirmed #####");
            BeanUtils.copyProperties(this, orderApproved);
            orderApproved.publishAfterCommit();
        }else if("Canceled".equals(this.getStatus())) {
            System.out.println("##### status is Canceled. not Ordered #####");

            OrderCanceled orderCanceled = new OrderCanceled();

            orderCanceled.setOrderId(this.getOrderId());
            orderCanceled.setDriverId(this.getDriverId());
            orderCanceled.setLocation(this.getLocation());
            orderCanceled.setCustomerName(this.getCustomerName());

            BeanUtils.copyProperties(this, orderCanceled);
            orderCanceled.publishAfterCommit();
        }

    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
