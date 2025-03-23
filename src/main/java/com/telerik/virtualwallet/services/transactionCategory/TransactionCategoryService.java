package com.telerik.virtualwallet.services.transactionCategory;

import com.telerik.virtualwallet.models.TransactionCategory;

import java.util.List;

public interface TransactionCategoryService {

    List<TransactionCategory> getTransactionCategories();

    TransactionCategory getTransactionCategory(int id);

    TransactionCategory getTransactionCategoryByName(String name);
}
