package com.codecool.bankaccountstarter.controller;

import com.codecool.bankaccountstarter.controller.exception.InvalidCurrencyException;
import com.codecool.bankaccountstarter.model.Account;
import com.codecool.bankaccountstarter.model.Currency;
import com.codecool.bankaccountstarter.model.dto.AccountDto;
import com.codecool.bankaccountstarter.model.dto.AccountMapper;
import com.codecool.bankaccountstarter.model.dto.BalanceDto;
import com.codecool.bankaccountstarter.model.dto.requests.*;
import com.codecool.bankaccountstarter.model.exception.DuplicateAccountCodeException;
import com.codecool.bankaccountstarter.model.exception.InsufficientFundsException;
import com.codecool.bankaccountstarter.model.exception.UnauthorizedOperationException;
import com.codecool.bankaccountstarter.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AccountController {

    private AccountService accountService;
    private AccountMapper accountMapper;

    @Autowired
    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/api/account/{id}")
    public ResponseEntity<AccountDto> getAccountDataById(@PathVariable Long id) {
        AccountDto result;
        try {
            Account foundAccount = accountService.getAccountById(id).orElseThrow();
            result = accountMapper.accountEntityToDto(foundAccount);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/account/with/code/{code}")
    public ResponseEntity<AccountDto> getAccountDataById(@PathVariable String code) {
        AccountDto result;
        try {
            Account foundAccount = accountService.getAccountByCode(code).orElseThrow();
            result = accountMapper.accountEntityToDto(foundAccount);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/account/{id}/balance")
    public ResponseEntity<BalanceDto> getBalanceForAccountId(@PathVariable Long id) {
        Account foundAccount;
        try {
            foundAccount = accountService.getAccountById(id).orElseThrow();
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        try {
            BalanceDto result = accountService.displayBalance(foundAccount);
            return ResponseEntity.ok().body(result);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<AccountDto> createAccountInLocalSchema(@RequestBody @Valid CreateAccountDto requestDto) {
        String accountOwnerName = requestDto.getOwnerName();
        String accountPassword = requestDto.getPassword();
        AccountDto accountDataToCreate = null;
        try {
            accountDataToCreate = accountService.createLocalAccount(accountOwnerName, accountPassword);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }
        if (accountDataToCreate != null) {
            Long createdAccountId = accountDataToCreate.getId();
            URI createdAccountUri = linkTo(
                    methodOn(AccountController.class).getAccountDataById(createdAccountId)
            ).toUri();
            return ResponseEntity.created(createdAccountUri).body(accountDataToCreate);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/accounts/specific")
    public ResponseEntity<?> createSpecificAccount(@RequestBody @Valid CreateSpecificAccountDto requestDto) {
        String accountOwnerName = requestDto.getOwnerName();
        String accountPassword = requestDto.getPassword();
        String accountCode = requestDto.getCode();
        AccountDto accountDataToCreate = null;
        try {
            accountDataToCreate = accountService.createSpecificAccount(accountCode, accountOwnerName, accountPassword);
        } catch (DuplicateAccountCodeException e) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("code", "Account code already exists!");
            return ResponseEntity.status(403).body(responseBody);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }
        if (accountDataToCreate != null) {
            Long createdAccountId = accountDataToCreate.getId();
            URI createdAccountUri = linkTo(
                    methodOn(AccountController.class).getAccountDataById(createdAccountId)
            ).toUri();
            return ResponseEntity.created(createdAccountUri).body(accountDataToCreate);
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("api/account/{id}/deposit")
    public ResponseEntity<?> depositAmountToAccountId(
            @PathVariable Long id,
            @RequestBody @Valid DepositDto requestBody) throws InvalidCurrencyException
    {
        Account foundAccount;
        try {
            foundAccount = accountService.getAccountById(id).orElseThrow();
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> responseBody = new HashMap<>();
        double depositedAmount = requestBody.getAmount();
        Currency ammountCurrency = null;
        try {
            ammountCurrency = Enum.valueOf(Currency.class, requestBody.getAmountCurrency());
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException("Currency name for deposit is not recognized!");
        }
        String confirmationPayload = requestBody.getConfirmationPayload();

        try {
            accountService.depositAmount(depositedAmount, ammountCurrency, foundAccount, confirmationPayload);
        } catch (UnauthorizedOperationException e) {
            responseBody.put("endpointError", e.getMessage());
            return ResponseEntity.status(401).body(responseBody);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.status(204).build();
    }

    @PutMapping("/api/account/{id}/withdraw")
    public ResponseEntity<?> withdrawFromAccountId(
            @PathVariable Long id,
            @RequestBody @Valid WithdrawDto requestBody
    ) throws InvalidCurrencyException {
        Account foundAccount;
        try {
            foundAccount = accountService.getAccountById(id).orElseThrow();
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> responseBody = new HashMap<>();
        double withdrawnAmount = requestBody.getAmount();
        Currency ammountCurrency = null;
        try {
            ammountCurrency = Enum.valueOf(Currency.class, requestBody.getAmountCurrency());
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException("Currency name to withdraw to is not recognized!");
        }
        String verificationPayload = requestBody.getVerificationPayload();

        try {
            accountService.withdrawAmount(withdrawnAmount, ammountCurrency, foundAccount, verificationPayload);
        } catch (InsufficientFundsException e) {
            return getNotAllowedResponseWithBody(responseBody, e.getMessage(), id);
        } catch (UnauthorizedOperationException e) {
            responseBody.put("endpointError", e.getMessage());
            return ResponseEntity.status(401).body(responseBody);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.status(204).build();
    }


    @PutMapping("/api/transfer")
    public ResponseEntity<?> performTransfer(@RequestBody @Valid TransferDto requestBody)
            throws InvalidCurrencyException {
        Long fromAccountId = requestBody.getFromAccountId();
        Long toAccountId = requestBody.getToAccountId();
        Account foundFromAccount;
        Account foundToAccount;
        boolean succeededFrom = false;
        Map<String, String> responseBody = new HashMap<>();
        try {
            foundFromAccount = accountService.getAccountById(fromAccountId).orElseThrow();
            succeededFrom = true;
            foundToAccount = accountService.getAccountById(toAccountId).orElseThrow();
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        } catch (NoSuchElementException e) {
            if (succeededFrom) {
                responseBody.put("toAccountId", "Not found");
            } else {
                responseBody.put("fromAccountId","Not found");
            }
            return ResponseEntity.status(404).body(responseBody);
        }

        double amountToTransfer = requestBody.getAmount();
        Currency ammountCurrency;
        try {
            ammountCurrency = Enum.valueOf(Currency.class, requestBody.getAmountCurrency());
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException("Currency name in which to transfer is not recognized!");
        }
        try {
            accountService.transferAmount(amountToTransfer, ammountCurrency, foundFromAccount, foundToAccount);
        } catch (InsufficientFundsException e) {
            return getNotAllowedResponseWithBody(responseBody, e.getMessage(), fromAccountId);
        } catch (TransactionSystemException e) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.status(204).build();
    }

    private ResponseEntity<?> getNotAllowedResponseWithBody(
            Map<String, String> responseBody,
            String errorMessage,
            Long idForRedirect
    ) {
        responseBody.put("amount", errorMessage);
        responseBody.put("extraInfo", "Please check the balance for account id: " + idForRedirect);
        URI balanceUri = linkTo(methodOn(AccountController.class).getBalanceForAccountId(idForRedirect)).toUri();
        responseBody.put("infoRedirect", balanceUri.toString());
        return ResponseEntity.status(405).body(responseBody);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCurrencyException.class)
    public Map<String, String> handleInvalidCurrencyException(InvalidCurrencyException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("currency", ex.getMessage());
        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return getFieldToErrorMap(ex);
    }


    static Map<String, String> getFieldToErrorMap(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                }
        );
        return errors;
    }
}
