package user.models.converters;


import user.lib.Payment;
import user.models.entities.PaymentEntity;

public class PaymentConverter {

    public static Payment toDto(PaymentEntity entity) {
        Payment dto = new Payment();
        dto.setId(entity.getId());
        dto.setSenderId(entity.getSenderId());
        dto.setReceiverId(entity.getReceiverId());
        dto.setFileId(entity.getFileId());
        dto.setAmount(entity.getAmount());

        return dto;

    }

    public static PaymentEntity toEntity(Payment dto){
        PaymentEntity entity = new PaymentEntity();
        entity.setSenderId(dto.getSenderId());
        entity.setReceiverId(dto.getReceiverId());
        entity.setFileId(dto.getFileId());
        entity.setAmount(dto.getAmount());

        return entity;

    }
}
