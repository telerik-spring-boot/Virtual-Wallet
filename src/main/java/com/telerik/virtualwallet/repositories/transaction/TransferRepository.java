package com.telerik.virtualwallet.repositories.transaction;

import com.telerik.virtualwallet.models.Transfer;

public interface TransferRepository {

    void createTransfer(Transfer transfer);
}
