package user.models.entities;

import javax.persistence.*;


@Entity
@Table(name = "account")
@NamedQueries(value =
        {
                @NamedQuery(name = "AccountEntity.getAll",
                        query = "SELECT im FROM AccountEntity im")
        })
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "reserved")
    private Integer reserved;


    // getters
    public Long getId() {
        return this.id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public Integer getBalance() {
        return this.balance;
    }
    public Integer getReserved() {
        return this.reserved;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setBalance(Integer balance) {
        this.balance = balance;
    }
    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
}
