package com.digitalmoney.cardservice.repository;

import com.digitalmoney.cardservice.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.xml.transform.Result;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    @Query(value = "SELECT * FROM cards WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    List<Card> getAllByAccountId(@Param("accountId") Integer accountId);

    @Modifying
    @Transactional
    @Query("DELETE FROM cards WHERE ACCOUNT_ID = :accountId AND ID = :id")
    void deleteCardByAccountIdAndId(@Param("accountId") Integer accountId, @Param("id") Integer id);

    @Query(value = "SELECT * FROM cards WHERE ACCOUNT_ID = :accountId AND ID = :id", nativeQuery = true)
    Card getCardByAccountIdAndId(@Param("accountId") Integer accountId,  @Param("id") Integer id);

}