package com.telerik.virtualwallet.repositories.transactionCategory;

import com.telerik.virtualwallet.models.TransactionCategory;

import java.util.List;

public interface TransactionCategoryRepository {

    List<TransactionCategory> getTransactionCategories();

    TransactionCategory getTransactionCategory(int id);
}
