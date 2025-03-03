package com.telerik.virtualwallet.services.transactionCategory;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.TransactionCategory;
import com.telerik.virtualwallet.repositories.transactionCategory.TransactionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionCategoryServiceImpl implements TransactionCategoryService {

    private final TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    public TransactionCategoryServiceImpl(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    @Override
    public List<TransactionCategory> getTransactionCategories() {
        return transactionCategoryRepository.getTransactionCategories();
    }

    @Override
    public TransactionCategory getTransactionCategory(int id) {
        TransactionCategory transactionCategory = transactionCategoryRepository.getTransactionCategory(id);

        if (transactionCategory == null) {
            throw new EntityNotFoundException("Transaction category", "id", id);
        }

        return transactionCategory;
    }
}
