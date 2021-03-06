package com.renanferrari.testeandroidv2.infrastructure;

import com.renanferrari.testeandroidv2.domain.model.statements.Statement;
import com.renanferrari.testeandroidv2.domain.model.user.User;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.renanferrari.testeandroidv2.common.Constants.FAILED_LOGIN_RESPONSE;
import static com.renanferrari.testeandroidv2.common.Constants.FAILED_STATEMENTS_RESPONSE;
import static com.renanferrari.testeandroidv2.common.Constants.INVALID_USER_OR_PASSWORD_ERROR;
import static com.renanferrari.testeandroidv2.common.Constants.SUCCESSFUL_LOGIN_RESPONSE;
import static com.renanferrari.testeandroidv2.common.Constants.SUCCESSFUL_STATEMENTS_RESPONSE;
import static com.renanferrari.testeandroidv2.common.Constants.USER_NOT_FOUND_ERROR;
import static com.renanferrari.testeandroidv2.common.Constants.VALID_EMAIL;
import static com.renanferrari.testeandroidv2.common.Constants.VALID_PASSWORD;
import static com.renanferrari.testeandroidv2.common.Constants.VALID_STATEMENT_LIST;
import static com.renanferrari.testeandroidv2.common.Constants.VALID_USER;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class BankServiceTest {

  @Mock private BankApi bankApi;

  private BankService bankService;

  @Before public void setUp() {
    bankService = new BankService(bankApi);
  }

  @Test public void login_withSuccessfulResponse_shouldMapUser() {
    when(bankApi.login(anyString(), anyString()))
        .thenReturn(Single.just(SUCCESSFUL_LOGIN_RESPONSE));

    final TestObserver<User> testObserver = bankService.login(VALID_EMAIL, VALID_PASSWORD).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertComplete();
    testObserver.assertValue(VALID_USER);
    testObserver.assertNoErrors();

    verify(bankApi).login(anyString(), anyString());
  }

  @Test public void login_withFailedResponse_shouldThrowException() {
    when(bankApi.login(anyString(), anyString())).thenReturn(Single.just(FAILED_LOGIN_RESPONSE));

    final TestObserver<User> testObserver = bankService.login(VALID_EMAIL, VALID_PASSWORD).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNotComplete();
    testObserver.assertError(RuntimeException.class);
    testObserver.assertErrorMessage(INVALID_USER_OR_PASSWORD_ERROR.getMessage());

    verify(bankApi).login(anyString(), anyString());
  }

  @Test public void getRecentStatements_withSuccessfulResponse_shouldMapStatements() {
    when(bankApi.statements(anyInt())).thenReturn(Single.just(SUCCESSFUL_STATEMENTS_RESPONSE));

    final TestObserver<List<Statement>> testObserver = bankService.getRecentStatements(0).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertComplete();
    testObserver.assertValue(VALID_STATEMENT_LIST);
    testObserver.assertNoErrors();

    verify(bankApi).statements(anyInt());
  }

  @Test public void getRecentStatements_withFailedResponse_shouldThrowException() {
    when(bankApi.statements(anyInt())).thenReturn(Single.just(FAILED_STATEMENTS_RESPONSE));

    final TestObserver<List<Statement>> testObserver = bankService.getRecentStatements(0).test();

    testObserver.awaitTerminalEvent();
    testObserver.assertNotComplete();
    testObserver.assertError(RuntimeException.class);
    testObserver.assertErrorMessage(USER_NOT_FOUND_ERROR.getMessage());

    verify(bankApi).statements(anyInt());
  }
}