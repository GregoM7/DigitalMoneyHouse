package com.digitalmoney.transactionservice.repository.custom;

import com.digitalmoney.transactionservice.entity.Transaction;
import com.digitalmoney.transactionservice.filters.TransactionFilters;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CustomPage<Transaction> findByFilters(TransactionFilters filtros, Pageable pageable) {

        CustomPage<Transaction> customPage = new CustomPage<Transaction>();

        //Query para obtener los transacciones filtradas
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);
        Root<Transaction> transactionRoot = cq.from(Transaction.class);
        buildQueryWithFilters(filtros, cb,cq, transactionRoot);
        List<Transaction> result = entityManager.createQuery(cq)
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageNumber()* pageable.getPageSize())
                .getResultList();
        customPage.setContent(result);

        //Query para obtener la cantidad total de transacciones ya filtradas
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Transaction> root = query.from(Transaction.class);
        query.select(cb.count(root));
        buildQueryWithFilters(filtros, cb , query, root);
        long cantidad = entityManager.createQuery(query).getSingleResult();
        customPage.setTotalElements(cantidad);

        return customPage;
    }

    private void buildQueryWithFilters
            (TransactionFilters filtros, CriteriaBuilder cb, CriteriaQuery<?> cq, Root<?> root) {

        Predicate restrictions = cb.conjunction();
        Predicate predicateType = null,predicateAmount=null,predicateDate=null,predicateAccounts=null;
        ArrayList<Predicate> lv_predicates = new ArrayList<>();

        if (filtros.hasType()){

           if(filtros.getType().equals("egreso")){
                predicateType = cb.and( cb.equal(root.get("type"), filtros.getType()), cb.equal(root.get("accountOriginId"), filtros.getIdAccount()));
                predicateAccounts= cb.and(cb.equal(root.get("accountOriginId"), filtros.getIdAccount()));
            }else{
               predicateType = cb.and( cb.equal(root.get("type"), filtros.getType()), cb.equal(root.get("accountDestinyId"), filtros.getIdAccount()));
               predicateAccounts =cb.and(cb.equal(root.get("accountDestinyId"), filtros.getIdAccount()));
           }

           lv_predicates.add(predicateType);
        }else{
            predicateAccounts = cb.or(cb.equal(root.get("accountOriginId"), filtros.getIdAccount()),
                    cb.equal(root.get("accountDestinyId"), filtros.getIdAccount()));
        }

        lv_predicates.add(predicateAccounts);

        if(filtros.hasAmount()) {
               predicateAmount = cb.and(restrictions, cb.between(root.get("amount").as(Double.class),
                       filtros.getAmountMin(),
                       filtros.getAmountMax()));
               lv_predicates.add(predicateAmount);
        }

        if (filtros.hasFechaDesde() && filtros.hasFechaHasta()){
            predicateDate = cb.between(root.get("date"),filtros.getFechaDesde(),filtros.getFechaHasta());

            lv_predicates.add(predicateDate);
        }

        cq.where(lv_predicates.toArray(new Predicate[0]));
    }
}