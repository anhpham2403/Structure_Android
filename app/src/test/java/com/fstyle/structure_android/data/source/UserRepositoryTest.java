package com.fstyle.structure_android.data.source;

import com.fstyle.structure_android.data.model.User;
import com.fstyle.structure_android.data.source.local.UserLocalDataSource;
import com.fstyle.structure_android.data.source.remote.UserRemoteDataSource;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Created by Sun on 3/11/2017.
 */
public class UserRepositoryTest {

    private static final String USER_LOGIN_1 = "abc";
    private static final String USER_LOGIN_2 = "def";

    @InjectMocks
    private UserRepositoryImpl mUserRepository;
    @Mock
    UserLocalDataSource mLocalDataSource;
    @Mock
    UserRemoteDataSource mRemoteDataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchUsers_200ResponseCode_InvokesCorrectApiCalls() {
        User user = new User(USER_LOGIN_1);
        User user2 = new User(USER_LOGIN_2);

        List<User> userReturns = new ArrayList<>();
        userReturns.add(user);
        userReturns.add(user2);
        // Given
        Mockito.when(mRemoteDataSource.searchUsers(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyInt())).thenReturn(Single.just(userReturns));

        // When
        TestObserver<List<User>> subscriber = new TestObserver<>();
        mUserRepository.searchUsers(USER_LOGIN_1, 2).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<List<User>> onNextEvents = subscriber.values();
        List<User> users = onNextEvents.get(0);
        Assert.assertEquals(USER_LOGIN_1, users.get(0).getLogin());
        Assert.assertEquals(USER_LOGIN_2, users.get(1).getLogin());
        Mockito.verify(mRemoteDataSource).searchUsers(USER_LOGIN_1, 2);
    }

    @Test
    public void searchUsers_OtherHttpError_SearchTerminatedWithError() {
        // Given
//        Mockito.when(mRemoteDataSource.searchUsers(ArgumentMatchers.anyString(),
//                ArgumentMatchers.anyInt()))
//                .thenReturn(Observable.error(new HttpException(Response.error(403,
//                        ResponseBody.create(MediaType.parse("application/json"), "Forbidden")))));
//
//        // When
//        TestObserver<List<User>> subscriber = new TestObserver<>();
//        mUserRepository.searchUsers(USER_LOGIN_1, 2).subscribe(subscriber);
//
//        // Then
//        subscriber.awaitTerminalEvent();
//        subscriber.assertError(HttpException.class);
//
//        Mockito.verify(mRemoteDataSource).searchUsers(USER_LOGIN_1, 2);
//        Mockito.verify(mRemoteDataSource, Mockito.never()).searchUsers(USER_LOGIN_2, 2);
    }
}
