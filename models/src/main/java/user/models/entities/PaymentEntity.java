package user.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "payment")
@NamedQueries(value =
        {
                @NamedQuery(name = "PaymentEntity.getAll",
                        query = "SELECT im FROM PaymentEntity im")
        })
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "amount")
    private Integer amount;


    // getters
    public Long getId() {
        return this.id;
    }
    public Long getSenderId() {
        return this.senderId;
    }
    public Long getReceiverId() {
        return this.receiverId;
    }
    public Long getFileId() {
        return this.fileId;
    }
    public Integer getAmount() {
        return this.amount;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
