package lu.pistache.overconcentrated.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Random;

@Getter
@Setter
public class Account {
    private Integer id;
    private Integer customerId;
    private String name;
    private Date creationDate;

    public Account(Integer id, Integer customerId) {
        this.id = id;
        this.customerId = customerId;
        Random rand = new Random();
        String iban = "LU" + rand.nextInt(99) + " " + rand.nextInt(9999)  + " " + rand.nextInt(9999) + " " + rand.nextInt(9999) + " " + rand.nextInt(9999);
        this.setName(iban);
        this.setCustomerId(customerId);
        this.setCreationDate(new Date());
    }

}
