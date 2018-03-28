package com.ba.cg.jn.tl.barter;

/**
 * Created by JasonNgo on 2018-03-25.
 */

interface TransactionViewInterface {
    void showTransactionInformation(Transaction transaction);
    void showApprovalScreen();
    void showApprovalScreenWithEditTransactionDisabled();
    void showInformationScreen();
    void setFriendTextView(String str);
    void enableTransactionCompleteButton();
    void disableTransactionCompleteButton();

} // TransactionViewInterface

