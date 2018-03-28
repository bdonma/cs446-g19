package com.ba.cg.jn.tl.barter;

/**
 * Created by JasonNgo on 2018-03-25.
 */

interface TransactionViewInterface {
    void showTransactionInformation(Transaction transaction);
    void showApprovalScreen();
    void showInformationScreen();
    void setFriendTextView(String str);
}
