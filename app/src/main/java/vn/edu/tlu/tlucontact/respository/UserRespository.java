package vn.edu.tlu.tlucontact.respository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import vn.edu.tlu.tlucontact.models.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRespository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<String> errorMessageLiveData;

    public UserRespository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        errorMessageLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    public void register(final String email, String password, final String name, final String phoneNumber) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                // Xác định role dựa trên email
                                String role = email.endsWith("@tlu.edu.vn") ? "CBGV" : "SV";

                                User user = new User(userId, role, name, email, phoneNumber);

                                // Lưu vào Firestore
                                DocumentReference userRef = firestore.collection("users").document(userId);
                                userRef.set(user)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                userLiveData.postValue(firebaseUser);
                                            } else {
                                                errorMessageLiveData.postValue("Lỗi khi lưu dữ liệu: " + task1.getException().getMessage());
                                            }
                                        });
                            }
                        } else {
                            errorMessageLiveData.postValue("Đăng ký thất bại: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                        } else {
                            errorMessageLiveData.postValue("Đăng nhập thất bại: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }
}
