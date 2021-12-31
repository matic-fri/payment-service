package user.lib;

public class Account {

    private Long id;

    private Long userId;

    private Integer balance;

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
