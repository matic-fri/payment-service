package user.models.converters;

import user.lib.Account;
import user.models.entities.AccountEntity;

public class AccountConverter {

    public static Account toDto(AccountEntity entity) {
        Account dto = new Account();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setBalance(entity.getBalance());
        dto.setReserved(entity.getReserved());

        return dto;

    }

    public static AccountEntity toEntity(Account dto){
        AccountEntity entity = new AccountEntity();
        entity.setUserId(dto.getUserId());
        entity.setBalance(dto.getBalance());
        entity.setReserved(dto.getReserved());

        return entity;

    }
}
