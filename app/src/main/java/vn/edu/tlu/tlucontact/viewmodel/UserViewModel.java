package vn.edu.tlu.tlucontact.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import vn.edu.tlu.tlucontact.respository.UserRespository;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends AndroidViewModel {
    private UserRespository userRespository;
    private LiveData<FirebaseUser> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRespository = new UserRespository();
        userLiveData = userRespository.getUserLiveData();
    }

    public void register(String email, String password, String name, String phoneNumber) {
        userRespository.register(email, password, name, phoneNumber);
    }

    public void login(String email, String password) {
        userRespository.login(email, password);
    }

    public void logOut() {
        userRespository.logOut();
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}