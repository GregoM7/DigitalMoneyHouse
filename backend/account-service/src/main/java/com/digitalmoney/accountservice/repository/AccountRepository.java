package com.digitalmoney.accountservice.repository;

import com.digitalmoney.accountservice.entity.Account;
import com.digitalmoney.accountservice.entity.AccountDestiny;
import com.digitalmoney.accountservice.entity.AccountsList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query(value = "SELECT distinct T.account_destiny_id,T.date, \n" +
            "IFNULL(U.cvu,'') as CVU, IFNULL(U.CBU,'') as CBU ,U.last_name , U.name\t\n" +
            "FROM transactions T\n" +
            "join accounts A \n" +
            "on T.account_destiny_id = A.id\n" +
            "join users U \n" +
            "on A.user_id = U.id\n" +
            "WHERE account_origin_id = ?1 ORDER BY date DESC LIMIT 5",
    nativeQuery = true)
    List<Object> getLastAccountList(Integer accountId);
}
