package lk.xtracheese.swiftsalon.request.response;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * Generic class for handling responses from Retrofit
 *
 * @param <T>
 */
public class ApiResponse<T> {
    private static final String TAG = "ApiResponse";

    public ApiResponse<T> create(Throwable error) {
        String message = "";
        if (error instanceof SocketTimeoutException) {
            message = "Time out. Check your connection and try again.";
        } else if (error instanceof UnknownHostException) {
            message = "Oops! something went wrong.";
        } else if(error instanceof IllegalStateException) {
            message = "Oops! something went wrong.";
        }
        else {
            Log.d(TAG, "create: " + error.getLocalizedMessage());
            Log.d(TAG, "create: MSG: " + error.getMessage());
        }
        return new ApiErrorResponse<>(!message.equals("") ? message : "We are unable to connect you with us.");
    }

    public ApiResponse<T> create(Response<T> response) {

        if (response.isSuccessful()) {
            T body = response.body();
            Log.d(TAG, "create: BODY" + response.body().toString() );
            if (body instanceof GenericResponse) {
                if (!CheckToken.isTokenValid((GenericResponse) body)) {
                    String errorMsg = "Token expired.";
                    return new ApiErrorResponse<>(errorMsg);
                }
            }

            if (body == null || response.code() == 204) { // 204 is empty response
                return new ApiEmptyResponse<>();
            } else {
                Log.d(TAG, "create: body: " + body.toString());
                return new ApiSuccessResponse<>(body);
            }
        } else {
            String errorMsg = "";
            try {
                errorMsg = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
                errorMsg = response.message();
            }
            Log.d(TAG, "create: ERROR MSG: " + errorMsg);
            if(errorMsg.length() > 100) {
                errorMsg = "Something went wrong. Try again later.";
            }
            Log.d(TAG, "create: ERROR: " + response.errorBody().toString());
            return new ApiErrorResponse<>(errorMsg);
        }
    }



    /**
     * Generic success response from api
     *
     * @param <T>
     */
    public class ApiSuccessResponse<T> extends ApiResponse<T> {

        private T body;

        ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }

    }

    /**
     * Generic Error response from API
     *
     * @param <T>
     */
    public class ApiErrorResponse<T> extends ApiResponse<T> {

        private String errorMessage;

        ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }


    /**
     * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
     */
    public class ApiEmptyResponse<T> extends ApiResponse<T> {
    }
}
