package beertech.becks.api.converter;

import beertech.becks.api.entities.Bank;
import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.exception.account.AccountDoesNotExistsException;
import beertech.becks.api.exception.bank.BankDoesNotExistsException;
import beertech.becks.api.exception.paymentslip.PaymentSlipRegisterException;
import beertech.becks.api.model.PaymentCategory;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.BankService;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentSlipConverter {

  @Autowired
  AccountService accountService;
  @Autowired
  BankService bankService;

  public PaymentSlip codeToPaymentSlip(String code)
      throws Exception {

    String[] splitedPaymentSlip = decodeSlip(code).split("-");

    if(!validatePaymentSlipCode(splitedPaymentSlip)){
      throw new PaymentSlipRegisterException("Payment slip code is not valid");
    }

    String strDate = splitedPaymentSlip[0];
    String strValue = splitedPaymentSlip[1];
    String strCategory = splitedPaymentSlip[2];
    String[] originBank = splitedPaymentSlip[3].split("/");
    String[] destinationBank = splitedPaymentSlip[4].split("/");

    LocalDate dueDate = null;
    try {
      dueDate = LocalDate.parse(strDate, DateTimeFormatter.BASIC_ISO_DATE);
    } catch (DateTimeParseException e) {
      throw new PaymentSlipRegisterException("Date is not valid");
    }

    Bank bank = bankService.findByCode(originBank[0]);

    if(bank.getName().equalsIgnoreCase("BECKS")){
      return PaymentSlip.builder()
          .code(code)
          .dueDate(dueDate)
          .value(new BigDecimal(strValue).divide(BigDecimal.valueOf(100)))
          .category(PaymentCategory.getObject(strCategory))
          .user(accountService.getAccountByCode(originBank[1]).getUser())
          .originAccountCode(originBank[1])
          .destinationBankCode(destinationBank[0])
          .destinationAccountCode(destinationBank[1])
          .build();
    }else{
      throw new PaymentSlipRegisterException("Error on register payment slip");
    }
  }

  private static String decodeSlip(String text) throws UnsupportedEncodingException {
    byte[] myBytes = DatatypeConverter.parseHexBinary(text);
    return new String(myBytes,"UTF-8");
  }

  private boolean validatePaymentSlipCode(String[] splited) {
    if (splited[0].matches("[0-9]{8}")
        && splited[1].matches("[0-9]{6}")
        && splited[2].matches("[A-Z]{2}")
        && splited[3].matches("[0-9]{3}/[0-9]{5}")
        && splited[4].matches("[0-9]{3}/[0-9]{5}")) {
      return true;
    }
    return false;
  }
}
