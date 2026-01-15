package duc.demo.dto.request;


import lombok.Getter;

import java.io.Serializable;

//Lombok sử dụng cho hợp lý
//Best practice for DTO : Getter, vì không cần hash code, không cần equals, cũng không cần sửa dữ liệu
// nó chỉ lưu dữ liệu -> không chỉnh sửa

//nếu là response: Getter + Builder


//@Data = getter + setter + equalssandhashcode + tostring
@Getter

public class AddressDTO implements Serializable {
    private String apartmentNumber;
    private String floor;
    private String building;
    private String streetNumber;
    private String street;
    private String city;
    private String country;
    private Integer addressType;


}
