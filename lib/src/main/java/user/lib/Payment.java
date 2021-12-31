package user.lib;

public class Payment
{
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long fileId;
    private Integer amount;

    // getters
    public Long getId() {
        return this.id;
    }
    public Long getSenderId() { return this.senderId; }
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
