package lk.xtracheese.swiftsalon.viewmodel;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


import lk.xtracheese.swiftsalon.model.User;
import lk.xtracheese.swiftsalon.repository.UserRepository;
import lk.xtracheese.swiftsalon.request.response.GenericResponse;
import lk.xtracheese.swiftsalon.util.Resource;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageViewModel extends AndroidViewModel {
    private static final String TAG = "ImageViewModel";


    private UserRepository userRepository;
    private Application application;


    private MediatorLiveData<Resource<GenericResponse<User>>> user = new MediatorLiveData<>();

    private boolean isFetching;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
       userRepository = UserRepository.getInstance(application);

    }



    public LiveData<Resource<GenericResponse<User>>> saveUserImage() {
        return user;
    }



    public void saveUserImageApi(int customerId, Uri imageUri) {
        if (!isFetching) {
            executeUser(customerId, getUploadImagePart(imageUri));
        }
    }



    private void executeUser(int cusId, MultipartBody.Part image) {
        isFetching = true;

        final LiveData<Resource<GenericResponse<User>>> repositorySource = userRepository.updateCustomerImageApi(cusId, image);

        user.addSource(repositorySource, resource -> {
            if (resource != null) {
                user.setValue(resource);
                if (resource.status == Resource.Status.SUCCESS) {
                    isFetching = false;
                } else if (resource.status == Resource.Status.ERROR) {
                    isFetching = false;
                    user.removeSource(repositorySource);
                }
            } else {
                user.removeSource(repositorySource);
            }
        });
    }

    private MultipartBody.Part getUploadImagePart(Uri imageUri) {
        File file = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = application.getApplicationContext()
                    .getContentResolver()
                    .openFileDescriptor(imageUri, "r", null);

            InputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            file = new File(getApplication().getCacheDir(), getFileName(imageUri));
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(file != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            return MultipartBody.Part.createFormData("userfile", file.getName(), requestBody);
        }
        else {
            return null;
        }
    }

    private String getFileName(Uri uri) {

        String name = "";
        if (uri != null) {

            Cursor cursor = application.getApplicationContext()
                    .getContentResolver()
                    .query(uri, null, null, null, null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                cursor.moveToFirst();
                name = cursor.getString(nameIndex);
                cursor.close();
            }

        }
        return name;
    }
}
