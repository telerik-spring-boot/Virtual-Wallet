package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Investment;

import java.util.List;

public interface InvestmentRepository {

    List<Investment> getAllInvestments();

    List<Investment> getAllInvestmentsByUsername(String username);
}
